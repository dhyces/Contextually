package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ITextureIcon extends IIcon {
    TextureAtlasSprite retrieveTexture();

    @Override
    default void render(PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        GuiComponent.blit(poseStack, x, y, blitOffset, 16, 16, retrieveTexture());
    }
}
