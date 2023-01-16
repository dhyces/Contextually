package dhyces.contextually.client.core.conditions;

import dhyces.contextually.services.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Optional;

public class ContextSource {
    // TODO: This class is to house all of the information regarding contexts. Now, it would be very easy to just slap
    //  hitresult info in here, the player's held items, and a slot, but. Not very extensible. So I propose a map system
    //  that utilizes some instance fields held elsewhere to access the targeted info. So you would have something like
    //  a Map<SourceAccess, Object> which would then be cast to the appropriate type.

    private Gui gui;
    private final Minecraft client;
    private AbstractClientPlayer player;
    private float partialTick;
    @Nullable
    private Slot slot = null;

    public ContextSource(Gui gui, Minecraft minecraft, AbstractClientPlayer player, float partialTick) {
        this.gui = gui;
        this.client = minecraft;
        this.player = player;
        this.partialTick = partialTick;
    }

    public static ContextSource of(Gui gui, Minecraft minecraft, AbstractClientPlayer player, float partialTick, @Nullable Slot slot) {
        ContextSource ret = new ContextSource(gui, minecraft, player, partialTick);
        ret.slot = slot;
        return ret;
    }

    public Gui getGui() {
        return gui;
    }

    public Minecraft getClient() {
        return client;
    }

    public ClientLevel getLevel() {
        return client.level;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    public Optional<HitResult> getHitResult() {
        return Optional.ofNullable(client.hitResult);
    }

    public Optional<BlockState> getHitResultSolidBlock() {
        if (client.hitResult instanceof BlockHitResult blockHitResult && !client.hitResult.getType().equals(HitResult.Type.MISS)) {
            BlockState state = getLevel().getBlockState(blockHitResult.getBlockPos());
            return Optional.of(state);
        }
        return Optional.empty();
    }

    public Optional<BlockState> getHitResultFluidBlock() {
        HitResult fluidPass = player.pick(Services.PLATFORM.getReachDistance(player), partialTick, true);
        if (fluidPass instanceof BlockHitResult blockHitResult && !fluidPass.getType().equals(HitResult.Type.MISS)) {
            BlockState state = getLevel().getBlockState(blockHitResult.getBlockPos());
            return Optional.of(state);
        }
        return Optional.empty();
    }

    public Optional<Entity> getHitResultEntity() {
        if (client.hitResult instanceof EntityHitResult entityHitResult && !client.hitResult.getType().equals(HitResult.Type.MISS)) {
            return Optional.of(entityHitResult.getEntity());
        }
        return Optional.empty();
    }

    public Optional<ItemStack> getHandStack(InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return Optional.ofNullable(stack.isEmpty() ? null : stack);
    }


    public Optional<Slot> getHoveredSlot() {
        return Optional.ofNullable(slot);
    }
}
