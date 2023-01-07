package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public record ItemIcon(ResourceLocation itemLocation) implements IIcon {
    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        var item = BuiltInRegistries.ITEM.get(itemLocation);
        Minecraft.getInstance().getItemRenderer().renderGuiItem(item.getDefaultInstance(), x, y);
        RenderSystem.setShaderTexture(0, KeyMappingTextureManager.KEYS);
    }

    @Override
    public ResourceLocation getId() {
        return Contextually.id("item");
    }
}
