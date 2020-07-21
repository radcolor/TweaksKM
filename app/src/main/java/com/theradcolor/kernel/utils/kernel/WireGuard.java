package com.theradcolor.kernel.utils.kernel;

import com.grarak.kerneladiutor.utils.Utils;

public class WireGuard {

    private static final String WIREGUARD = "/sys/module/wireguard/version";

    public static String getWireGuard() {
        return Utils.readFile(WIREGUARD);
    }

    public static boolean hasWireGuard() {
        return Utils.existFile(WIREGUARD);
    }

}
