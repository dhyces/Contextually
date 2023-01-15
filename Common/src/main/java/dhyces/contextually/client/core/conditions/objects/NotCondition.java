package dhyces.contextually.client.core.conditions.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;

public record NotCondition(IContextCondition condition) implements IContextCondition {
    public static final Codec<NotCondition> CODEC = IContextCondition.CODEC.fieldOf("value").xmap(NotCondition::new, NotCondition::condition).codec();

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return !condition.test(target, pos, level, player);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.NOT;
    }
}
