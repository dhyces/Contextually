package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.client.contexts.INamed;
import net.minecraft.client.gui.Gui;

public interface IIcon extends INamed {
    void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height);
}
