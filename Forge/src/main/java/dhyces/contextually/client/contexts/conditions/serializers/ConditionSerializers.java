package dhyces.contextually.client.contexts.conditions.serializers;

import dhyces.contextually.client.contexts.conditions.objects.*;

public class ConditionSerializers {

    // TODO: potentially a SaddleableEntityCondition?
    // TODO: potentially a SaddledEntityCondition?
    public static final IConditionSerializer<TargetEntityNbtCondition> TARGET_ENTITY_NBT_SERIALIZER = new TargetEntityNbtCondition.Serializer();
    public static final IConditionSerializer<TargetHeldItemNbtCondition> TARGET_HELD_NBT_SERIALIZER = new TargetHeldItemNbtCondition.Serializer();
    public static final IConditionSerializer<PlayerHeldItemCondition> PLAYER_HELD_ITEM_SERIALIZER = new PlayerHeldItemCondition.Serializer();
    public static final IConditionSerializer<PlayerHeldBlockCondition> PLAYER_HELD_BLOCK_SERIALIZER = new PlayerHeldBlockCondition.Serializer();
    public static final IConditionSerializer<PlayerKeyHeldCondition> PLAYER_KEY_HELD_SERIALIZER = new PlayerKeyHeldCondition.Serializer();
    public static final IConditionSerializer<VillagerProfessionCondition> VILLAGER_PROFESSION_SERIALIZER = new VillagerProfessionCondition.Serializer();
    public static final IConditionSerializer<NotCondition> NOT_SERIALIZER = new NotCondition.Serializer();
    public static final IConditionSerializer<AndCondition> AND_SERIALIZER = new AndCondition.Serializer();
    public static final IConditionSerializer<OrCondition> OR_SERIALIZER = new OrCondition.Serializer();
}
