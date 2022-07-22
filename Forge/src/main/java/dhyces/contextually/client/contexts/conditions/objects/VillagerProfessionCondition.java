package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;

import static dhyces.contextually.ContextuallyCommon.modloc;

public record VillagerProfessionCondition(ResourceLocation profession) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof Villager villager) {
            return profession.equals(ResourceLocation.of(villager.getVillagerData().getProfession().name(), ':'));
        }
        return false;
    }

    public static class Serializer implements IConditionSerializer<VillagerProfessionCondition> {
        @Override
        public VillagerProfessionCondition deserialize(JsonObject json) {
            var professionJson = json.get("profession");
            return new VillagerProfessionCondition(ResourceLocation.of(professionJson.getAsString(), ':'));
        }

        @Override
        public JsonObject serialize(VillagerProfessionCondition context) {
            // TODO
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return modloc("villager_profession");
        }
    }
}
