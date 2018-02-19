package com.hexagon.game.network.packets;

/**
 * Created by Johannes on 19.02.2018.
 */

public interface PacketListner {

    public  void onBuild();
    public void onDestroy();

    public void onJoin();
    public void onLeave();

    public void onTrade();

    public void onKeepAlive();
    public void onMapUpdate();


}
