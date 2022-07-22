package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.AbstractConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public record TargetHeldItemCondition(Item item, @Nullable InteractionHand hand) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof LivingEntity livingEntity) {
            if (hand == null) {
                return livingEntity.getMainHandItem().is(item) || livingEntity.getOffhandItem().is(item);
            }
            return livingEntity.getItemInHand(hand).is(item);
        }
        return false;
    }

    public static class Serializer extends AbstractConditionSerializer<TargetHeldItemCondition> {

        public Serializer() {
            super("target_entity_nbt");
        }

        @Override
        public TargetHeldItemCondition deserialize(JsonObject json) {
            var item = CraftingHelper.getItem(json.get("id").getAsString(), true);
            var hand = getHand(json.get("hand"));
            return new TargetHeldItemCondition(item, hand);
        }

        @Override
        public JsonObject serialize(TargetHeldItemCondition context) {
            //TODO
            return null;
        }
    }
}
