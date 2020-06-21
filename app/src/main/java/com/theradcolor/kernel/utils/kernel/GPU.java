package com.theradcolor.kernel.utils.kernel;

import com.grarak.kerneladiutor.utils.Utils;

public class GPU {

    private static final String KGSL3D0_DEVFREQ_GPUBUSY = "/sys/class/kgsl/kgsl-3d0/gpubusy";
    private static final String CUR_KGSL3D0_DEVFREQ_FREQ = "/sys/class/kgsl/kgsl-3d0/gpuclk";
    private static final String MAX_KGSL3D0_DEVFREQ_FREQ = "/sys/class/kgsl/kgsl-3d0/max_gpuclk";
    private static final String MIN_KGSL3D0_DEVFREQ_FREQ = "/sys/class/kgsl/kgsl-3d0/devfreq/min_freq";
    private static final String AVAILABLE_KGSL3D0_DEVFREQ_FREQS = "/sys/class/kgsl/kgsl-3d0/gpu_available_frequencies";
    private static final String SCALING_KGSL3D0_DEVFREQ_GOVERNOR = "/sys/class/kgsl/kgsl-3d0/devfreq/governor";
    private static final String AVAILABLE_KGSL3D0_DEVFREQ_GOVERNORS = "/sys/class/kgsl/kgsl-3d0/devfreq/available_governors";

    public int getCurFreq() {
        return Utils.strToInt(Utils.readFile(CUR_KGSL3D0_DEVFREQ_FREQ));
    }

    public int getMaxFreq() {
        return Utils.strToInt(Utils.readFile(MAX_KGSL3D0_DEVFREQ_FREQ));
    }

}
