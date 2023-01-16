package dhyces.contextually.client.core.conditions.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.ContextSource;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public record OrCondition(List<IContextCondition> conditions) implements IContextCondition {
    public static final Codec<OrCondition> CODEC = IContextCondition.CODEC.listOf().fieldOf("values").xmap(OrCondition::new, OrCondition::conditions).codec();

    @Override
    public boolean test(ContextSource contextSource) {
        return conditions.stream().anyMatch(condition -> condition.test(contextSource));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.OR;
    }
}
