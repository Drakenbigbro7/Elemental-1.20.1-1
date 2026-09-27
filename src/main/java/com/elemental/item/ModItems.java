package com.elemental.item;

import com.elemental.Elemental;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final Item SUNFORGED_SCIMITAR = Registry.register(
            Registries.ITEM,
            new Identifier("elemental", "sunforged_scimitar"),
            new SunforgedScimitarItem(
                    ScimitarToolMaterial.INSTANCE,
                    SunforgedScimitarItem.BASE_ATTACK_DAMAGE,
                    SunforgedScimitarItem.BASE_ATTACK_SPEED,
                    new FabricItemSettings().maxDamage(SunforgedScimitarItem.DURABILITY).rarity(Rarity.COMMON)
            )
    );
    public static final Item FROSTWAKE_PICK = Registry.register(
            Registries.ITEM,
            new Identifier(Elemental.MOD_ID,"frostwake_pick"),
            new FrostwakePickItem(
                    FrostwakePickToolMaterial.INSTANCE,
                    FrostwakePickItem.BASE_ATTACK_DAMAGE,
                    FrostwakePickItem.BASE_ATTACK_SPEED,
                    new FabricItemSettings().maxDamage(FrostwakePickItem.DURABILITY).rarity(Rarity.COMMON)
            )
    );
    public static final Item ROOTBOUND_AXE = Registry.register(
            Registries.ITEM,
            new Identifier(Elemental.MOD_ID, "rootbound_axe"),
            new RootboundAxeItem(
                    RootboundAxeToolMaterial.INSTANCE,
                    RootboundAxeItem.BASE_ATTACK_DAMAGE,
                    RootboundAxeItem.BASE_ATTACK_SPEED,
                    new FabricItemSettings().maxDamage(RootboundAxeItem.DURABILITY).rarity(Rarity.COMMON)
            )
    );

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(SUNFORGED_SCIMITAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SUNFORGED_SCIMITAR));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(FROSTWAKE_PICK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(FROSTWAKE_PICK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(ROOTBOUND_AXE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ROOTBOUND_AXE));
    }
}
