package net.id.aether.blocks.blockentity.dungeon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.blocks.blockentity.AetherBlockEntityTypes;
import net.id.aether.blocks.dungeon.DungeonSwitchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class DungeonSwitchBlockEntity extends BlockEntity implements GameEventListener {
    private BlockPos linkedPos;
    private int animationDelta;

    public DungeonSwitchBlockEntity(BlockPos pos, BlockState state) {
//        this(AetherBlockEntityTypes.DUNGEON_SWITCH, pos, state);
        this(AetherBlockEntityTypes.FOOD_BOWL, pos, state); // temp
    }

    public DungeonSwitchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        //todo tweak range
    }

    public void setLinkedPos(BlockPos linkedPos) {
        this.linkedPos = linkedPos;
    }

    public BlockPos getLinkedPos() {
        return linkedPos;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (linkedPos != null)
            nbt.putLong("linkedpos", linkedPos.asLong());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("linkedpos"))
            linkedPos = BlockPos.fromLong(nbt.getLong("linkedpos"));
    }

    @Override
    public PositionSource getPositionSource() {
        return new BlockPositionSource(pos);
    }

    @Override
    public int getRange() {
        //todo tweak range
        return 10;
    }

    @Override
    public boolean listen(World world, GameEvent event, @Nullable Entity entity, BlockPos pos) {
        if (event == GameEvent.EXPLODE)
            if (world != null && world.getBlockState(this.getPos()).getBlock() instanceof DungeonSwitchBlock dungeonSwitchBlock) {
                dungeonSwitchBlock.onExplosionEvent(world, this.getPos());
            }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public void clientTick() {
        animationDelta++;
        if (animationDelta > 360)
            animationDelta = 1;
    }

    public int getAnimationDelta() {
        return animationDelta;
    }
}
