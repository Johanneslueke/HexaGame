package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketTrade extends Packet {

    private UUID        TradePartnerID;
    private String      ResourceSignature_Good1;
    private String      ResourceSignature_Good2;
    private int         amount_Good1;
    private int         amount_Good2;


    PacketTrade(PacketType type, UUID tradePartnerID,String sig_one,String sig_two,int amount_Good1,int amount_Good2) {
        super(type);
        this.amount_Good1 = amount_Good1;
        this.amount_Good2 = amount_Good2;
        this.ResourceSignature_Good1 = sig_one;
        this.ResourceSignature_Good2 = sig_two;
        this.TradePartnerID = tradePartnerID;
    }

    PacketTrade(PacketType type, UUID clientID,UUID tradePartnerID,String sig_one,String sig_two,int amount_Good1,int amount_Good2) {
        super(type, clientID);
        this.amount_Good1 = amount_Good1;
        this.amount_Good2 = amount_Good2;
        this.ResourceSignature_Good1 = sig_one;
        this.ResourceSignature_Good2 = sig_two;
        this.TradePartnerID = tradePartnerID;
    }


    public UUID getTradePartnerID() {
        return TradePartnerID;
    }

    public void setTradePartnerID(UUID tradePartnerID) {
        TradePartnerID = tradePartnerID;
    }

    public String getResourceSignature_Good1() {
        return ResourceSignature_Good1;
    }

    public void setResourceSignature_Good1(String resourceSignature_Good1) {
        ResourceSignature_Good1 = resourceSignature_Good1;
    }

    public String getResourceSignature_Good2() {
        return ResourceSignature_Good2;
    }

    public void setResourceSignature_Good2(String resourceSignature_Good2) {
        ResourceSignature_Good2 = resourceSignature_Good2;
    }

    public int getAmount_Good1() {
        return amount_Good1;
    }

    public void setAmount_Good1(int amount_Good1) {
        this.amount_Good1 = amount_Good1;
    }

    public int getAmount_Good2() {
        return amount_Good2;
    }

    public void setAmount_Good2(int amount_Good2) {
        this.amount_Good2 = amount_Good2;
    }
}
