package dhyces.contextually.client.gui.widgets;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;

public abstract class DoubleWidget extends GuiComponent implements Renderable, IGuiListener, NarratableEntry {

    double x;
    double y;
    double width;
    double height;
    boolean visible = true;
    boolean active = true;
    boolean focused;

    public DoubleWidget(double pX, double pY, double pWidth, double pHeight) {
        this.x = pX;
        this.y = pY;
        this.width = pWidth;
        this.height = pHeight;
    }

    @Override
    public boolean onFocusChange(boolean onFocus) {
        if (active && visible) {
            this.focused = !this.focused;
            return focused;
        }
        return false;
    }

    @Override
    public boolean check(double mouseX, double mouseY) {
        return active && visible && mouseX >= x && mouseX < (x + width) && mouseY >= y && mouseY < (y + height);
    }
}
