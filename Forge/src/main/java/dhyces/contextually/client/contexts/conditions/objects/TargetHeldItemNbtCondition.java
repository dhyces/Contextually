package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public record TargetHeldItemNbtCondition(CompoundTag tag, @Nullable InteractionHand hand) implements IConditionPredicate {

    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof LivingEntity livingEntity) {
            if (hand == null) {
                return livingEntity.getMainHandItem().getTag().equals(tag) || livingEntity.getOffhandItem().getTag().equals(tag);
            }
            return livingEntity.getItemInHand(hand).getTag().equals(tag);
        }
        return false;
    }

    public static class Serializer implements IConditionSerializer<TargetHeldItemNbtCondition> {

        final ResourceLocation id = ContextuallyCommon.modloc("target_held_nbt");

        @Override
        public TargetHeldItemNbtCondition deserialize(JsonObject json) {
            var nbt = CraftingHelper.getNBT(json.get("nbt"));
            var hand = getHand(json.get("hand"));
            return new TargetHeldItemNbtCondition(nbt, hand);
        }

        @Override
        public JsonObject serialize(TargetHeldItemNbtCondition context) {
            // TODO
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
