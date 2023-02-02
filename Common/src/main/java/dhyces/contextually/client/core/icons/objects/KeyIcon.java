package dhyces.contextually.client.core.icons.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.core.icons.ITextureIcon;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record KeyIcon(IKey key) implements ITextureIcon {
    public static final Codec<KeyIcon> CODEC = MoreCodecs.KEY_CODEC.fieldOf("key").xmap(
            KeyIcon::new,
            KeyIcon::key
            ).codec();

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, KeyMappingTextureManager.KEYS);
        ITextureIcon.super.render(gui, poseStack, partialTicks, blitOffset, x, y, width, height);
    }

    @Override
    public TextureAtlasSprite retrieveTexture() {
        return ContextuallyClient.getTextureManager().get(key.getValue());
    }

    @Override
    public IIconType<?> getType() {
        return IIconType.KEY;
    }
}