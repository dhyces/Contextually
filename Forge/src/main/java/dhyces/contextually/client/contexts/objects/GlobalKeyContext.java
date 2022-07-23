package dhyces.contextually.client.contexts.objects;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.icons.IIcon;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class GlobalKeyContext extends AbstractKeyContext<Player> {

    public GlobalKeyContext(@NotNull ResourceLocation id, @NotNull Set<IIcon> icons, @NotNull Set<IConditionPredicate> conditions) {
        super(id, icons, conditions);
    }

    public static class Serializer implements IContextSerializer<Player, GlobalKeyContext> {
        @Override
        public Pair<Collection<Player>, GlobalKeyContext> deserialize(ResourceLocation id, JsonObject json) {
            var keys = readIcons(json.getAsJsonArray("icons"));
            var conditions = readConditions(json.getAsJsonArray("conditions"));
            return Pair.of(null, new GlobalKeyContext(id, keys, conditions));
        }

        @Override
        public JsonObject serialize(@NotNull ResourceLocation id, @NotNull Pair<Collection<Player>, GlobalKeyContext> context) {
            return null;
        }
    }
}
