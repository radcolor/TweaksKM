package com.theradcolor.kernel.utils.kernel;

import com.grarak.kerneladiutor.utils.root.RootUtils;

public class sRGB {

    public static void srgbON(){
        RootUtils.runCommand("active=1\n" +
                "\n" +
                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                "\n" +
                "if [ $active = \"1\" ]\n" +
                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                "fi");
    }

    public static void srgbOFF(){
        RootUtils.runCommand("active=0\n" +
                "\n" +
                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                "\n" +
                "if [ $active = \"1\" ]\n" +
                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                "fi");
    }

}
