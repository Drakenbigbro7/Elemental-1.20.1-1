package com.elemental.network;

import com.elemental.Elemental;
import com.elemental.item.TidebreakerTridentItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class TidebreakerNetworking {
    public static final Identifier DASH_PACKET_ID = Elemental.id("tidebreaker_dash");

    public static void registerServerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(DASH_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                TidebreakerTridentItem.performDash(player);
            });
        });
    }
}
