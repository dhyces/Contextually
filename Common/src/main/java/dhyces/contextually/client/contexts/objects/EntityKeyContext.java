package dhyces.contextually.client.contexts.objects;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.icons.IIcon;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class EntityKeyContext extends AbstractKeyContext<Entity> {

    public EntityKeyContext(@NotNull ResourceLocation id, @NotNull Set<IIcon> icons, @NotNull Set<IConditionPredicate> conditions) {
        super(id, icons, conditions);
    }

    public static class Serializer implements IContextSerializer<EntityType<?>, EntityKeyContext> {

        @Override
        public Pair<Collection<EntityType<?>>, EntityKeyContext> deserialize(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            var targetJson = json.get("target_entity");
            ImmutableSet.Builder<EntityType<?>> builder = null;
            if (targetJson.isJsonPrimitive()) {
                builder = ImmutableSet.builder();
                if (!isDefault(targetJson)) {
                    var entityType = getEntityType(targetJson);
                    builder.add(entityType);
                }
            } else if (targetJson.isJsonArray()) {
                builder = ImmutableSet.builder();
                for (JsonElement e : targetJson.getAsJsonArray()) {
                    KeyContextLoader.checkParse(e != null, "Element null in array. Likely unterminated array.");
                    builder.add(getEntityType(e));
                }
            }

            KeyContextLoader.checkParse(builder != null, "Key \"target_entity\" not present.");

            var keys = readIcons(json);
            var conditions = readConditions(json);
            return Pair.of(builder.build(), new EntityKeyContext(id, keys, conditions));
        }

        private EntityType<?> getEntityType(JsonElement element) {
            var key = ResourceLocation.of(element.getAsString(), ':');
            var type = Registry.ENTITY_TYPE.get(key);
            KeyContextLoader.checkParse(Registry.ENTITY_TYPE.getKey(type).equals(key), "EntityType for given key: " + key + " not found.");
            return type;
        }

        @Override
        public JsonObject serialize(@NotNull ResourceLocation id, @NotNull Pair<Collection<EntityType<?>>, EntityKeyContext> context) {
            return null;
        }
    }
}
