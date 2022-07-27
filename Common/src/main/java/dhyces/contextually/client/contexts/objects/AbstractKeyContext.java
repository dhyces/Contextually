package dhyces.contextually.client.contexts.objects;

import com.google.common.base.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.icons.IIcon;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class AbstractKeyContext<T> implements IKeyContext<T> {

    final ResourceLocation id;
    final Set<IIcon> icons;
    final Set<IConditionPredicate> conditions;
    final Component text;

    private int widthCache = 0;
    private boolean invalid = true;

    public AbstractKeyContext(@NotNull ResourceLocation id, @NotNull Set<IIcon> icons) {
        this(id, icons, Set.of());
    }

    public AbstractKeyContext(@NotNull ResourceLocation id, @NotNull Set<IIcon> icons, @NotNull Set<IConditionPredicate> conditions) {
        this.id = id;
        this.icons = icons;
        this.conditions = conditions;
        this.text = Component.translatable(id.getNamespace() + "." + id.getPath().replace('/', '.'));
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
        if (invalid && widthCache == 0) {
            var numKeys = icons.size();
            var numPlus = numKeys - 1;
            var keySize = numKeys * 16;
            var plusWidth = font.width(PLUS);
            var plusSize = numPlus * plusWidth;
            widthCache = keySize + plusSize + (SMALL_PADDING * (numPlus * 2)) + SMALL_PADDING + font.width(this.text);
            this.invalid = false;
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

    @NotNull
    @Override
    public ResourceLocation getID() {
        return id;
    }

    @NotNull
    @Override
    public Set<IConditionPredicate> getConditions() {
        return conditions;
    }

    @NotNull
    @Override
    public Set<IIcon> getIcons() {
        return icons;
    }

    @Override
    public void invalidateCache() {
        this.invalid = true;
        this.widthCache = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractKeyContext<?> that = (AbstractKeyContext<?>) o;
        return Objects.equal(icons, that.icons);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(icons);
    }
}
