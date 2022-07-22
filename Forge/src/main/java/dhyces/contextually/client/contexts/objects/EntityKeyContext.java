package dhyces.contextually.client.contexts.objects;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.client.contexts.conditions.Condition;
import dhyces.contextually.client.contexts.keys.Key;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class EntityKeyContext extends AbstractKeyContext<Entity> {

    public EntityKeyContext(@NotNull ResourceLocation id, @NotNull Set<Key> keys, @NotNull Set<Condition> conditions) {
        super(id, keys, conditions);
    }

    public static class Serializer implements IContextSerializer<Entity, EntityKeyContext> {

        @Override
        public Pair<Collection<Entity>, EntityKeyContext> deserialize(ResourceLocation id, JsonObject json) {
            return null;
        }

        @Override
        public JsonObject serialize(@NotNull ResourceLocation id, @NotNull Pair<Collection<Entity>, EntityKeyContext> context) {
            return null;
        }
    }
}
