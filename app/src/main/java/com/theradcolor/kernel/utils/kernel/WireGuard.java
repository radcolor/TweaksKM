package com.theradcolor.kernel.utils.kernel;

import com.grarak.kerneladiutor.utils.Utils;

public class WireGuard {

    private static final String WIREGUARD = "/sys/module/wireguard/version";

    public String getWireguard() {
        return Utils.readFile(WIREGUARD);
    }

}
