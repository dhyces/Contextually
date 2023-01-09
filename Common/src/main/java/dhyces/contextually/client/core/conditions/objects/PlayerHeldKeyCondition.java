package dhyces.contextually.client.core.conditions.objects;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;

public record PlayerHeldKeyCondition(IKey key) implements IContextCondition {
    public static final Codec<PlayerHeldKeyCondition> CODEC = IKey.CODEC.fieldOf("key").xmap(PlayerHeldKeyCondition::new, PlayerHeldKeyCondition::key).codec();

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.HELD_KEY;
    }
}
