package dhyces.contextually.common;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.gui.screens.ContextScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Contextually.MOD_ID)
public class Events {

    @SubscribeEvent
    static void test(TickEvent.ClientTickEvent e) {
        if (e.phase.equals(TickEvent.Phase.START) && Minecraft.getInstance().screen == null) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LALT)) {
                Minecraft.getInstance().mouseHandler.releaseMouse();
            } else {
                Minecraft.getInstance().mouseHandler.grabMouse();
            }
        }
    }

    @SubscribeEvent
    static void test(InputEvent.MouseButton.Pre e) {
        if (Minecraft.getInstance().screen == null && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LALT)) {
            e.setCanceled(true);
        }
    }

    //TODO: create a decent screen to move contexts around
//    @SubscribeEvent
//    static void test(InputEvent.Key e) {
//        if (Minecraft.getInstance().screen == null && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_P)) {
//            Minecraft.getInstance().setScreen(ContextScreen.INSTANCE);
//        }
//    }
}
