package com.theradcolor.kernel.utils.kernel.cpuhotplug;

import android.content.Context;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;
import com.theradcolor.kernel.utils.kernel.cpu.CPU;

public class MPDecision {

    public static final String HOTPLUG_MPDEC = "mpdecision";

    public static void enableMpdecision(boolean enable, Context context) {
        if (enable) {
            run(Control.startService(HOTPLUG_MPDEC), HOTPLUG_MPDEC, context);
        } else {
            CPU cpuFreq = CPU.getInstance(context);
            run(Control.stopService(HOTPLUG_MPDEC), HOTPLUG_MPDEC, context);
            for (int i = 0; i < cpuFreq.getCpuCount(); i++) {
                cpuFreq.onlineCpu(i, true, "CPU_HOTPLUG", false, context);
            }
        }
    }

    public static boolean isMpdecisionEnabled() {
        return Utils.isPropRunning(HOTPLUG_MPDEC);
    }

    public static boolean supported() {
        return Utils.hasProp(HOTPLUG_MPDEC);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, "CPU_HOTPLUG", id, context);
    }

}
