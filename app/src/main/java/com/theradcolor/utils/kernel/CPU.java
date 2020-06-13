package com.theradcolor.utils.kernel;

import com.grarak.kerneladiutor.utils.Utils;

public class CPU {

    private static final String CUR_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_cur_freq";
    private static final String AVAILABLE_FREQS = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_available_frequencies";
    private static final String CPU_MAX_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_max_freq";
    private static final String CPU_MIN_FREQ = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_min_freq";
    public static final String CPU_ONLINE = "/sys/devices/system/cpu/cpu%d/online";
    private static final String CPU_SCALING_GOVERNOR = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor";
    private static final String CPU_AVAILABLE_GOVERNORS = "/sys/devices/system/cpu/cpu%d/cpufreq/scaling_available_governors";

    public int getCurFreq(int cpu) {
        String value = Utils.readFile(Utils.strFormat(CUR_FREQ, cpu));
        if (value != null) {
            return Utils.strToInt(value);
        }
        return 0;
    }

    public int getMaxFreq(int cpu) {
        String value = Utils.readFile(Utils.strFormat(CPU_MAX_FREQ, cpu));
        if (value != null) {
            return Utils.strToInt(value);
        }
        return 0;
    }

    public int getMinFreq(int cpu) {
        String value = Utils.readFile(Utils.strFormat(CPU_MIN_FREQ, cpu));
        if (value != null) {
            return Utils.strToInt(value);
        }
        return 0;
    }

    public String getGovernor(int cpu) {
        String value = "";
        if (Utils.existFile(Utils.strFormat(CPU_SCALING_GOVERNOR, cpu))) {
            value = Utils.readFile(Utils.strFormat(CPU_SCALING_GOVERNOR, cpu));
        }
        return value;
    }
}
