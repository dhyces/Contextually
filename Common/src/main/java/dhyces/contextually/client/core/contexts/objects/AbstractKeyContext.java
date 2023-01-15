package dhyces.contextually.client.core.contexts.objects;

import com.google.common.base.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.icons.IIcon;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractKeyContext<K, T> implements IKeyContext<K, T> {
    public static <T extends AbstractKeyContext<?, ?>> Products.P2<RecordCodecBuilder.Mu<T>, Set<IIcon>, Set<IContextCondition>> fillBaseParts(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                IIcon.CODEC.listOf().fieldOf("icons").xmap(Set::copyOf, List::copyOf).forGetter(AbstractKeyContext::getIcons),
                Codec.optionalField("conditions", IContextCondition.CODEC.listOf())
                        .xmap(
                                optional -> optional.map(Set::copyOf).orElse(Set.of()),
                                set -> Optional.ofNullable(set.isEmpty() ? null : List.copyOf(set))
                        )
                        .forGetter(AbstractKeyContext::getConditions)
        );
    }

    @Nullable
    private ResourceLocation id;
    private final Set<IIcon> icons;
    private final Set<IContextCondition> conditions;
    private final Set<K> targets;
    protected Component text;

    private int widthCache = 0;
    private boolean widthCacheInvalid = true;

    public AbstractKeyContext(@NotNull Set<IIcon> icons) {
        this(icons, Set.of());
    }

    public AbstractKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions) {
        this(icons, Set.of(), conditions, null);
    }

    public AbstractKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<K> targets, @NotNull Set<IContextCondition> conditions) {
        this(icons, targets, conditions, null);
    }

    public AbstractKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<K> targets, @NotNull Set<IContextCondition> conditions, @Nullable ResourceLocation id) {
        this.id = id;
        this.icons = icons;
        this.conditions = conditions;
        this.targets = targets;
        this.text = createTranslatable();
    }

    private Component createTranslatable() {
        return id == null ? Component.empty() : Component.translatable(id.getNamespace() + "." + id.getPath().replace('/', '.'));
    }

    @Override
    public void renderIcons(T contextObject, Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        if (pX < 0) pX = 0;
        var font = gui.getFont();
        var contextWidth = width(font);
        var pixelsToMove = (pX + contextWidth) - width;
        if (pixelsToMove > 0) {
            pX -= pixelsToMove + PADDING;
        }
        renderIcons(gui, poseStack, partialTicks, pX, pY, width, height);
    }

    @Override
    public int width(Font font) {
        if (widthCacheInvalid && widthCache == 0) {
            var numKeys = icons.size();
            var numPlus = numKeys - 1;
            var keySize = numKeys * 16;
            var plusWidth = font.width(PLUS);
            var plusSize = numPlus * plusWidth;
            widthCache = keySize + plusSize + (SMALL_PADDING * (numPlus * 2)) + SMALL_PADDING + font.width(this.text);
            this.widthCacheInvalid = false;
        }
        return widthCache;
    }


    protected void renderIcons(Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        var plusCount = icons.size() - 1;
        for (IIcon key : icons) {
            key.render(gui, poseStack, partialTicks, gui.getBlitOffset(), pX, pY, width, height);
            pX += 16;
            if (plusCount > 0) {
                pX += SMALL_PADDING;
                pX += gui.getFont().width("+") + SMALL_PADDING;
                plusCount--;
            }
        }
    }

    private int determineXPos(Font font, int pX, int width) {
        return width - width(font) - PADDING;
    }

    @Override
    public void renderText(T contextObject, Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        var font = gui.getFont();
        var plusCount = icons.size() - 1;
        pX += 16;
        while (plusCount > 0) {
            pX += SMALL_PADDING;
            font.drawShadow(poseStack, "+", pX, pY, 0xFFFFFFFF);
            pX += font.width("+") + SMALL_PADDING + 16;
            plusCount--;
        }
        pX += SMALL_PADDING;
        font.drawShadow(poseStack, text, pX, pY, 0xFFFFFFFF);
    }

    @Override
    public boolean testConditions(@Nullable Object contextObject, @Nullable HitResult hitResult, @NotNull ClientLevel level, @NotNull AbstractClientPlayer player) {
        return conditions.isEmpty() || conditions.stream().anyMatch(c -> c.test(contextObject, hitResult, level, player));
    }

    @Override
    public void setIdIfNull(@NotNull ResourceLocation resourceLocation) {
        if (this.id == null) {
            this.id = resourceLocation;
            text = createTranslatable();
        } else {
            throw new IllegalStateException("Context %s already has id".formatted(this.id));
        }
    }

    @Override
    @Nullable
    public ResourceLocation getID() {
        return id;
    }

    @NotNull
    @Override
    public Set<IContextCondition> getConditions() {
        return conditions;
    }

    @NotNull
    @Override
    public Set<IIcon> getIcons() {
        return icons;
    }

    @NotNull
    @Override
    public Set<K> getTargets() {
        return targets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractKeyContext<?, ?> that = (AbstractKeyContext<?, ?>) o;
        return Objects.equal(icons, that.icons);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(icons);
    }
}
