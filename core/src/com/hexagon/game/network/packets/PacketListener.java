package com.hexagon.game.network.packets;

import java.util.Map;
import java.util.concurrent.Callable;

import de.svdragster.logica.util.Delegate;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketListener {

    Map<PacketType,Delegate>    DispatchTable;


    @Deprecated
    public PacketListener(){
    }

    public PacketListener(Map<PacketType,Delegate> dispatchTable){

        this.DispatchTable = dispatchTable;
    }

    public void call(Packet packet) throws Exception
    {
        /**
         * replaces All methods and will only invoke if the packet contains a type which is
         * implemented. Otherwise an exception is thrown
         */
        DispatchTable.get(packet.getType()).invoke(packet);


        /*
        if (packet instanceof PacketKeepAlive) {
            onKeepAlive((PacketKeepAlive) packet);
        } else {
            System.out.println("REST IN PEACE");
        }
        */

    }



    @Deprecated
    public  void onBuild(PacketBuild packet) throws Exception {
        DispatchTable.get(packet.getType()).invoke(packet);
    }

    @Deprecated
    public void onDestroy(PacketDestroy packet) throws Exception{
        DispatchTable.get(packet.getType()).invoke(packet);
    }

    @Deprecated
    public void onJoin(PacketJoin packet)throws Exception{
        DispatchTable.get(packet.getType()).invoke(packet);
    }

    @Deprecated
    public void onLeave(PacketLeave packet) throws Exception{
        DispatchTable.get(packet.getType()).invoke(packet);
    }

    @Deprecated
    public void onTrade(PacketTrade packet) throws Exception{
        DispatchTable.get(packet.getType()).invoke(packet);
    }

    @Deprecated
    public void onKeepAlive(PacketKeepAlive packet)throws Exception {
        DispatchTable.get(packet.getType()).invoke(packet);
        System.out.println("Received Keep Alive");
    }

    @Deprecated
    public void onMapUpdate(PacketMapUpdate packet) throws Exception{
        DispatchTable.get(packet.getType()).invoke(packet);
    }



}
