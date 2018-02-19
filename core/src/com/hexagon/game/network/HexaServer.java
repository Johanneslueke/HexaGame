package com.hexagon.game.network;

import com.hexagon.game.network.packets.Packet;
import com.hexagon.game.network.packets.PacketListner;

/**
 * Created by Johannes on 19.02.2018.
 */

public class HexaServer {

    /**
     * connection stuff by Sven ;)
     */
    private int     connection;

    /**
     * web address of the router which connects hosting-server with client-servers
     */
    private static final String   GameRouterDNS = "www.example.com";

    /**
     * Acts on Events
     */
    private PacketListner listner;


    public HexaServer(){
        //implementation details
    }


    public void Send(Packet packet){

    }

    public Packet Receive(){
        return null;
    }


}
