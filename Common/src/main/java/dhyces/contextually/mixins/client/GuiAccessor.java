package dhyces.contextually.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiAccessor {

    @Accessor("minecraft")
    Minecraft getClient();

    @Accessor
    int getScreenWidth();

    @Accessor
    int getScreenHeight();
}
