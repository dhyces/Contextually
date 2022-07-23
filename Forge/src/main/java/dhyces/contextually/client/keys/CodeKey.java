package dhyces.contextually.client.keys;

public record CodeKey(int keyCode) implements IKey {
    @Override
    public int getValue() {
        return keyCode;
    }
}
