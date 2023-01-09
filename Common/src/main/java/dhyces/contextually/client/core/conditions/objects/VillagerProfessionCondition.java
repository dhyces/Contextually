package dhyces.contextually.client.core.conditions.objects;

import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.phys.HitResult;

public record VillagerProfessionCondition(VillagerProfession profession) implements IContextCondition {
    public static final Codec<VillagerProfessionCondition> CODEC = BuiltInRegistries.VILLAGER_PROFESSION.byNameCodec().fieldOf("profession").xmap(
            VillagerProfessionCondition::new,
            VillagerProfessionCondition::profession
    ).codec();

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof Villager villager) {
            return profession.equals(villager.getVillagerData().getProfession());
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.VILLAGER_PROFESSION;
    }
}
