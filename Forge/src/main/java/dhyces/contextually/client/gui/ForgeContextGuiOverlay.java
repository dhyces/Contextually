package dhyces.contextually.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ForgeContextGuiOverlay extends ContextGui implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        INSTANCE.render(gui, poseStack, partialTick, screenWidth, screenHeight);
    }
}
