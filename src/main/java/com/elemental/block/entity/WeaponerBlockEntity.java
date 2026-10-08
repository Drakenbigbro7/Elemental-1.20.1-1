package com.elemental.block.entity;

import com.elemental.screen.WeaponerScreenHandler;
import com.elemental.util.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for the Weaponer crafting station.
 * Manages a 3-slot inventory (slot 0: Stencil, slot 1: Element, slot 2: Result).
 */
public class WeaponerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public WeaponerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WEAPONER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.elemental.weaponer");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WeaponerScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        // Persist only input slots so uncrafted result is calculated dynamically
        DefaultedList<ItemStack> toSave = DefaultedList.ofSize(3, ItemStack.EMPTY);
        toSave.set(0, this.items.get(0));
        toSave.set(1, this.items.get(1));
        Inventories.writeNbt(nbt, toSave);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.world != null && !this.world.isClient) {
            this.world.updateListeners(this.pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }
}
