package com.pgalaxyp.fragmento.rpg.client;

import com.pgalaxyp.fragmento.rpg.client.state.ClientViewState;

public final class ClientNetworkProxy {

    private static final ClientViewState STATE = new ClientViewState();

    public static ClientViewState state() {
        return STATE;
    }

    private ClientNetworkProxy() {}
}