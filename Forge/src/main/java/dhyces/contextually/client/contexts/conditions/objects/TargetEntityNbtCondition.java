package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.AbstractConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.crafting.CraftingHelper;

public record TargetEntityNbtCondition(CompoundTag testTag) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof Entity entity) {
            var entityTag = entity.serializeNBT();
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

    public static class Serializer extends AbstractConditionSerializer<TargetEntityNbtCondition> {

        public Serializer() {
            super("target_entity_nbt");
        }

        @Override
        public TargetEntityNbtCondition deserialize(JsonObject json) {
            var nbt = CraftingHelper.getNBT(json.get("nbt"));
            return new TargetEntityNbtCondition(nbt);
        }

        @Override
        public JsonObject serialize(TargetEntityNbtCondition context) {
            //TODO
            return null;
        }
    }
}
