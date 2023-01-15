package dhyces.contextually.client.core.conditions.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public record OrCondition(List<IContextCondition> conditions) implements IContextCondition {
    public static final Codec<OrCondition> CODEC = IContextCondition.CODEC.listOf().fieldOf("values").xmap(OrCondition::new, OrCondition::conditions).codec();

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return conditions.stream().anyMatch(iContextCondition -> iContextCondition.test(target, pos, level, player));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.OR;
    }
}
