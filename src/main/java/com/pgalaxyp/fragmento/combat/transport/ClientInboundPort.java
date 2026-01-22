package com.pgalaxyp.fragmento.combat.transport;

public interface ClientInboundPort {
    void acceptSnapshot(GameSnapshot snapshot);
}
