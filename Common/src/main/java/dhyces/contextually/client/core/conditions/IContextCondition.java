package dhyces.contextually.client.core.conditions;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.KeyContextLoader;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;

public interface IContextCondition {
    Codec<IContextCondition> CODEC = KeyContextLoader.CONDITION_MAP_CODEC.dispatch("condition_type", IContextCondition::getType, IConditionType::getCodec);

    boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player);

    IConditionType<?> getType();
}
