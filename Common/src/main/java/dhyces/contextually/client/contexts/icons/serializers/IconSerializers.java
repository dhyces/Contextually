package dhyces.contextually.client.contexts.icons.serializers;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.icons.*;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.MappingKey;
import dhyces.contextually.util.IntPair;
import net.minecraft.resources.ResourceLocation;

public class IconSerializers {

    public static final IIconSerializer<KeyIcon> KEY_SERIALIZER = new IIconSerializer<>() {
        @Override
        public KeyIcon deserialize(JsonObject json) {
            if (json.get("key") instanceof JsonPrimitive primitive) {
                if (primitive.isString()) {
                    return IconUtils.of(primitive.getAsString());
                } else {
                    return IconUtils.of(primitive.getAsInt());
                }
            }
            throw new JsonSyntaxException("Expected key \"key\" to be either a string or an integer");
        }

        @Override
        public JsonObject serialize(JsonObject jsonObject, KeyIcon icon) {
            if (icon.key() instanceof CodeKey codeKey) {
                jsonObject.add("key", new JsonPrimitive(codeKey.keyCode()));
            } else if (icon.key() instanceof MappingKey mappingKey) {
                jsonObject.add("key", new JsonPrimitive(mappingKey.mapping().getName()));
            }
            return jsonObject;
        }
    };

    public static final IIconSerializer<KeyTextureIcon> KEY_TEXTURE_SERIALIZER = new IIconSerializer<>() {
        @Override
        public KeyTextureIcon deserialize(JsonObject json) {
            return IconUtils.of(ContextuallyCommon.defaultingId(json.get("key_texture").getAsString()));
        }

        @Override
        public JsonObject serialize(JsonObject jsonObject, KeyTextureIcon icon) {
            jsonObject.add("key_texture", new JsonPrimitive(icon.texture().toString()));
            return jsonObject;
        }
    };

    public static final IIconSerializer<ItemIcon> ITEM_SERIALIZER = new IIconSerializer<>() {
        @Override
        public ItemIcon deserialize(JsonObject json) {
            return new ItemIcon(ResourceLocation.of(json.get("item").getAsString(), ':'));
        }

        @Override
        public JsonObject serialize(JsonObject jsonObject, ItemIcon icon) {
            jsonObject.add("item", new JsonPrimitive(icon.itemLocation().toString()));
            return jsonObject;
        }
    };

    public static final IIconSerializer<AnimatedIcon> ANIMATED_SERIALIZER = new IIconSerializer<>() {
        @Override
        public AnimatedIcon deserialize(JsonObject json) {
            var array = json.getAsJsonArray("animated");
            ImmutableList.Builder<IntPair<IIcon>> builder = ImmutableList.builder();
            for (JsonElement e : array) {
                if (e instanceof JsonObject jsonObject) {
                    builder.add(parseObject(jsonObject));
                    continue;
                }
                throw new IllegalStateException("Unexpected value: " + e);
            }
            return new AnimatedIcon(builder.build());
        }

        private IntPair<IIcon> parseObject(JsonObject jsonObject) {
            IIcon icon = KeyContextLoader.deserializeIcon(jsonObject.getAsJsonObject("icon"));
            var ticks = jsonObject.get("ticks").getAsInt();
            return new IntPair<>(icon, ticks);
        }

        @Override
        public JsonObject serialize(JsonObject jsonObject, AnimatedIcon icon) {
            for (IntPair<IIcon> pair : icon.getIcons()) {
                jsonObject.add("icon", KeyContextLoader.serializeIcon(pair.object()));
                jsonObject.add("ticks", new JsonPrimitive(pair.value()));
            }
            return jsonObject;
        }
    };
}
