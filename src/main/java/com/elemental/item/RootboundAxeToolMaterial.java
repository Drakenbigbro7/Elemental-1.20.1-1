package com.elemental.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class
RootboundAxeToolMaterial implements ToolMaterial {
    public static final RootboundAxeToolMaterial INSTANCE = new RootboundAxeToolMaterial();

    @Override
    public int getDurability() {
        return RootboundAxeItem.DURABILITY;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 8.0f;
    }

    @Override
    public float getAttackDamage() {
        return 5.0f;
    }

    @Override
    public int getMiningLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.NETHERITE_SCRAP);
    }
}