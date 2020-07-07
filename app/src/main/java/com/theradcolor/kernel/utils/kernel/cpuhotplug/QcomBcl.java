package com.theradcolor.kernel.utils.kernel.cpuhotplug;

import android.content.Context;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;
import com.grarak.kerneladiutor.utils.root.RootUtils;

public class QcomBcl {

    private static final String PATH = "/sys/devices/soc.0/qcom,bcl.*/";
    private static String PARENT;
    private static String DEFAULT_HOTPLUG_MASK;
    private static String DEFAULT_SOC_HOTPLUG_MASK;

    public static void online(boolean online, Context context) {
        online(online, "CPU_HOTPLUG", context);
    }

    public static void online(boolean online, String category, Context context) {
        Control.runSetting(Control.write(online ? "disable" : "enable", PARENT + "/mode"), category,
                PARENT + "/mode" + online, context);
        if (DEFAULT_HOTPLUG_MASK != null) {
            Control.runSetting(Control.write(online ? "0" : DEFAULT_HOTPLUG_MASK, PARENT + "/hotplug_mask"),
                    category, PARENT + "/hotplug_mask" + online, context);
        }
        if (DEFAULT_SOC_HOTPLUG_MASK != null) {
            Control.runSetting(Control.write(online ? "0" : DEFAULT_SOC_HOTPLUG_MASK,
                    PARENT + "/hotplug_soc_mask"), category, PARENT + "/hotplug_soc_mask" + online, context);
        }
        Control.runSetting(Control.write(online ? "enable" : "disable", PARENT + "/mode"), category,
                PARENT + "/mode" + online + "1", context);
    }

    public static boolean supported() {
        if (PARENT != null && !PARENT.isEmpty()) return true;
        if (Utils.existFile(PATH)) {
            PARENT = RootUtils.runCommand("realpath " + PATH);
            if (Utils.existFile(PARENT + "/hotplug_mask")) {
                DEFAULT_HOTPLUG_MASK = Utils.readFile(PARENT + "/hotplug_mask");
            }
            if (Utils.existFile(PARENT + "/hotplug_soc_mask")) {
                DEFAULT_SOC_HOTPLUG_MASK = Utils.readFile(PARENT + "/hotplug_soc_mask");
            }
            return true;
        }
        PARENT = "";
        return false;
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, "CPU_HOTPLUG", id, context);
    }

}
