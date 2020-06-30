package com.theradcolor.kernel.utils.kernel;

import com.grarak.kerneladiutor.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Network {

    private static final String TCP_AVAILABLE_CONGESTIONS = "/proc/sys/net/ipv4/tcp_available_congestion_control";

    public String getTcpCongestion() {
        return getTcpAvailableCongestions().get(0);
    }

    public List<String> getTcpAvailableCongestions() {
        return new ArrayList<>(Arrays.asList(Utils.readFile(TCP_AVAILABLE_CONGESTIONS).split(" ")));
    }

}
