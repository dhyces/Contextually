package dhyces.contextually.client.contexts.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.Condition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EntityKeyContext extends AbstractKeyContext<Entity> {

    public EntityKeyContext(@NotNull ResourceLocation id, @NotNull Set<ResourceLocation> keys, @NotNull Set<Condition> conditions) {
        super(id, keys, conditions);
    }

    public static class Serializer implements ISerializer<Entity, EntityKeyContext> {

        @Override
        public EntityKeyContext deserialize(ResourceLocation id, JsonObject json) {
            return null;
        }

        @Override
        public JsonObject serialize(ResourceLocation id, EntityKeyContext context) {
            return null;
        }
    }
}
