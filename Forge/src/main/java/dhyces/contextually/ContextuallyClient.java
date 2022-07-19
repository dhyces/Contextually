package dhyces.contextually;

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

    ContextuallyClient() {
    }

    static void init(IEventBus bus) {
        bus.addListener(ContextuallyClient::registerReloadables);
        bus.addListener(ContextuallyClient::registerGuis);
    }

    private static void registerReloadables(RegisterClientReloadListenersEvent event) {
        keyTextures = new KeyMappingTextureManager(Minecraft.getInstance().textureManager);
        event.registerReloadListener(keyTextures);
    }

    private static void registerGuis(RegisterGuiOverlaysEvent event) {
        event.registerAbove(new ResourceLocation("item_name"), "key_contexts", new ContextGuiOverlay());
    }

    public static KeyMappingTextureManager getKeyTextureManager() {
        return keyTextures;
    }

    public List<ResourceLocation> collectResources() {
        return List.of();
    }
}