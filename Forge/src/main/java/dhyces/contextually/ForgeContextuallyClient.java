package dhyces.contextually;

import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.gui.ContextGuiOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeContextuallyClient {

    static void init(IEventBus bus) {
        bus.addListener(ForgeContextuallyClient::registerReloadables);
        bus.addListener(ForgeContextuallyClient::registerGuis);
    }

    private static void registerReloadables(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ContextuallyClient.getTextureManager());
        event.registerReloadListener(ContextuallyClient.getContextManager());
    }

    private static void registerGuis(RegisterGuiOverlaysEvent event) {
        event.registerAbove(new ResourceLocation("item_name"), "key_contexts", ContextGuiOverlay.INSTANCE);
    }
}
