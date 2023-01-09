package dhyces.contextually.client.core.conditions.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public record AndCondition(List<IContextCondition> conditions) implements IContextCondition {
    public static final Codec<AndCondition> CODEC = IContextCondition.CODEC.listOf().fieldOf("values").codec().xmap(AndCondition::new, AndCondition::conditions);

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return conditions.stream().allMatch(iContextCondition -> iContextCondition.test(target, pos, level, player));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.AND;
    }
}
