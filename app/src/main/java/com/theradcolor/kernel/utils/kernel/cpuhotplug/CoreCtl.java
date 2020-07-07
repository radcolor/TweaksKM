package com.theradcolor.kernel.utils.kernel.cpuhotplug;

import android.content.Context;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;
import com.theradcolor.kernel.utils.kernel.cpu.CPU;

import java.util.ArrayList;
import java.util.List;

public class CoreCtl {

    private static CoreCtl sInstance;

    public static CoreCtl getInstance() {
        if (sInstance == null) {
            sInstance = new CoreCtl();
        }
        return sInstance;
    }

    public static final String CORE_CTL = "/sys/devices/system/cpu/cpu%d/core_ctl";
    private static final String HCUBE = "/sys/devices/system/cpu/cpu%d/hcube";

    private static final String ENABLE = "/hc_on";
    private static final String IS_BIG_CLUSTER = "/is_big_cluster";
    public static final String MIN_CPUS = "/min_cpus";
    private static final String BUSY_DOWN_THRESHOLD = "/busy_down_thres";
    private static final String BUSY_UP_THRESHOLD = "/busy_up_thres";
    private static final String OFFLINE_DELAY_MS = "/offline_delay_ms";
    private static final String ONLINE_DELAY_MS = "/online_delay_ms";

    private String PARENT;

    private final List<String> sFiles = new ArrayList<>();

    {
        sFiles.add(CORE_CTL);
        sFiles.add(HCUBE);
    }

    private CoreCtl() {
        String parent = null;
        for (String file : sFiles) {
            if (Utils.existFile(Utils.strFormat(file, 0))) {
                parent = file;
                break;
            }
        }
        if (parent != null) {
            if (Utils.existFile(Utils.strFormat(parent, CPU.getInstance().getBigCpu()))) {
                PARENT = Utils.strFormat(parent, CPU.getInstance().getBigCpu());
                if (Utils.existFile(PARENT + IS_BIG_CLUSTER)) {
                    PARENT = Utils.readFile(PARENT + IS_BIG_CLUSTER).equals("1") ? PARENT : null;
                }
            }
        }
    }

    public void setOfflineDelayMs(int value, Context context) {
        run(Control.write(String.valueOf(value), PARENT + OFFLINE_DELAY_MS), PARENT + OFFLINE_DELAY_MS, context);
    }

    public int getOfflineDelayMs() {
        return Utils.strToInt(Utils.readFile(PARENT + OFFLINE_DELAY_MS));
    }

    public boolean hasOfflineDelayMs() {
        return Utils.existFile(PARENT + OFFLINE_DELAY_MS);
    }

    public void setOnlineDelayMs(int value, Context context) {
        run(Control.write(String.valueOf(value), PARENT + ONLINE_DELAY_MS), PARENT + ONLINE_DELAY_MS, context);
    }

    public int getOnlineDelayMs() {
        return Utils.strToInt(Utils.readFile(PARENT + ONLINE_DELAY_MS));
    }

    public boolean hasOnlineDelayMs() {
        return Utils.existFile(PARENT + ONLINE_DELAY_MS);
    }

    public void setBusyUpThreshold(int value, Context context) {
        run(Control.write(String.valueOf(value), PARENT + BUSY_UP_THRESHOLD),
                PARENT + BUSY_UP_THRESHOLD, context);
    }

    public int getBusyUpThreshold() {
        String value = Utils.readFile(PARENT + BUSY_UP_THRESHOLD);
        if (value.contains(" ")) {
            return Utils.strToInt(value.split(" ")[0]);
        }
        return Utils.strToInt(value);
    }

    public boolean hasBusyUpThreshold() {
        return Utils.existFile(PARENT + BUSY_UP_THRESHOLD);
    }

    public void setBusyDownThreshold(int value, Context context) {
        run(Control.write(String.valueOf(value), PARENT + BUSY_DOWN_THRESHOLD),
                PARENT + BUSY_DOWN_THRESHOLD, context);
    }

    public int getBusyDownThreshold() {
        String value = Utils.readFile(PARENT + BUSY_DOWN_THRESHOLD);
        if (value.contains(" ")) {
            return Utils.strToInt(value.split(" ")[0]);
        }
        return Utils.strToInt(value);
    }

    public boolean hasBusyDownThreshold() {
        return Utils.existFile(PARENT + BUSY_DOWN_THRESHOLD);
    }

    public void setMinCpus(int min, int cpu, Context context) {
        setMinCpus(min, cpu, "CPU_HOTPLUG", context);
    }

    public void setMinCpus(int min, int cpu, String category, Context context) {
        if (context != null) {
            CPU.getInstance(context).mCoreCtlMinCpu = min;
        }
        Control.runSetting(Control.write(String.valueOf(min), Utils.strFormat(PARENT + MIN_CPUS,
                cpu)), category, Utils.strFormat(PARENT + MIN_CPUS, cpu), context);
    }

    public int getMinCpus(int core) {
        return Utils.strToInt(Utils.readFile(Utils.strFormat(PARENT + MIN_CPUS, core)));
    }

    public boolean hasMinCpus() {
        return hasMinCpus(0);
    }

    public boolean hasMinCpus(int core) {
        return Utils.existFile(Utils.strFormat(PARENT + MIN_CPUS, core));
    }

    public void enable(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", PARENT + ENABLE), PARENT + ENABLE, context);
    }

    public boolean isEnabled() {
        return Utils.readFile(PARENT + ENABLE).equals("1");
    }

    public boolean hasEnable() {
        return Utils.existFile(PARENT + ENABLE);
    }

    public boolean supported() {
        return PARENT != null;
    }

    private void run(String command, String id, Context context) {
        Control.runSetting(command, "CPU_HOTPLUG", id, context);
    }

}
