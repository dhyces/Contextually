package dhyces.contextually.client.core.conditions.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.conditions.predicates.ItemContextPredicate;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public record TargetHeldItemCondition(ItemContextPredicate predicate, Optional<InteractionHand> hand) implements IContextCondition {
    public static final Codec<TargetHeldItemCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemContextPredicate.CODEC.fieldOf("item_predicate").forGetter(TargetHeldItemCondition::predicate),
                    Codec.optionalField("hand", MoreCodecs.INTERACTION_HAND).forGetter(TargetHeldItemCondition::hand)
            ).apply(instance, TargetHeldItemCondition::new)
    );

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof LivingEntity livingEntity) {
            if (hand().isEmpty() || hand.get().equals(InteractionHand.MAIN_HAND)) {
                var handItem = livingEntity.getMainHandItem();
                if (!predicate.matches(handItem)) {
                    return false;
                }
            }
            if (hand().isEmpty() || hand.get().equals(InteractionHand.OFF_HAND)) {
                var handItem = livingEntity.getOffhandItem();
                if (!predicate.matches(handItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.TARGET_HELD_ITEM;
    }
}
