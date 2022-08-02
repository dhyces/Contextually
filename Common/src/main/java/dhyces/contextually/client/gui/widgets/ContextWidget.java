package dhyces.contextually.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class ContextWidget extends DoubleWidget {

    public ContextWidget(double pX, double pY, double pWidth, double pHeight) {
        super(pX, pY, pWidth, pHeight);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        var tesselator = Tesselator.getInstance();
        var builder = tesselator.getBuilder();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(x, y, 0).color(0xAA555555).endVertex();
        builder.vertex(x, y+height, 0).color(0xAA555555).endVertex();
        builder.vertex(x+width, y+height, 0).color(0xAA555555).endVertex();
        builder.vertex(x+width, y, 0).color(0xAA555555).endVertex();
        tesselator.end();
        RenderSystem.enableTexture();
    }

    @Override
    public void onDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (validateButton(button)) {
            this.x += dragX;
            this.y += dragY;
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    static class VisibilityWidget extends GuiComponent implements Widget, GuiEventListener {

        private static final ResourceLocation CHECKBOX_LOCATION = new ResourceLocation("textures/gui/checkbox.png");

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShaderTexture(0, CHECKBOX_LOCATION);

        }
    }
}
