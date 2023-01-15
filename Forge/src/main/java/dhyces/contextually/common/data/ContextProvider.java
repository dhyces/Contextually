package dhyces.contextually.common.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.conditions.objects.*;
import dhyces.contextually.client.core.conditions.predicates.ItemContextPredicate;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.icons.IIcon;
import dhyces.contextually.util.IconUtils;
import dhyces.contextually.client.core.icons.objects.AnimatedIcon;
import dhyces.contextually.client.core.icons.objects.ItemIcon;
import dhyces.contextually.client.core.icons.objects.KeyIcon;
import dhyces.contextually.client.core.icons.objects.KeyTextureIcon;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.keys.MappingKey;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class ContextProvider implements DataProvider {

    PackOutput.PathProvider pathProvider;
    ExistingFileHelper fileHelper;
    String modid;
    final String contextPath = "contexts";
    final Map<ResourceLocation, IKeyContext<?, ?>> data = Maps.newHashMap();

    public ContextProvider(@NotNull PackOutput output, @NotNull ExistingFileHelper fileHelper, @NotNull String modid) {
        Preconditions.checkNotNull(output);
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, contextPath);
        Preconditions.checkNotNull(fileHelper);
        this.fileHelper = fileHelper;
        Preconditions.checkNotNull(modid);
        this.modid = modid;
    }

    protected abstract void addContexts(BiConsumer<String, IKeyContext<?, ?>> exporter);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addContexts((s, context) -> data.put(new ResourceLocation(modid, s), context));

        return CompletableFuture.allOf(data.entrySet().stream().map(
                        entry -> {
                            IKeyContext<?, ?> context = entry.getValue();
                            JsonElement element = IKeyContext.CODEC.encodeStart(JsonOps.INSTANCE, context).getOrThrow(false, Contextually.LOGGER::error);
                            Path path = this.pathProvider.json(entry.getKey());
                            return DataProvider.saveStable(cache, element, path);
                        }
                ).toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public @NotNull String getName() {
        return "Contextually: Context Provider for " + modid;
    }

    public static class ContextBuilder<K, V, T extends IKeyContext<K, V>> {
        private final Set<IIcon> iconSet = new HashSet<>();
        private final Set<IContextCondition> conditionSet = new HashSet<>();
        private final Set<K> targetSet = new HashSet<>();
        private final ContextFactory<K, V, T> factory;

        private ContextBuilder(ContextFactory<K, V, T> factory) {
            this.factory = factory;
        }

        public static <K, V, T extends IKeyContext<K, V>> ContextBuilder<K, V, T> create(ContextFactory<K, V, T> factory) {
            return new ContextBuilder<>(factory);
        }

        public ContextBuilder<K, V, T> addIcon(IIcon icon) {
            iconSet.add(icon);
            return this;
        }

        public ContextBuilder<K, V, T> addCondition(IContextCondition condition) {
            conditionSet.add(condition);
            return this;
        }

        public ContextBuilder<K, V, T> addTarget(K target) {
            targetSet.add(target);
            return this;
        }

        public ContextBuilder<K, V, T> addTargets(List<K> targets) {
            targetSet.addAll(targets);
            return this;
        }

        public void export(String id, BiConsumer<String, IKeyContext<?, ?>> consumer) {
            var built = factory.create(iconSet, conditionSet, targetSet);
            consumer.accept(id, built);
        }

        public interface ContextFactory<K, V, T extends IKeyContext<K, V>> {
            T create(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions, @NotNull Set<K> targetedStates);
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
