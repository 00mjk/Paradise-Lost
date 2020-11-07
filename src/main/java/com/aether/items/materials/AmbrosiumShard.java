package com.aether.items.materials;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItemGroups;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AmbrosiumShard extends Item {

    public AmbrosiumShard() {
        super(new Settings().group(AetherItemGroups.Materials));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == AetherBlocks.aether_grass) {
            if (!context.getPlayer().isCreative())
                context.getStack().setCount(context.getStack().getCount() - 1);
            context.getWorld().setBlockState(context.getBlockPos(), AetherBlocks.aether_enchanted_grass.getDefaultState());
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (playerIn.canFoodHeal()) {
            if (!playerIn.isCreative())
                heldItem.setCount(heldItem.getCount() - 1);
            playerIn.heal(2.0F);
            return new TypedActionResult<>(ActionResult.SUCCESS, heldItem);
        }

        return new TypedActionResult<>(ActionResult.PASS, heldItem);
    }
}