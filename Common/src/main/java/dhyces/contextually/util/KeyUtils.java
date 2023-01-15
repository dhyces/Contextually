package dhyces.contextually.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.mixins.client.InputConstantsTypeAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

public class KeyUtils {
    private static final Map<String, KeyMapping> KEYMAPPING_BY_STRING = Maps.uniqueIndex(Arrays.asList(Minecraft.getInstance().options.keyMappings), KeyMapping::getName);

    public static KeyMapping get(String mappingName) {
        return KEYMAPPING_BY_STRING.get(mappingName);
    }

    @Nonnull
    public static InputConstants.Key getKey(int value) {
        InputConstants.Key key = getMap(InputConstants.Type.KEYSYM).get(value);
        if (key == null) {
            key = getMap(InputConstants.Type.MOUSE).get(value);
            if (key == null) {
                key = getMap(InputConstants.Type.SCANCODE).get(value);
                if (key == null) {
                    return InputConstants.UNKNOWN;
                }
            }
        }
        return key;
    }

    private static Int2ObjectMap<InputConstants.Key> getMap(InputConstants.Type type) {
        return ((InputConstantsTypeAccessor)(Object)type).getMap();
    }
}