package com.theradcolor.kernel.utils.kernel;

import android.content.Context;

import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.Arrays;
import java.util.List;

public class GPU {

    private static final String KGSL3D0_DEVFREQ_GPUBUSY = "/sys/class/kgsl/kgsl-3d0/gpu_busy_percentage";
    private static final String CUR_KGSL3D0_DEVFREQ_FREQ = "/sys/class/kgsl/kgsl-3d0/gpuclk";
    private static final String MAX_KGSL3D0_DEVFREQ_FREQ = "/sys/class/kgsl/kgsl-3d0/max_gpuclk";
    private static final String MIN_KGSL3D0_DEVFREQ_FREQ = "/sys/class/kgsl/kgsl-3d0/devfreq/min_freq";
    private static final String AVAILABLE_KGSL3D0_DEVFREQ_FREQS = "/sys/class/kgsl/kgsl-3d0/gpu_available_frequencies";
    private static final String SCALING_KGSL3D0_DEVFREQ_GOVERNOR = "/sys/class/kgsl/kgsl-3d0/devfreq/governor";
    private static final String AVAILABLE_KGSL3D0_DEVFREQ_GOVERNORS = "/sys/class/kgsl/kgsl-3d0/devfreq/available_governors";
    private static final String GPU_POWER_LEVEL = "/sys/class/kgsl/kgsl-3d0/default_pwrlevel";

    public static String getPwrLevel() {
        return Utils.readFile(GPU_POWER_LEVEL);
    }

    public static void setPwrLevel(String value, Context context) {
        run(Control.write(String.valueOf(value), GPU_POWER_LEVEL), GPU_POWER_LEVEL, context);
    }

    public static boolean hasPwrLevel() {
        return Utils.existFile(GPU_POWER_LEVEL);
    }

    public static int getCurFreq() {
        return Utils.strToInt(Utils.readFile(CUR_KGSL3D0_DEVFREQ_FREQ));
    }

    public static int getMaxFreq() {
        return Utils.strToInt(Utils.readFile(MAX_KGSL3D0_DEVFREQ_FREQ));
    }

    public static int getMinFreq() {
        return Utils.strToInt(Utils.readFile(MIN_KGSL3D0_DEVFREQ_FREQ));
    }

    public static String getGoverner() {
        return Utils.readFile(SCALING_KGSL3D0_DEVFREQ_GOVERNOR);
    }

    public static List<String> getAvailableGovernors() {
        return Arrays.asList(AVAILABLE_KGSL3D0_DEVFREQ_GOVERNORS);
    }

    public static String getGpuBusy() {
        return Utils.readFile(KGSL3D0_DEVFREQ_GPUBUSY);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, "GPU", id, context);
    }

}
