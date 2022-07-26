package dhyces.contextually.mixins.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public class KeyMappingMixin implements IKeyMappingMixin {

    @Shadow
    private InputConstants.Key key;

    @Override
    public InputConstants.Key getKey() {
        return key;
    }
}
