package com.aether.items.weapons;

import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonDartEntity;
import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class Dart extends Item {

    public Dart(Rarity rarity) {
        super(new Settings().rarity(rarity).group(AetherItemGroups.Weapons));
    }

    public PersistentProjectileEntity createDart(World world, ItemStack stack, LivingEntity entity) {
        if (this == AetherItems.ENCHANTED_DART) return new EnchantedDartEntity(entity, world);
        else if (this == AetherItems.POISON_DART) return new PoisonDartEntity(entity, world);
        return new GoldenDartEntity(entity, world);
    }
}