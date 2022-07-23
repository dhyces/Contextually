package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;


public record VillagerProfessionCondition(VillagerProfession profession) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.modloc("villager_profession");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof Villager villager) {
            return profession.equals(villager.getVillagerData().getProfession());
        }
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<VillagerProfessionCondition> {
        @Override
        public VillagerProfessionCondition deserialize(JsonObject json) {
            var professionJson = json.get("profession");
            var professionKey = ResourceLocation.of(professionJson.getAsString(), ':');
            var profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(professionKey);
            if (!ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession).equals(professionKey)) {
                throw new IllegalStateException("Profession not found for: " + professionKey);
            }
            return new VillagerProfessionCondition(profession);
        }

        @Override
        public JsonObject serialize(VillagerProfessionCondition context) {
            var base = createBaseConditionJson();
            base.add("profession", new JsonPrimitive(ForgeRegistries.VILLAGER_PROFESSIONS.getKey(context.profession).toString()));
            return base;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
