package dhyces.contextually.client.contexts.conditions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public abstract class Condition implements IConditionPredicate {

    public static class TargetHeldItemNbtCondition extends Condition {

        final CompoundTag tag;
        @Nullable final InteractionHand hand;

        public TargetHeldItemNbtCondition(CompoundTag tag, @Nullable InteractionHand hand) {
            this.tag = tag;
            this.hand = hand;
        }

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
    }

    public static class TargetHeldItemCondition extends Condition {

        final Item item;
        @Nullable final InteractionHand hand;

        public TargetHeldItemCondition(Item item, @Nullable InteractionHand hand) {
            this.item = item;
            this.hand = hand;
        }

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
    }

    public static class PlayerHeldItemNbtCondition extends Condition {

        final CompoundTag tag;
        @Nullable final InteractionHand hand;

        public PlayerHeldItemNbtCondition(CompoundTag tag, @Nullable InteractionHand hand) {
            this.tag = tag;
            this.hand = hand;
        }

        @Override
        public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
            if (hand == null) {
                return player.getMainHandItem().getTag().equals(tag) || player.getOffhandItem().getTag().equals(tag);
            }
            return player.getItemInHand(hand).getTag().equals(tag);
        }
    }

    public static class PlayerHeldItemCondition extends Condition {

        final Item item;
        @Nullable final InteractionHand hand;

        public PlayerHeldItemCondition(Item item, @Nullable InteractionHand hand) {
            this.item = item;
            this.hand = hand;
        }

        @Override
        public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
            if (hand == null) {
                return player.getMainHandItem().is(item) || player.getOffhandItem().is(item);
            }
            return player.getItemInHand(hand).is(item);
        }
    }

    public static class PlayerHeldBlockCondition extends Condition {

        @Nullable final InteractionHand hand;

        public PlayerHeldBlockCondition(@Nullable InteractionHand hand) {
            this.hand = hand;
        }

        @Override
        public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
            if (hand == null) {
                return player.getMainHandItem().getItem() instanceof BlockItem || player.getOffhandItem().getItem() instanceof BlockItem;
            }
            return player.getItemInHand(hand).getItem() instanceof BlockItem;
        }
    }

    public static class EntityNbtCondition extends Condition {

        final CompoundTag testTag;

        public EntityNbtCondition(CompoundTag testTag) {
            this.testTag = testTag;
        }

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
    }

    public static class VillagerProfessionCondition extends Condition {

        final String profession;

        public VillagerProfessionCondition(String profession) {
            this.profession = profession;
        }

        @Override
        public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
            if (target instanceof Villager villager) {
                return profession.equals(villager.getVillagerData().getProfession().name());
            }
            return false;
        }
    }

    public static class NotCondition extends Condition {

        final Condition condition;

        public NotCondition(Condition condition) {
            this.condition = condition;
        }

        @Override
        public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
            return !condition.test(target, level, player);
        }
    }
}
