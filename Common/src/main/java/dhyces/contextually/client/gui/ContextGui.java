package dhyces.contextually.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.client.core.conditions.ContextSource;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.gui.screens.ContextScreen;
import dhyces.contextually.mixins.client.GuiAccessor;
import dhyces.contextually.services.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ContextGui {

    public static final ContextGui INSTANCE = new ContextGui();

    ContextGui() {}

    public void render(Gui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft.getInstance().getProfiler().popPush("context_renderer");
        // If a screen is open, we don't want to render. Maybe there should be screen contexts?
        if ((((GuiAccessor)gui).getClient().screen != null && !(((GuiAccessor)gui).getClient().screen instanceof ContextScreen)) || ((GuiAccessor)gui).getClient().gameMode.getPlayerMode().equals(GameType.SPECTATOR)) {
            return;
        }
        Services.PLATFORM.setupRenderState(gui);

        var mc = ((GuiAccessor)gui).getClient();
        var clientPlayer = mc.player;
        var clientLevel = mc.level;

        ContextSource source = ContextSource.of(gui, mc, clientPlayer, partialTick);

        // Gather contexts
        Set<ContextRenderHolder<?, ?>> contextSet = new LinkedHashSet<>();

        //  -Global contexts
        if (!ContextuallyClient.getContextManager().getGlobalContexts().isEmpty()) {
            var contexts = ContextuallyClient.getContextManager().filterGlobalContexts(source);
            // TODO: add an event
            contextSet.add(new ContextRenderHolder<>(null, contexts));
        }

        //  -HitResult (entity and solid block) contexts
        var hitResult = mc.hitResult;
        if (hitResult != null && !hitResult.getType().equals(HitResult.Type.MISS)) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                source.with(ContextSource.SourceAccess.ENTITY, entityHitResult);
                var entity = entityHitResult.getEntity();
                var contexts = ContextuallyClient.getContextManager().filterContextsForEntity(source);
                // TODO: add an event
                contextSet.add(new ContextRenderHolder<>(entity, contexts));
            } else if (hitResult instanceof BlockHitResult blockHitResult) {
                source.with(ContextSource.SourceAccess.BLOCK, blockHitResult);
                var block = clientLevel.getBlockState(blockHitResult.getBlockPos());
                var contexts = ContextuallyClient.getContextManager().filterContextsForBlock(source);
                // TODO: add an event
                contextSet.add(new ContextRenderHolder<>(block, contexts));
            }
        }

        //  -Fluid HitResult contexts
        var fluidHitResult = fluidPass(clientPlayer, partialTick);
        if (!fluidHitResult.getType().equals(HitResult.Type.MISS) && fluidHitResult instanceof BlockHitResult fluidResult) {
            source.with(ContextSource.SourceAccess.FLUID, fluidResult);
            var fluid = clientLevel.getFluidState(fluidResult.getBlockPos());
            if (!fluid.isEmpty()) {
                var contexts = ContextuallyClient.getContextManager().filterContextsForFluid(source);
                // TODO: add an event
                contextSet.add(new ContextRenderHolder<>(fluid, contexts));
            }
        }

        //  -Item contexts
        // TODO: add an event
        var offhand = clientPlayer.getOffhandItem();
        var mainhand = clientPlayer.getMainHandItem();
        if (!mainhand.isEmpty()) {
            var mainContexts = ContextuallyClient.getContextManager().filterContextsForItem(source, InteractionHand.MAIN_HAND);
            if (!mainContexts.isEmpty()) {
                contextSet.add(new ContextRenderHolder<>(mainhand, mainContexts));
            }
        }
        if (!offhand.isEmpty()) {
            var offContexts = ContextuallyClient.getContextManager().filterContextsForItem(source, InteractionHand.OFF_HAND);
            if (!offContexts.isEmpty()) {
                contextSet.add(new ContextRenderHolder<>(offhand, offContexts));
            }
        }

        var heightPos = height - 16 - 32;
        var half = contextSet.stream().mapToInt(c -> c.contextCollection.size()).sum() / 2;
        var count = 0;
        // TODO: instead of doing this, we should maybe just collect the renderer as a Runnable and then
        // Render contexts
        for (ContextRenderHolder<?, ?> holder : contextSet) {
            var context = holder.contextObject();
            for (IKeyContext<?, ?> keyContext : holder.contextCollection()) {
                var xPos = count < half || mc.options.showSubtitles().get() ? IKeyContext.PADDING : width - keyContext.width(gui.getFont()) - IKeyContext.PADDING;
                keyContext.renderIcons(cast(context), gui, poseStack, partialTick, xPos, heightPos, width, height);
                heightPos = count != half-1 || mc.options.showSubtitles().get() ? heightPos - (16 + IKeyContext.SMALL_PADDING) : height - 16 - 32;
                count++;
            }
        }
        heightPos = height - 16 - 32;
        count = 0;
        for (ContextRenderHolder<?, ?> holder : contextSet) {
            var context = holder.contextObject();
            for (IKeyContext<?, ?> keyContext : holder.contextCollection()) {
                var xPos = count < half || mc.options.showSubtitles().get() ? IKeyContext.PADDING : width - keyContext.width(gui.getFont()) - IKeyContext.PADDING;
                keyContext.renderText(cast(context), gui, poseStack, partialTick, xPos, heightPos + 4, width, height);
                heightPos = count != half-1 || mc.options.showSubtitles().get() ? heightPos - (16 + IKeyContext.SMALL_PADDING) : height - 16 - 32;
                count++;
            }
        }
    }

    // We should know that the type matches
    @SuppressWarnings("unchecked")
    private <X> X cast(Object o) {
        return (X)o;
    }

    private HitResult fluidPass(AbstractClientPlayer pPlayer, float partialTick) {
        return pPlayer.pick(Services.PLATFORM.getReachDistance(pPlayer), partialTick, true);
    }

    record ContextRenderHolder<K, T>(T contextObject, Collection<IKeyContext<K, T>> contextCollection) {}
}
