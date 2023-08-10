package dhyces.contextually.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dhyces.contextually.client.gui.IRenderRect;
import dhyces.contextually.client.gui.Rectangle;
import dhyces.contextually.client.gui.ScreenspaceHandler;
import dhyces.contextually.client.gui.widgets.ContextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ContextScreen extends Screen implements ScreenspaceHandler {

    public static final ContextScreen INSTANCE = new ContextScreen(Component.literal("bruh"));
    final ContextWidget uh = new ContextWidget(this, 0, 0, 60, 60);
    private final ScreenspaceHandler screenspaceHandler = new ScreenspaceHandler() {
        IntSupplier screenWidth = () -> width;
        IntSupplier screenHeight = () -> height;

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }

        @Override
        public int getWidth() {
            return screenWidth.getAsInt();
        }

        @Override
        public int getHeight() {
            return screenHeight.getAsInt();
        }
    };
    private final GridController controller = new GridController(screenspaceHandler);

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
//        controller.effectiveArea.setWidth(width);
//        controller.effectiveArea.setHeight(height);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        var tesselator = Tesselator.getInstance();
        var builder = tesselator.getBuilder();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(0.0, 0, 0).color(0x44444444).normal(0, 1, 0).endVertex();
        builder.vertex(0.0, this.height, 0).color(0x44444444).normal(0, 1, 0).endVertex();
        builder.vertex(this.width, this.height, 0).color(0x44444444).normal(1, 0, 0).endVertex();
        builder.vertex(this.width, 0, 0).color(0x44444444).normal(1, 0, 0).endVertex();

        tesselator.end();

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

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
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        controller.reset();
        return super.mouseReleased($$0, $$1, $$2);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        controller.add(pDragX, pDragY);
        return controller.handle((aDouble, aDouble2) -> super.mouseDragged(pMouseX, pMouseY, pButton, aDouble, aDouble2));
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    static class GridController {
        int gridSize;
        ScreenspaceHandler effectiveArea;
        double draggedX;
        double draggedY;

        public GridController(ScreenspaceHandler effectiveArea) {
            this(effectiveArea, 10);
        }

        public GridController(ScreenspaceHandler effectiveArea, int gridSize) {
            this.effectiveArea = effectiveArea;
            this.gridSize = gridSize;
        }

        void add(double dragX, double dragY) {
            this.draggedX += dragX;
            this.draggedY += dragY;
        }

        void reset() {
            draggedX = 0;
            draggedY = 0;
        }

        boolean handle(BiFunction<Double, Double, Boolean> screenDrag) {
            double absDraggedX = Math.abs(draggedX);
            double absDraggedY = Math.abs(draggedY);
            double moveX = 0;
            double moveY = 0;
            if (absDraggedX >= gridSize) {
                moveX = draggedX < 0 ? -gridSize : gridSize;
                draggedX -= moveX;
            }
            if (absDraggedY >= gridSize) {
                moveY = draggedY < 0 ? -gridSize : gridSize;
                draggedY -= moveY;
            }
            return screenDrag.apply(moveX, moveY);
        }
    }
}