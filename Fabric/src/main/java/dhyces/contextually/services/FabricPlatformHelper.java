package dhyces.contextually.services;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import dhyces.contextually.mixins.client.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return ((KeyMappingAccessor)keyMapping).getKey();
    }

    @Override
    public double getReachDistance(Player player) {
        return ReachEntityAttributes.getReachDistance(player, 4.5);
    }

    @Override
    public void setupRenderState(Gui gui) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.setShaderTexture(0, KeyMappingTextureManager.KEYS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }
}
