package com.legacy.aether.blocks.decorative;

import com.legacy.aether.blocks.BlocksAether;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class BlockAetherWall extends WallBlock {

    public BlockAetherWall(BlockState state) {
        super(FabricBlockSettings.copy(state.getBlock()).build());
    }

    @Override
    public boolean isIn(Tag<Block> tag) {
        return tag == BlockTags.WALLS || super.isIn(tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState blockState_1, BlockState blockState_2, Direction direction_1) {
        return this.isAerogel() && blockState_2.getBlock() == this || super.isSideInvisible(blockState_1, blockState_2, direction_1);
    }

    @Override
    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return this.isAerogel();
    }

    private boolean isAerogel() {
        return this == BlocksAether.aerogel_wall;
    }

}