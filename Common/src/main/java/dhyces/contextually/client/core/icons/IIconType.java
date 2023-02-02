package dhyces.contextually.client.core.icons;

import com.mojang.serialization.Codec;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.INamed;
import dhyces.contextually.client.core.KeyContextLoader;
import dhyces.contextually.client.core.icons.objects.*;
import net.minecraft.resources.ResourceLocation;

public interface IIconType<T extends IIcon> extends INamed {
    IIconType<AnimatedIcon> ANIMATED = register("animated", AnimatedIcon.CODEC);
    IIconType<ItemIcon> ITEM = register("item", ItemIcon.CODEC);
    IIconType<KeyIcon> KEY = register("key", KeyIcon.CODEC);
    IIconType<KeyTextureIcon> KEY_TEXTURE = register("key_texture", KeyTextureIcon.CODEC);
    IIconType<TextureAtlasIcon> TEXTURE_ATLAS = register("texture", TextureAtlasIcon.CODEC);

    Codec<T> getCodec();

    private static <T extends IIcon> IIconType<T> register(String id, Codec<T> codec) {
        return KeyContextLoader.registerIconType(Contextually.id(id), codec);
    }

    static <T extends IIcon> IIconType<T> create(ResourceLocation location, Codec<T> codec) {
        return new IIconType<T>() {
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
