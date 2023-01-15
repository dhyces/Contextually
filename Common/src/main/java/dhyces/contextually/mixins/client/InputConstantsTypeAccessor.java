package dhyces.contextually.mixins.client;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InputConstants.Type.class)
public interface InputConstantsTypeAccessor {

    @Accessor
    Int2ObjectMap<InputConstants.Key> getMap();
}