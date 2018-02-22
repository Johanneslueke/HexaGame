package com.hexagon.game.network.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketServerList extends Packet {

    public List<Entry> entries = new ArrayList<>();

    public PacketServerList() {
        super(PacketType.SERVER_LIST);
    }

    public PacketServerList(UUID senderId) {
        super(PacketType.SERVER_LIST, senderId);
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        for (Entry entry : entries) {
            builder.append(entry.host.toString())
                    .append(",")
                    .append(entry.room)
                    .append(";");
        }
        return super.serialize() + builder.toString();
    }

    public static class Entry {
        public UUID host;
        public String room;

        public Entry(UUID host, String room) {
            this.host = host;
            this.room = room;
        }
    }
}
