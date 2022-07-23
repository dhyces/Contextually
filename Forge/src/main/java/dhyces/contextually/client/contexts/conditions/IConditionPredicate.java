package dhyces.contextually.client.contexts.conditions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;

@FunctionalInterface
public interface IConditionPredicate {
    boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player);
}
