package dhyces.contextually.client.contexts.icons.serializers;

import com.google.gson.JsonElement;
import dhyces.contextually.client.contexts.icons.IIcon;

public interface IIconSerializer<T extends IIcon> {
    T deserialize(JsonElement json);

    JsonElement serialize(T key);
}
