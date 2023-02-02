package dhyces.contextually.client.core.icons.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.core.icons.ITextureIcon;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record KeyTextureIcon(ResourceLocation texture) implements ITextureIcon {
    public static final Codec<KeyTextureIcon> CODEC = ResourceLocation.CODEC.fieldOf("key_texture").xmap(KeyTextureIcon::new, KeyTextureIcon::texture).codec();

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, KeyMappingTextureManager.KEYS);
        ITextureIcon.super.render(gui, poseStack, partialTicks, blitOffset, x, y, width, height);
    }

    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(texture);
    }

    @Override
    public IIconType<?> getType() {
        return IIconType.KEY_TEXTURE;
    }
}
