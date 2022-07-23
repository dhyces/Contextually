package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IIcon {
    void render(PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height);
    String getId();
}
