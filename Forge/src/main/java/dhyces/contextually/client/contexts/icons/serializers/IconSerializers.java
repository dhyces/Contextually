package dhyces.contextually.client.contexts.icons.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.icons.*;
import net.minecraft.resources.ResourceLocation;

public class IconSerializers {

    public static final IIconSerializer<KeyMappingIcon> KEY_MAPPING_SERIALIZER = new IIconSerializer<>() {
        @Override
        public KeyMappingIcon deserialize(JsonElement json) {
            return IconUtils.of(json.getAsString());
        }

        @Override
        public JsonElement serialize(KeyMappingIcon icon) {
            return new JsonPrimitive(icon.mapping().saveString());
        }
    };

    public static final IIconSerializer<KeyCodeIcon> KEYCODE_SERIALIZER = new IIconSerializer<>() {
        @Override
        public KeyCodeIcon deserialize(JsonElement json) {
            return IconUtils.of(json.getAsInt());
        }

        @Override
        public JsonElement serialize(KeyCodeIcon icon) {
            return new JsonPrimitive(icon.value());
        }
    };

    public static final IIconSerializer<KeyTextureIcon> KEY_TEXTURE_SERIALIZER = new IIconSerializer<>() {
        @Override
        public KeyTextureIcon deserialize(JsonElement json) {
            return IconUtils.of(ContextuallyCommon.modDefaultingloc(json.getAsString()));
        }

        @Override
        public JsonElement serialize(KeyTextureIcon icon) {
            return new JsonPrimitive(icon.texture().toString());
        }
    };

    public static final IIconSerializer<ItemIcon> ITEM_SERIALIZER = new IIconSerializer<>() {
        @Override
        public ItemIcon deserialize(JsonElement json) {
            return new ItemIcon(ResourceLocation.of(json.getAsString(), ':'));
        }

        @Override
        public JsonElement serialize(ItemIcon icon) {
            return new JsonPrimitive(icon.itemLocation().toString());
        }
    };
}
