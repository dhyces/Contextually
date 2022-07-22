package dhyces.contextually.client.contexts.objects.serializers;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.Condition;
import dhyces.contextually.client.contexts.keys.Key;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public interface IContextSerializer<E, T extends IKeyContext<?>> {

    Pair<Collection<E>, T> deserialize(@NotNull ResourceLocation id, @NotNull JsonObject json);

    JsonObject serialize(@NotNull ResourceLocation id, @NotNull Pair<Collection<E>, T> context);

    default Set<Condition> readConditions(@Nullable JsonArray conditionArray) {
        ImmutableSet.Builder<Condition> builder = ImmutableSet.builder();
        if (conditionArray != null) {
            for (JsonElement o : conditionArray) {
                try {
                    builder.add(KeyContextLoader.deserializeCondition(o.getAsJsonObject()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.build();
    }

    default Set<Key> readKeys(@NotNull JsonArray keyArray) {
        Objects.requireNonNull(keyArray);
        ImmutableSet.Builder<Key> builder = ImmutableSet.builder();
        for (JsonElement o : keyArray) {
            try {
                builder.add(KeyContextLoader.deserializeKey(o.getAsJsonObject()));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

    default boolean isDefault(JsonElement e) {
        if (e.isJsonPrimitive()) {
            return e.getAsString().equals("*");
        }
        return false;
    }
}
