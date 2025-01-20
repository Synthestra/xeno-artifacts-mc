package com.synthestra.xeno_artifacts.common.network;

import com.synthestra.xeno_artifacts.api.network.NetworkHelper;

public class ModNetwork {

    public static void init() {
        NetworkHelper.addNetworkRegistration(
                ModNetwork::registerMessages, 3);
    }

    private static void registerMessages(NetworkHelper.RegisterMessagesEvent event) {
        event.registerClientBound(ClientBoundParticlePacket.CODEC);
    }
}