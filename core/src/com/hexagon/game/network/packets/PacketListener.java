package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketListener {

    public void call(Packet packet) {
        if (packet instanceof PacketKeepAlive) {
            onKeepAlive((PacketKeepAlive) packet);
        } else {
            System.out.println("REST IN PEACE");
        }

    }

    public  void onBuild(PacketBuild packet) {

    }

    public void onDestroy(PacketDestroy packet) {

    }

    public void onJoin(PacketJoin packet) {

    }
    public void onLeave(PacketLeave packet) {

    }

    public void onTrade(PacketTrade packet) {

    }

    public void onKeepAlive(PacketKeepAlive packet) {
        System.out.println("Received Keep Alive");
    }

    public void onMapUpdate(PacketMapUpdate packet) {

    }


}
