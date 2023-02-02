package dhyces.contextually.client.core.icons.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.core.icons.ITextureIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public record TextureAtlasIcon(ResourceLocation textureAtlasLocation, ResourceLocation spriteLocation, int color) implements ITextureIcon {
    public static final Codec<TextureAtlasIcon> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("texture_atlas").forGetter(TextureAtlasIcon::textureAtlasLocation),
                    ResourceLocation.CODEC.fieldOf("sprite").forGetter(TextureAtlasIcon::spriteLocation),
                    Codec.INT.fieldOf("color").xmap(
                            integer -> ((integer >> 24) & 0xFF) == 0 ? integer | 0xFF000000 : integer,
                            integer -> integer
                    ).forGetter(TextureAtlasIcon::color)
            ).apply(instance, TextureAtlasIcon::new)
    );

    @Override
    public IIconType<?> getType() {
        return IIconType.TEXTURE_ATLAS;
    }

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.setShaderColor(r, g, b, a);
        RenderSystem.setShaderTexture(0, textureAtlasLocation);
        ITextureIcon.super.render(gui, poseStack, partialTicks, blitOffset, x, y, width, height);
    }

    @Override
    public TextureAtlasSprite retrieveTexture() {
        return Minecraft.getInstance().getTextureAtlas(textureAtlasLocation).apply(spriteLocation);
    }
}
