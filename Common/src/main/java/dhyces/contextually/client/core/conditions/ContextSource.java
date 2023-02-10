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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ContextSource {

    protected Map<SourceAccess<?>, Object> sourceMap = new HashMap<>();

    private Gui gui;
    private final Minecraft client;
    private AbstractClientPlayer player;
    private float partialTick;

    private ContextSource(Gui gui, Minecraft minecraft, AbstractClientPlayer player, float partialTick) {
        this.gui = gui;
        this.client = minecraft;
        this.player = player;
        this.partialTick = partialTick;
    }

    public static ContextSource of(Gui gui, Minecraft minecraft, AbstractClientPlayer player, float partialTick) {
        return new ContextSource(gui, minecraft, player, partialTick);
    }

    public <T> ContextSource with(SourceAccess<T> sourceAccess, T source) {
        sourceMap.put(sourceAccess, source);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAccess(SourceAccess<T> sourceAccess) {
        return Optional.ofNullable((T)sourceMap.get(sourceAccess));
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

    public static class SourceAccess<T> {
        public static final SourceAccess<BlockHitResult> BLOCK = new SourceAccess<>(BlockHitResult.class);
        public static final SourceAccess<BlockHitResult> FLUID = new SourceAccess<>(BlockHitResult.class);
        public static final SourceAccess<EntityHitResult> ENTITY = new SourceAccess<>(EntityHitResult.class);
        public static final SourceAccess<Slot> SLOT = new SourceAccess<>(Slot.class);

        Class<T> clazz;
        private SourceAccess(Class<T> clazz) {
            this.clazz = clazz;
        }
    }
}
