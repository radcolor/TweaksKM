package com.theradcolor.kernel.utils.kernel.cpuhotplug;

public class Hotplug {

    public static boolean supported() {
        return MPDecision.supported() || MSMHotplug.getInstance().supported() || CoreCtl.getInstance().supported();
    }

}
