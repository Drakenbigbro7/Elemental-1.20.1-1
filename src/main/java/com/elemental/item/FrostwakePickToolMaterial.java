package com.elemental.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class FrostwakePickToolMaterial implements ToolMaterial {
    public static final FrostwakePickToolMaterial INSTANCE = new FrostwakePickToolMaterial();

    @Override
    public int getDurability() {
        return FrostwakePickItem.DURABILITY;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 8.0f;
    }

    @Override
    public float getAttackDamage() {
        return 4.0f;
    }

    @Override
    public int getMiningLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 18;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.BLUE_ICE);
    }
}