package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.keys.KeyUtils;
import dhyces.contextually.client.keys.MappingKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.HitResult;

public record PlayerKeyHeldCondition(IKey key) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.modloc("player_key_held");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<PlayerKeyHeldCondition> {

        @Override
        public PlayerKeyHeldCondition deserialize(JsonObject json) {
            var key = json.get("key").getAsJsonPrimitive();
            if (key.isString()) {
                return new PlayerKeyHeldCondition(new MappingKey(KeyUtils.get(key.getAsString())));
            }
            return new PlayerKeyHeldCondition(new CodeKey(key.getAsInt()));
        }

        @Override
        public JsonObject serialize(PlayerKeyHeldCondition context) {
            var base = createBaseConditionJson();
            if (context.key instanceof MappingKey mappingKey) {
                base.add("key", new JsonPrimitive(mappingKey.mapping().getName()));
            } else {
                base.add("key", new JsonPrimitive(context.key.getValue()));
            }
            return base;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
