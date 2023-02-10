package dhyces.contextually.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.client.gui.ContextGui;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Inject(method = "render", at = @At("TAIL"))
    private void contextually$afterHudRender(PoseStack poseStack, float partialTick, CallbackInfo ci) {
        Gui thiz = (Gui)(Object)this;
        ContextGui.INSTANCE.render(thiz, poseStack, partialTick, screenWidth, screenHeight);
    }
}
