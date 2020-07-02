package com.theradcolor.kernel.utils.kernel;

import android.content.Context;

import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.List;


public class KCAL {

    private static KCAL sInstance;

    public static KCAL getInstance() {
        if (sInstance == null) {
            sInstance = new KCAL();
        }
        return sInstance;
    }

    private static final String KCAL = "/sys/devices/platform/kcal_ctrl.0";
    private static final String KCAL_CTRL = KCAL + "/kcal";
    private static final String KCAL_CTRL_CTRL = KCAL + "/kcal_ctrl";
    private static final String KCAL_CTRL_ENABLE = KCAL + "/kcal_enable";

    private final List<String> mKCAL_CTRL_MIN = new ArrayList<>();
    private final List<String> mKCAL_CTRL_INVERT = new ArrayList<>();
    private final List<String> mKCAL_CTRL_SAT = new ArrayList<>();
    private final List<String> mKCAL_CTRL_HUE = new ArrayList<>();
    private final List<String> mKCAL_CTRL_VAL = new ArrayList<>();
    private final List<String> mKCAL_CTRL_CONT = new ArrayList<>();

    {
        mKCAL_CTRL_MIN.add("/sys/devices/platform/kcal_ctrl.0/kcal_min");
        mKCAL_CTRL_MIN.add("/sys/module/msm_drm/parameters/kcal_min");
        mKCAL_CTRL_INVERT.add("/sys/devices/platform/kcal_ctrl.0/kcal_invert");
        mKCAL_CTRL_INVERT.add("/sys/module/msm_drm/parameters/kcal_invert");
        mKCAL_CTRL_SAT.add("/sys/devices/platform/kcal_ctrl.0/kcal_sat");
        mKCAL_CTRL_SAT.add("/sys/module/msm_drm/parameters/kcal_sat");
        mKCAL_CTRL_HUE.add("/sys/devices/platform/kcal_ctrl.0/kcal_hue");
        mKCAL_CTRL_HUE.add("/sys/module/msm_drm/parameters/kcal_hue");
        mKCAL_CTRL_VAL.add("/sys/devices/platform/kcal_ctrl.0/kcal_val");
        mKCAL_CTRL_VAL.add("/sys/module/msm_drm/parameters/kcal_val");
        mKCAL_CTRL_CONT.add("/sys/devices/platform/kcal_ctrl.0/kcal_cont");
        mKCAL_CTRL_CONT.add("/sys/module/msm_drm/parameters/kcal_cont");
    }

    private String KCAL_CTRL_MIN;
    private String KCAL_CTRL_INVERT;
    private String KCAL_CTRL_SAT;
    private String KCAL_CTRL_HUE;
    private String KCAL_CTRL_VAL;
    private String KCAL_CTRL_CONT;

    public KCAL() {
        for (String file : mKCAL_CTRL_MIN) {
            if (Utils.existFile(file)) {
                KCAL_CTRL_MIN = file;
                break;
            }
        }
        for (String file : mKCAL_CTRL_INVERT) {
            if (Utils.existFile(file)) {
                KCAL_CTRL_INVERT = file;
                break;
            }
        }
        for (String file : mKCAL_CTRL_SAT) {
            if (Utils.existFile(file)) {
                KCAL_CTRL_SAT = file;
                break;
            }
        }
        for (String file : mKCAL_CTRL_HUE) {
            if (Utils.existFile(file)) {
                KCAL_CTRL_HUE = file;
                break;
            }
        }
        for (String file : mKCAL_CTRL_VAL) {
            if (Utils.existFile(file)) {
                KCAL_CTRL_VAL = file;
                break;
            }
        }
        for (String file : mKCAL_CTRL_CONT) {
            if (Utils.existFile(file)) {
                KCAL_CTRL_CONT = file;
                break;
            }
        }
    }

    public void setScreenContrast(int value, Context context) {
        run(Control.write(String.valueOf(value), KCAL_CTRL_CONT), KCAL_CTRL_CONT, context);
    }

    public int getScreenContrast() {
        return Utils.strToInt(Utils.readFile(KCAL_CTRL_CONT));
    }

    public boolean hasScreenContrast() {
        return Utils.existFile(KCAL_CTRL_CONT);
    }

    public void setScreenValue(int value, Context context) {
        run(Control.write(String.valueOf(value), KCAL_CTRL_VAL), KCAL_CTRL_VAL, context);
    }

    public int getScreenValue() {
        return Utils.strToInt(Utils.readFile(KCAL_CTRL_VAL));
    }

    public boolean hasScreenValue() {
        return Utils.existFile(KCAL_CTRL_VAL);
    }

    public void setScreenHue(int value, Context context) {
        run(Control.write(String.valueOf(value), KCAL_CTRL_HUE), KCAL_CTRL_HUE, context);
    }

    public int getScreenHue() {
        return Utils.strToInt(Utils.readFile(KCAL_CTRL_HUE));
    }

    public boolean hasScreenHue() {
        return Utils.existFile(KCAL_CTRL_HUE);
    }

    public void enableGrayscaleMode(boolean enable, Context context) {
        setSaturationIntensity(enable ? 128 : 255, context);
    }

    public void setSaturationIntensity(int value, Context context) {
        run(Control.write(String.valueOf(value), KCAL_CTRL_SAT), KCAL_CTRL_SAT, context);
    }

    public int getSaturationIntensity() {
        return Utils.strToInt(Utils.readFile(KCAL_CTRL_SAT));
    }

    public boolean hasSaturationIntensity() {
        return Utils.existFile(KCAL_CTRL_SAT);
    }

    public void enableInvertScreen(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", KCAL_CTRL_INVERT), KCAL_CTRL_INVERT, context);
    }

    public boolean isInvertScreenEnabled() {
        return Utils.readFile(KCAL_CTRL_INVERT).equals("1");
    }

    public boolean hasInvertScreen() {
        return Utils.existFile(KCAL_CTRL_INVERT);
    }

    public void setMinColor(int value, Context context) {
        run(Control.write(String.valueOf(value), KCAL_CTRL_MIN), KCAL_CTRL_MIN, context);
    }

    public int getMinColor() {
        return Utils.strToInt(Utils.readFile(KCAL_CTRL_MIN));
    }

    public boolean hasMinColor() {
        return Utils.existFile(KCAL_CTRL_MIN);
    }

    public void setKCAL(int red, int green, int blue, Context context){
        run(Control.write(red + " " + green + " " + blue, KCAL_CTRL), "KCAL" , context);
    }

    private void run(String command, String id, Context context) {
        Control.runSetting(command, "SCREEN", id, context);
    }

}
