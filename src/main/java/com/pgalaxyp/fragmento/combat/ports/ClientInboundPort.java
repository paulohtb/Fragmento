package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;

public interface ClientInboundPort {
    void acceptSnapshot(GameSnapshot snapshot);
}
