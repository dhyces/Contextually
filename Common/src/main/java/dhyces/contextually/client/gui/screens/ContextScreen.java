package dhyces.contextually.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dhyces.contextually.client.gui.widgets.ContextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class ContextScreen extends Screen {

    public static final ContextScreen INSTANCE = new ContextScreen(Component.literal("bruh"));
    final ContextWidget uh = new ContextWidget(0, 0, 60, 60);

    protected ContextScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        //TODO: create a screen
//        for (RenderRect rect : ContextuallyClient.RENDER_RECTS) {
//            addRenderableWidget(new ContextWidget(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
//        }
        addRenderableWidget(this.uh);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        var tesselator = Tesselator.getInstance();
        var builder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(8.0F);
        builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

        builder.vertex(0.5, 0, 0).color(0xFFFFFFFF).normal(0, 1, 0).endVertex();
        builder.vertex(0.5, this.height, 0).color(0xFFFFFFFF).normal(0, 1, 0).endVertex();

        builder.vertex(0, this.height, 0).color(0xFFFFFFFF).normal(1, 0, 0).endVertex();
        builder.vertex(this.width+2, this.height, 0).color(0xFFFFFFFF).normal(1, 0, 0).endVertex();

        builder.vertex(this.width+1.5, 0, 0).color(0xFFFFFFFF).normal(0, 1, 0).endVertex();
        builder.vertex(this.width+1.5, this.height, 0).color(0xFFFFFFFF).normal(0, 1, 0).endVertex();

        tesselator.end();
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}