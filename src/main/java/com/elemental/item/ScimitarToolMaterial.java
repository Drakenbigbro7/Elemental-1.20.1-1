package com.elemental.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ScimitarToolMaterial implements ToolMaterial {
    public static final ScimitarToolMaterial INSTANCE = new ScimitarToolMaterial();

    @Override
    public int getDurability() {
        return SunforgedScimitarItem.DURABILITY;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 6.0f;
    }

    @Override
    public float getAttackDamage() {
        return 3.0f;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.GOLD_INGOT);
    }
}
