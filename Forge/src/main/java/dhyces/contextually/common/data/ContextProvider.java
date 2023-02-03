package dhyces.contextually.common.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.conditions.objects.*;
import dhyces.contextually.client.core.conditions.predicates.ItemContextPredicate;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.contexts.PartialBlockState;
import dhyces.contextually.client.core.contexts.objects.AbstractKeyContext;
import dhyces.contextually.client.core.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.core.icons.IIcon;
import dhyces.contextually.client.core.icons.objects.*;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.keys.MappingKey;
import dhyces.contextually.util.IconUtils;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class ContextProvider implements DataProvider {

    @Nullable
    LanguageProvider langProvider;
    PackOutput.PathProvider pathProvider;
    ExistingFileHelper fileHelper;
    String modid;
    final String contextPath = "contexts";

    public ContextProvider(@Nullable LanguageProvider langProvider, @Nonnull PackOutput output, @Nonnull ExistingFileHelper fileHelper, @Nonnull String modid) {
        this.langProvider = langProvider;
        Preconditions.checkNotNull(output);
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, contextPath);
        Preconditions.checkNotNull(fileHelper);
        this.fileHelper = fileHelper;
        Preconditions.checkNotNull(modid);
        this.modid = modid;
    }

    protected abstract void addContexts(ContextExporter exporter);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        final Map<ResourceLocation, GeneratedContext> data = Maps.newHashMap();
        addContexts((s, context, translation) -> {
            ResourceLocation rl = new ResourceLocation(modid, s);
            data.put(rl, context);
            if (!translation.isBlank()) {
                langProvider.add(rl.getNamespace() + ".contexts." + rl.getPath(), translation);
            }
        });

        return CompletableFuture.allOf(data.entrySet().stream().map(
                        entry -> {
                            GeneratedContext context = entry.getValue();
                            JsonElement element = context.toJson().getOrThrow(false, Contextually.LOGGER::error);
                            Path path = this.pathProvider.json(entry.getKey());
                            return DataProvider.saveStable(cache, element, path);
                        }
                ).toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public @Nonnull String getName() {
        return "Contextually: Context Provider for " + modid;
    }

    interface GeneratedContext {
        DataResult<JsonElement> toJson();
    }

    public interface ContextExporter {
        void export(String id, GeneratedContext context, String translation);
    }

    public static class ContextBuilder<K, V, F extends IKeyContext<K, V>> {
        private final Set<IIcon> iconSet = new HashSet<>();
        private final Set<IContextCondition> conditionSet = new HashSet<>();
        private final Set<K> targetSet = new HashSet<>();
        private final ContextFactory<K, V, F> factory;
        private String translation = "";

        private ContextBuilder(ContextFactory<K, V, F> factory) {
            this.factory = factory;
        }

        public static <K, V, F extends IKeyContext<K, V>> ContextBuilder<K, V, F> create(ContextFactory<K, V, F> factory) {
            return new ContextBuilder<>(factory);
        }

        public ContextBuilder<K, V, F> addIcon(IIcon icon) {
            iconSet.add(icon);
            return this;
        }

        public ContextBuilder<K, V, F> addCondition(IContextCondition condition) {
            conditionSet.add(condition);
            return this;
        }

        public ContextBuilder<K, V, F> addTarget(K target) {
            targetSet.add(target);
            return this;
        }

        public ContextBuilder<K, V, F> addTargets(List<K> targets) {
            targetSet.addAll(targets);
            return this;
        }

        public ContextBuilder<K, V, F> addTranslation(@Nonnull String translation) {
            this.translation = translation;
            return this;
        }

        public void export(String id, ContextExporter consumer) {
            var built = factory.create(iconSet, conditionSet, targetSet);
            consumer.export(id, () -> IKeyContext.CODEC.encodeStart(JsonOps.INSTANCE, built), translation);
        }

        public interface ContextFactory<K, V, T extends IKeyContext<K, V>> {
            T create(@Nonnull Set<IIcon> icons, @Nonnull Set<IContextCondition> conditions, @Nonnull Set<K> targetedStates);
        }
    }

    public static class BlockContextBuilder {
        private static final Codec<BlockContextBuilder> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("context_type").forGetter(blockContextBuilder -> "contextually:block_context"),
                        IIcon.CODEC.listOf().fieldOf("icons").forGetter(blockContextBuilder -> List.copyOf(blockContextBuilder.iconSet)),
                        Codec.optionalField("conditions", IContextCondition.CODEC.listOf())
                                .xmap(
                                        optional -> optional.orElse(List.of()),
                                        set -> Optional.ofNullable(set.isEmpty() ? null : List.copyOf(set))
                                )
                                .forGetter(blockContextBuilder -> List.copyOf(blockContextBuilder.conditionSet)),
                        MoreCodecs.BLOCK_OR_PARTIAL_CODEC.listOf().fieldOf("targets").forGetter(blockContextBuilder -> List.copyOf(blockContextBuilder.targetSet))
                ).apply(instance, BlockContextBuilder::new)
        );
        private final Set<IIcon> iconSet;
        private final Set<IContextCondition> conditionSet;
        private final Set<PartialBlockState> targetSet;
        private String translation = "";

        private BlockContextBuilder() {
            this("", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        private BlockContextBuilder(String s, List<IIcon> iconSet, List<IContextCondition> conditionSet, List<PartialBlockState> targetSet) {
            this.iconSet = new HashSet<>(iconSet);
            this.conditionSet = new HashSet<>(conditionSet);
            this.targetSet = new HashSet<>(targetSet);
        }

        public static BlockContextBuilder create() {
            return new BlockContextBuilder();
        }

        public BlockContextBuilder addIcon(IIcon icon) {
            iconSet.add(icon);
            return this;
        }

        public BlockContextBuilder addCondition(IContextCondition condition) {
            conditionSet.add(condition);
            return this;
        }

        public BlockContextBuilder addBlock(Block target) {
            targetSet.add(PartialBlockState.builder(target).build());
            return this;
        }

        public BlockContextBuilder addBlocks(List<Block> targets) {
            targets.forEach(block -> targetSet.add(PartialBlockState.builder(block).build()));
            return this;
        }

        public BlockContextBuilder addState(BlockState target) {
            targetSet.add(PartialBlockState.fromBlockState(target));
            return this;
        }

        public BlockContextBuilder addStates(List<BlockState> targets) {
            targets.forEach(blockState -> targetSet.add(PartialBlockState.fromBlockState(blockState)));
            return this;
        }

        public BlockContextBuilder addPartialState(PartialBlockState target) {
            targetSet.add(target);
            return this;
        }

        public BlockContextBuilder addPartialStates(List<PartialBlockState> targets) {
            targetSet.addAll(targets);
            return this;
        }

        public BlockContextBuilder addTranslation(@Nonnull String translation) {
            this.translation = translation;
            return this;
        }

        public void export(String id, ContextExporter consumer) {
            consumer.export(id, () -> CODEC.encodeStart(JsonOps.INSTANCE, this), translation);
        }
    }

    public static class IconFactories {
        private IconFactories() {throw new UnsupportedOperationException();}

        public static AnimatedIconBuilder animatedIconBuilder() {
            return new AnimatedIconBuilder();
        }

        public static ItemIcon itemIcon(Item item) {
            return new ItemIcon(item);
        }

        public static KeyIcon keyIcon(IKey key) {
            return IconUtils.of(key.getValue());
        }

        public static KeyIcon keyIcon(String key) {
            return IconUtils.of(key);
        }

        public static KeyIcon keyIcon(int key) {
            return IconUtils.of(key);
        }

        public static KeyTextureIcon keyTextureIcon(ResourceLocation texture) {
            return IconUtils.of(texture);
        }

        public static TextureAtlasIcon textureAtlasIcon(ResourceLocation atlasLocation, ResourceLocation spriteLocation, int color) {
            return new TextureAtlasIcon(atlasLocation, spriteLocation, color);
        }

        public static class AnimatedIconBuilder {
            List<Pair<IIcon, Integer>> timedIcons = new ArrayList<>();

            private AnimatedIconBuilder() {}

            public AnimatedIconBuilder iconForTime(IIcon icon, int time) {
                timedIcons.add(Pair.of(icon, time));
                return this;
            }

            public AnimatedIconBuilder itemIconForTime(Item item, int time) {
                timedIcons.add(Pair.of(new ItemIcon(item), time));
                return this;
            }

            public AnimatedIconBuilder keyIconForTime(IKey key, int time) {
                timedIcons.add(Pair.of(IconUtils.of(key.getValue()), time));
                return this;
            }

            public AnimatedIconBuilder keyIconForTime(int key, int time) {
                timedIcons.add(Pair.of(IconUtils.of(key), time));
                return this;
            }

            public AnimatedIconBuilder keyIconForTime(String key, int time) {
                timedIcons.add(Pair.of(IconUtils.of(key), time));
                return this;
            }

            public AnimatedIconBuilder keyTexIconForTime(ResourceLocation keyTexture, int time) {
                timedIcons.add(Pair.of(IconUtils.of(keyTexture), time));
                return this;
            }

            public AnimatedIcon build() {
                return new AnimatedIcon(timedIcons);
            }
        }
    }

    public static class ConditionFactories {
        public static NotCondition not(IContextCondition condition) {
            return new NotCondition(condition);
        }

        public static AndCondition and(IContextCondition... conditions) {
            return new AndCondition(List.of(conditions));
        }

        public static OrCondition or(IContextCondition... conditions) {
            return new OrCondition(List.of(conditions));
        }

        public static PlayerHeldItemCondition heldItem(ItemContextPredicate predicate, @Nullable InteractionHand hand) {
            return new PlayerHeldItemCondition(predicate, Optional.ofNullable(hand));
        }

        public static PlayerHeldItemCondition heldItem(ItemContextPredicate predicate) {
            return heldItem(predicate, null);
        }

        public static PlayerHeldKeyCondition heldKey(IKey key) {
            return new PlayerHeldKeyCondition(key);
        }

        public static PlayerHeldKeyCondition heldKey(int key) {
            return new PlayerHeldKeyCondition(new CodeKey(key));
        }

        public static PlayerHeldKeyCondition heldKey(String key) {
            return new PlayerHeldKeyCondition(new MappingKey(key));
        }

        public static TargetHeldItemCondition targetHeldItem(ItemContextPredicate predicate, @Nullable InteractionHand hand) {
            return new TargetHeldItemCondition(predicate, Optional.ofNullable(hand));
        }

        public static TargetHeldItemCondition targetHeldItem(ItemContextPredicate predicate) {
            return targetHeldItem(predicate, null);
        }

        public static VillagerProfessionCondition villagerProfession(VillagerProfession profession) {
            return new VillagerProfessionCondition(profession);
        }
    }
}
