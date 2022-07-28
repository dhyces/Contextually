package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.util.JsonHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;

public record TargetEntityNbtCondition(CompoundTag testTag) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.id("target_entity_nbt");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof Entity entity) {
            var entityTag = new CompoundTag();
            entity.save(entityTag);
            return compareTag(testTag, entityTag);
        }
        return false;
    }

    boolean compareTag(CompoundTag test, CompoundTag target) {
        for (String key : test.getAllKeys()) {
            if (target.contains(key)) {
                var testTag = test.get(key);
                var targetTag = target.get(key);
                if (testTag instanceof CompoundTag testCompound && targetTag instanceof CompoundTag targetCompound) {
                    if (!compareTag(testCompound, targetCompound)) {
                        return false;
                    }
                } else {
                    if (!testTag.equals(targetTag)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<TargetEntityNbtCondition> {

        @Override
        public TargetEntityNbtCondition deserialize(JsonObject json) {
            var nbt = JsonHelper.readNbt(json.get("nbt"));
            return new TargetEntityNbtCondition(nbt);
        }

        @Override
        public JsonObject serialize(TargetEntityNbtCondition context) {
            var base = createBaseConditionJson();
            base.add("nbt", new JsonPrimitive(context.testTag.toString()));
            return base;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
