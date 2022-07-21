package dhyces.contextually;

import dhyces.contextually.client.contexts.KeyContextManager;
import dhyces.contextually.client.gui.ContextGuiOverlay;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.List;

public class ContextuallyClient {
    static KeyMappingTextureManager keyTextures;
    static KeyContextManager keyContexts;

    ContextuallyClient() {
    }

    static void init(IEventBus bus) {
        bus.addListener(ContextuallyClient::registerReloadables);
        bus.addListener(ContextuallyClient::registerGuis);
    }

    private static void registerReloadables(RegisterClientReloadListenersEvent event) {
        keyTextures = new KeyMappingTextureManager(Minecraft.getInstance().textureManager);
        keyContexts = new KeyContextManager();
        event.registerReloadListener(keyTextures);
    }

    private static void registerGuis(RegisterGuiOverlaysEvent event) {
        event.registerAbove(new ResourceLocation("item_name"), "key_contexts", new ContextGuiOverlay());
    }

    public static KeyMappingTextureManager getTextureManager() {
        return keyTextures;
    }

    public static KeyContextManager getContextManager() {
        return keyContexts;
    }

    public List<ResourceLocation> collectResources() {
        return List.of();
    }
}
