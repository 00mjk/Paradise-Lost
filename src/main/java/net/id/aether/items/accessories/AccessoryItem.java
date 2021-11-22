package net.id.aether.items.accessories;

import net.id.aether.Aether;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

// unused
public class AccessoryItem extends Item {
    private final AccessoryType type;
    private final Identifier texture, texture_slim;
    private final float damageMultiplier;

    public AccessoryItem(AccessoryType type, String material, float damageMultiplier, Settings settings) {
        super(settings.maxDamage(5 * type.getDurability()));
        this.type = type;
        this.texture = Aether.locate("textures/armor/accessory_" + material + ".png");
        this.texture_slim = Aether.locate("textures/armor/accessory_" + material + "_slim.png");
        this.damageMultiplier = damageMultiplier;
    }

    public AccessoryItem(AccessoryType type, float damageMultiplier, Settings settings) {
        this(type, "base", damageMultiplier, settings);
    }

    public AccessoryItem(AccessoryType type, String material, Settings settings) {
        this(type, material, 1f, settings);
    }

    public AccessoryItem(AccessoryType type, Settings settings) {
        this(type, 1f, settings);
    }

//
//    public AccessoryItem(AccessoryType ascension) {
//        this(ascension, Rarity.COMMON, 0xDDDDDD);
//    }

//    public AccessoryItem(AccessoryType ascension, Settings settings) {
//        this(ascension, rarity, 0xDDDDDD);
//    }

//    public AccessoryItem(AccessoryType ascension, int color) {
//        this(ascension, Rarity.COMMON, color);
//    }

//    public AccessoryItem(String material, AccessoryType ascension) {
//        this(material, ascension, Rarity.COMMON, 0xDDDDDD);
//    }
//
//    public AccessoryItem(String material, AccessoryType ascension, Settings settings) {
//        this(material, ascension, rarity, 0xDDDDDD);
//    }
//
//    public AccessoryItem(String material, AccessoryType ascension, int color) {
//        this(material, ascension, Rarity.COMMON, color);
//    }

    public Identifier getTexture() {
        return this.getTexture(false);
    }

    public Identifier getTexture(boolean isSlim) {
        return isSlim ? this.texture_slim : this.texture;
    }

    public AccessoryType getType() {
        return this.type;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }
}