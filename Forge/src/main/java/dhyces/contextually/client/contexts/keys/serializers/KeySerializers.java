package dhyces.contextually.client.contexts.keys.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.keys.Key;
import net.minecraft.resources.ResourceLocation;

public class KeySerializers {

    public static final IKeySerializer MAPPING_KEY_SERIALIZER = new IKeySerializer() {
        @Override
        public Key deserialize(JsonElement json) {
            return Key.of(json.getAsString());
        }

        @Override
        public JsonElement serialize(Key key) {
            if (!key.isMapping())
                throw new IllegalArgumentException("Key: " + key + " is not an instance of a key mapping Key.");
            return new JsonPrimitive(key.getKeyInfo());
        }
    };

    public static final IKeySerializer KEYCODE_KEY_SERIALIZER = new IKeySerializer() {
        @Override
        public Key deserialize(JsonElement json) {
            return Key.of(json.getAsInt());
        }

        @Override
        public JsonElement serialize(Key key) {
            //TODO: do later
            return new JsonPrimitive(key.getKeyInfo());
        }
    };

    public static final IKeySerializer TEXTURE_KEY_SERIALIZER = new IKeySerializer() {
        @Override
        public Key deserialize(JsonElement json) {
            return Key.of(ContextuallyCommon.modDefaultingloc(json.getAsString()));
        }

        @Override
        public JsonElement serialize(Key key) {
            if (key.isMapping())
                throw new IllegalArgumentException("Key: " + key + " is not an instance of a key texture.");
            return new JsonPrimitive(key.getKeyInfo());
        }
    };
}
