package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

/**
 * This packet will be sent by the Server when the Server stops
 */
public class PacketTerminate extends Packet {


    PacketTerminate() {
        super(PacketType.TERMINATE);
    }
}
