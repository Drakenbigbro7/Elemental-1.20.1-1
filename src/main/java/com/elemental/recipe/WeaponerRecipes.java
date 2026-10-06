package com.elemental.recipe;

import com.elemental.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Single-file recipe manager for the Weaponer crafting station.
 * Easily register and retrieve elemental weapon crafting recipes using helper methods.
 */
public class WeaponerRecipes {
    public record Recipe(Item stencil, Item element, Item result, int count) {
        public boolean matches(ItemStack stencilStack, ItemStack elementStack) {
            return stencilStack.isOf(this.stencil) && elementStack.isOf(this.element);
        }

        public ItemStack createResult() {
            return new ItemStack(this.result, this.count);
        }
    }

    private static final List<Recipe> RECIPES = new ArrayList<>();

    static {
        // Register all Weaponer recipes here in one place
        register(ModItems.STENCIL, ModItems.DRY_HEAT_ELEMENT, ModItems.SUNFORGED_SCIMITAR);
        register(ModItems.STENCIL, ModItems.SNOW_ELEMENT, ModItems.FROSTWAKE_PICK);
        register(ModItems.STENCIL, ModItems.TREE_ELEMENT, ModItems.ROOTBOUND_AXE);
    }

    /**
     * Helper method to register a recipe with default count of 1.
     */
    public static void register(Item stencil, Item element, Item result) {
        register(stencil, element, result, 1);
    }

    /**
     * Helper method to register a recipe with a custom result count.
     */
    public static void register(Item stencil, Item element, Item result, int count) {
        RECIPES.add(new Recipe(stencil, element, result, count));
    }

    /**
     * Helper method to find the matching recipe and return the crafted result.
     */
    public static ItemStack getCraftingResult(ItemStack stencil, ItemStack element) {
        if (stencil.isEmpty() || element.isEmpty()) {
            return ItemStack.EMPTY;
        }
        for (Recipe recipe : RECIPES) {
            if (recipe.matches(stencil, element)) {
                return recipe.createResult();
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Helper method to check if an item is a valid elemental ingredient.
     */
    public static boolean isElementIngredient(ItemStack stack) {
        if (stack.isOf(ModItems.ELEMENT_CORE) || stack.isOf(ModItems.FIRE_ELEMENT)
                || stack.isOf(ModItems.OCEAN_ELEMENT) || stack.isOf(ModItems.MOUNTAIN_ELEMENT)) {
            return true;
        }
        for (Recipe recipe : RECIPES) {
            if (stack.isOf(recipe.element())) {
                return true;
            }
        }
        return false;
    }

    public static List<Recipe> getAllRecipes() {
        return RECIPES;
    }

    public static void init() {
        // Ensures class loading / static initializer runs
    }
}
