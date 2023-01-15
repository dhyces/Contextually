package dhyces.contextually;

import dhyces.contextually.client.ContextuallyClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class FabricContextuallyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //TODO: setup fabric platform. We will be using the HudRenderCallback event for rendering
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedListener(Contextually.id("key_textures"), ContextuallyClient::getTextureManager));
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedListener(Contextually.id("contexts"), ContextuallyClient::getContextManager));
        ClientLifecycleEvents.CLIENT_STARTED.register(this::clientStartTick); // onInitializeClient is called too early for setting up texture atlases. Rather that use lambdas, just delay it
    }

    private void clientStartTick(Minecraft client) {
        ContextuallyClient.init();
    }

    record WrappedListener(ResourceLocation id, Supplier<PreparableReloadListener> listener) implements IdentifiableResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return id;
        }

        @Override
        public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
            return listener.get().reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
        }
    }
}
