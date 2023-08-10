package dhyces.contextually.client.gui;

public record IntCoord(int x, int y) {
    public boolean isWithin(int minX, int maxX, int minY, int maxY) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }
}
