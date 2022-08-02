package dhyces.contextually.client.gui.widgets;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface IGuiListener extends GuiEventListener {
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (check(mouseX, mouseY)) {
            onClicked(mouseX, mouseY, button);
            return true;
        }
        return false;
    }

    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        onReleased(mouseX, mouseY, button);
        return true;
    }

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        onDragged(mouseX, mouseY, button, dragX, dragY);
        return true;
    }

    default boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        onScrolled(mouseX, mouseY, delta);
        return true;
    }

    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        onKeyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        onKeyReleased(keyCode, scanCode, modifiers);
        return true;
    }

    default boolean charTyped(char codePoint, int modifiers) {
        onCharTyped(codePoint, modifiers);
        return true;
    }

    default boolean changeFocus(boolean onFocus) {
        return onFocusChange(onFocus);
    }

    default boolean isMouseOver(double mouseX, double mouseY) {
        return check(mouseX, mouseY);
    }

    default boolean validateButton(int button) {
        return button == 0;
    }

    default void onClicked(double mouseX, double mouseY, int button) {}

    default void onReleased(double mouseX, double mouseY, int button) {}

    default void onDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {}

    default void onScrolled(double mouseX, double mouseY, double delta) {}

    default void onKeyPressed(int keyCode, int scanCode, int modifiers) {}

    default void onKeyReleased(int keyCode, int scanCode, int modifiers) {}

    default void onCharTyped(char codePoint, int modifiers) {}

    boolean onFocusChange(boolean onFocus);

    boolean check(double mouseX, double mouseY);
}
