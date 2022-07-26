package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface IPlatformHelper {

    public InputConstants.Key getKey(KeyMapping keyMapping);
}
