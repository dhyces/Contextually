package dhyces.contextually.client.contexts.keys;

import com.google.common.collect.Maps;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.textures.KeyConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

//TODO: make this class looser. Very tightly coupled with KeyConstants for a data class
public class Key {

    private static final Map<String, Key> BY_MAPPING = Maps.newHashMap();
    private static final Map<String, Key> BY_TEXTURE = Maps.newHashMap();

    final String keyInfo;
    final boolean isMapping;

    Key(String keyInfo, boolean isMapping) {
        Objects.requireNonNull(keyInfo);
        this.keyInfo = keyInfo;
        this.isMapping = isMapping;
    }

    public String getKeyInfo() {
        return keyInfo;
    }

    public boolean isMapping() {
        return isMapping;
    }

    public ResourceLocation resolveTextureLocation() {
        if (isMapping) {
            return KeyConstants.get(KeyConstants.getKeyMapping(keyInfo).getKey().getValue());
        }
        return ContextuallyCommon.modDefaultingloc(keyInfo);
    }

    public Optional<KeyMapping> getMapping() {
        if (isMapping) {
            return Optional.of(KeyConstants.getKeyMapping(keyInfo));
        }
        return Optional.empty();
    }

    public static Key of(int keyCode) {
        return of(KeyConstants.get(keyCode));
    }

    public static Key of(ResourceLocation texturePath) {
        var pathStr = texturePath.toString();
        var ret = BY_TEXTURE.get(pathStr);
        if (ret == null) {
            ret = new Key(pathStr, false);
            BY_TEXTURE.put(pathStr, ret);
        }
        return ret;
    }

    public static Key of(String mappingPath) {
        var ret = BY_MAPPING.get(mappingPath);
        if (ret == null) {
            ret = new Key(mappingPath, true);
            BY_MAPPING.put(mappingPath, ret);
        }
        return ret;
    }
}
