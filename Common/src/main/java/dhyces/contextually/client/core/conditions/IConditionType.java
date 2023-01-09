package dhyces.contextually.client.core.conditions;

import com.mojang.serialization.Codec;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.INamed;
import dhyces.contextually.client.core.KeyContextLoader;
import dhyces.contextually.client.core.conditions.objects.*;
import net.minecraft.resources.ResourceLocation;

public interface IConditionType<T extends IContextCondition> extends INamed {
    IConditionType<AndCondition> AND = registerType("and", AndCondition.CODEC);
    IConditionType<NotCondition> NOT = registerType("not", NotCondition.CODEC);
    IConditionType<OrCondition> OR = registerType("or", OrCondition.CODEC);
    IConditionType<PlayerHeldItemCondition> HELD_ITEM = registerType("held_item", PlayerHeldItemCondition.CODEC);
    IConditionType<PlayerHeldKeyCondition> HELD_KEY = registerType("held_key", PlayerHeldKeyCondition.CODEC);
    IConditionType<TargetHeldItemCondition> TARGET_HELD_ITEM = registerType("target_held_item", TargetHeldItemCondition.CODEC);
    IConditionType<VillagerProfessionCondition> VILLAGER_PROFESSION = registerType("villager_profession", VillagerProfessionCondition.CODEC);

    Codec<T> getCodec();

    private static <T extends IContextCondition> IConditionType<T> registerType(String id, Codec<T> codec) {
        return KeyContextLoader.registerConditionType(Contextually.id(id), codec);
    }

    static <T extends IContextCondition> IConditionType<T> create(ResourceLocation location, Codec<T> codec) {
        return new IConditionType<T>() {
            @Override
            public Codec<T> getCodec() {
                return codec;
            }

            @Override
            public ResourceLocation getId() {
                return location;
            }
        };
    }
}
