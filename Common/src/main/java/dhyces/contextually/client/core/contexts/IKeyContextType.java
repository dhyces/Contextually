package dhyces.contextually.client.core.contexts;

import com.mojang.serialization.Codec;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.INamed;
import dhyces.contextually.client.core.KeyContextLoader;
import dhyces.contextually.client.core.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.core.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.core.contexts.objects.GlobalKeyContext;
import dhyces.contextually.client.core.contexts.objects.ItemKeyContext;
import net.minecraft.resources.ResourceLocation;

public interface IKeyContextType<T extends IKeyContext<?, ?>> extends INamed {
    IKeyContextType<BlockKeyContext> BLOCK = registerType("block_context", BlockKeyContext.CODEC);
    IKeyContextType<ItemKeyContext> ITEM = registerType("item_context", ItemKeyContext.CODEC);
    IKeyContextType<EntityKeyContext> ENTITY = registerType("entity_context", EntityKeyContext.CODEC);
    IKeyContextType<GlobalKeyContext> GLOBAL = registerType("global_hud_context", GlobalKeyContext.CODEC);

    Codec<T> getCodec();

    private static <T extends IKeyContext<?, ?>> IKeyContextType<T> registerType(String id, Codec<T> codec) {
        return KeyContextLoader.registerKeyContextType(Contextually.id(id), codec);
    }

    static <T extends IKeyContext<?, ?>> IKeyContextType<T> create(ResourceLocation location, Codec<T> codec) {
        return new IKeyContextType<T>() {
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
