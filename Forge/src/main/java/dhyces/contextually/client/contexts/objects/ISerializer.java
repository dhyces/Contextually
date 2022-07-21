package dhyces.contextually.client.contexts.objects;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface ISerializer<E, T extends IKeyContext<E>> {

    T deserialize(ResourceLocation id, JsonObject json);

    JsonObject serialize(ResourceLocation id, T context);
}
