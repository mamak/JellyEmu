package server.realm;

import server.Client;
import server.PacketEnum;

public enum RealmPacketEnum implements PacketEnum {
    CLIENT_VERSION_ERROR("AlEv"),
    LOGIN_ERROR("AlEf");

    public String packet;

    RealmPacketEnum(String packet){
        this.packet=packet;
    }

    @Override
    public void send(Client client){
        client.getServer()._out.send(client, packet);
    }

    @Override
    public void send(Client client, String param){
        client.getServer()._out.send(client, packet + param);
    }
}
