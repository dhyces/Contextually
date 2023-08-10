package dhyces.contextually;

import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.gui.ContextGui;
import dhyces.contextually.mixins.client.GuiAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
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
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedListener(Contextually.id("key_textures"), ContextuallyClient::getTextureManager));
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new WrappedListener(Contextually.id("contexts"), ContextuallyClient::getContextManager));
        ClientLifecycleEvents.CLIENT_STARTED.register(this::clientStartTick); // onInitializeClient is called too early for setting up texture atlases
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            GuiAccessor accessor = ((GuiAccessor) Minecraft.getInstance().gui);
            ContextGui.INSTANCE.render(Minecraft.getInstance().gui, matrixStack, tickDelta, accessor.getScreenWidth(), accessor.getScreenHeight());
        });
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
        public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
            return listener.get().reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
        }
    }
}
