package dhyces.contextually.client.contexts.icons.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.icons.IIcon;

public interface IIconSerializer<T extends IIcon> {
    T deserialize(JsonObject json);

    JsonObject serialize(JsonObject jsonObject, T key);
}
