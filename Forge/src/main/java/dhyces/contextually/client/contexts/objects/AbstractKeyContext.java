package dhyces.contextually.client.contexts.objects;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.client.contexts.conditions.Condition;
import dhyces.contextually.client.contexts.keys.Key;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class AbstractKeyContext<T> implements IKeyContext<T> {

    final ResourceLocation id;
    final Set<Key> keys;
    final Set<Condition> conditions;
    final Component text;

    private int widthCache = -1;

    public AbstractKeyContext(@NotNull ResourceLocation id, @NotNull Set<Key> keys) {
        this(id, keys, Set.of());
    }

    public AbstractKeyContext(@NotNull ResourceLocation id, @NotNull Set<Key> keys, @NotNull Set<Condition> conditions) {
        this.id = id;
        this.keys = keys;
        this.conditions = conditions;
        this.text = Component.translatable("contextually.contexts." + id.getPath());
    }



    @Override
    public void renderIcons(T contextObject, ForgeGui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        if (pX < 0) pX = 0;
        var font = gui.getFont();
        var contextWidth = width(font);
        var pixelsToMove = (pX + contextWidth) - width;
        if (pixelsToMove > 0) {
            pX -= pixelsToMove + PADDING;
        }
        renderKeys(gui, poseStack, partialTicks, pX, pY, width, height);
        //renderText(gui, font, poseStack, partialTicks, pX, pY + 4, width, height);
    }

    @Override
    public int width(Font font) {
        if (widthCache == -1) {
            var numKeys = keys.size();
            var numPlus = numKeys - 1;
            var keySize = numKeys * 16;
            var plusWidth = font.width(PLUS);
            var plusSize = numPlus * plusWidth;
            widthCache = keySize + plusSize + (SMALL_PADDING * (numPlus * 2)) + SMALL_PADDING + font.width(this.text);
        }
        return widthCache;
    }


    protected void renderKeys(ForgeGui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        var plusCount = keys.size() - 1;
        for (Key key : keys) {
            gui.blit(poseStack, pX, pY, gui.getBlitOffset(), 16, 16, ContextuallyClient.getTextureManager().get(key.resolveTextureLocation()));
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
    public void renderText(T contextObject, ForgeGui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        renderText(gui, gui.getFont(), poseStack, partialTicks, pX, pY, width, height);
    }

    protected void renderText(ForgeGui gui, Font font, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height) {
        var plusCount = keys.size() - 1;
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

    @NotNull
    @Override
    public ResourceLocation getID() {
        return id;
    }

    @NotNull
    @Override
    public Set<Condition> getConditions() {
        return conditions;
    }

    @NotNull
    @Override
    public Set<Key> getKeys() {
        return keys;
    }

    @Override
    public void invalidateCache() {
        this.widthCache = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractKeyContext<?> that = (AbstractKeyContext<?>) o;
        return Objects.equal(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(keys);
    }
}
