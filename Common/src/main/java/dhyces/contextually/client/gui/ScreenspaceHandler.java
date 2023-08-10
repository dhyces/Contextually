package dhyces.contextually.client.gui;

public interface ScreenspaceHandler extends ScreenspaceElement {

    default boolean canMoveTo(ScreenspaceElement element, IntCoord coord) {

        return coord.isWithin(getX(), getX()+getWidth(), getY(), getY()+getHeight()) && element.getX()+element.getWidth() <= getX()+getWidth() && element.getY()+element.getHeight() <= getY()+getHeight();
    }
}
