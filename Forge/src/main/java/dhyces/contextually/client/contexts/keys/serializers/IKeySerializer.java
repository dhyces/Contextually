package dhyces.contextually.client.contexts.keys.serializers;

import com.google.gson.JsonElement;
import dhyces.contextually.client.contexts.keys.Key;

public interface IKeySerializer {
    Key deserialize(JsonElement json);

    JsonElement serialize(Key key);
}
