package com.hexagon.game.network.listener;

import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.Packet;
import com.hexagon.game.network.packets.PacketType;

import java.util.HashMap;
import java.util.Map;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Sven on 26.02.2018.
 */

public abstract class PacketListener {

    HexaServer server;
    Map<PacketType,Delegate> dispatchTable = new HashMap<>();

    public PacketListener(HexaServer server) {
        this.server = server;
    }

    public abstract void registerAll();

    public void call(Packet packet) throws Exception {
        /**
         * replaces All methods and will only invoke if the packet contains a type which is
         * implemented. Otherwise an exception is thrown
         */
        dispatchTable
                .get(
                        packet.getType()
                ).invoke(
                        packet
                );

    }

}
