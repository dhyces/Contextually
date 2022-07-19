package dhyces.contextually.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ContextGuiOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        gui.setupOverlayRenderState(true, false, KeyMappingTextureManager.KEYS);
        var mc = gui.getMinecraft();
        var clientPlayer = mc.player;
        var hitResult = mc.hitResult;
        if (hitResult instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {
            var key = ContextuallyClient.getKeyTextureManager().get(ContextuallyCommon.modloc("a_key"));
            GuiComponent.blit(poseStack, width/2, height/2, gui.getBlitOffset(), 8, 8, key);
        } else if (hitResult instanceof EntityHitResult entityHitResult && !entityHitResult.getType().equals(HitResult.Type.MISS)) {

        }
    }
}
