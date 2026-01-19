package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;

public interface ClientInboundPort {
    void acceptSnapshot(GameSnapshot snapshot);
}
