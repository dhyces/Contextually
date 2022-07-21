package dhyces.contextually.client.contexts.conditions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;

public abstract class Condition implements IConditionPredicate {

    public static class HeldItemNbtCondition extends Condition {

        final CompoundTag tag;
        final InteractionHand hand;

        public HeldItemNbtCondition(CompoundTag tag, InteractionHand hand) {
            this.tag = tag;
            this.hand = hand;
        }

        @Override
        public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
            return player.getItemInHand(hand).getTag().equals(tag);
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
}
