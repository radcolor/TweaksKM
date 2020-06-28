package com.theradcolor.kernel.utils.kernel;

import com.grarak.kerneladiutor.utils.Utils;

public class Entropy {

    private static final String PARENT = "/proc/sys/kernel/random";
    private static final String AVAILABLE = PARENT + "/entropy_avail";
    private static final String POOLSIZE = PARENT + "/poolsize";
    private static final String READ = PARENT + "/read_wakeup_threshold";
    private static final String WRITE = PARENT + "/write_wakeup_threshold";

    public int getWrite() {
        return Utils.strToInt(Utils.readFile(WRITE));
    }

    public int getRead() {
        return Utils.strToInt(Utils.readFile(READ));
    }

    public int getPoolSize() {
        return Utils.strToInt(Utils.readFile(POOLSIZE));
    }

    public int getAvailable() {
        return Utils.strToInt(Utils.readFile(AVAILABLE));
    }

    public static boolean supported() {
        return Utils.existFile(PARENT);
    }

}
