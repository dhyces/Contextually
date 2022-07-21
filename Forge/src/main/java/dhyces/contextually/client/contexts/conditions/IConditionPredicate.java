package dhyces.contextually.client.contexts.conditions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;

@FunctionalInterface
public interface IConditionPredicate {
    boolean test(Object target, ClientLevel level, AbstractClientPlayer player);
}
