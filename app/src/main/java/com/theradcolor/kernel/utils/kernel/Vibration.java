package com.theradcolor.kernel.utils.kernel;

import android.content.Context;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.HashMap;


public class Vibration {

    private static Vibration sInstance;

    public static Vibration getInstance() {
        if (sInstance == null) {
            sInstance = new Vibration();
        }
        return sInstance;
    }

    private static final String VIB_LIGHT = "/sys/devices/virtual/timed_output/vibrator/vmax_mv_light";
    private static final String VIB_ENABLE = "/sys/devices/i2c-3/3-0033/vibrator/vib0/vib_enable";

    private final HashMap<String, MinMax> mVibrations = new HashMap<>();

    {
        mVibrations.put("/sys/class/timed_output/vibrator/amp", new MinMax(0, 100));
        mVibrations.put("/sys/class/timed_output/vibrator/level", new MinMax(12, 31));
        mVibrations.put("/sys/class/timed_output/vibrator/pwm_value", new MinMax("/sys/class/timed_output/vibrator/pwm_min", "/sys/class/timed_output/vibrator/pwm_max", 0, 100));
        mVibrations.put("/sys/class/timed_output/vibrator/pwm_value_1p", new MinMax(53, 99));
        mVibrations.put("/sys/class/timed_output/vibrator/voltage_level", new MinMax(1200, 3199));
        mVibrations.put("/sys/class/timed_output/vibrator/vtg_level", new MinMax("/sys/class/timed_output/vibrator/vtg_min", "/sys/class/timed_output/vibrator/vtg_max", 12, 31));
        mVibrations.put("/sys/class/timed_output/vibrator/vmax_mv", new MinMax(116, 3596));
        mVibrations.put("/sys/class/timed_output/vibrator/vmax_mv_strong", new MinMax(116, 3596));
        mVibrations.put("/sys/devices/platform/tspdrv/nforce_timed", new MinMax(1, 127));
        mVibrations.put("/sys/devices/i2c-3/3-0033/vibrator/vib0/vib_duty_cycle", new MinMax(25, 100));
        mVibrations.put("/sys/module/qpnp_vibrator/parameters/vib_voltage", new MinMax(12, 31));
        mVibrations.put("/sys/vibrator/pwmvalue", new MinMax(0, 127));
        mVibrations.put("/sys/kernel/thunderquake_engine/level", new MinMax(0, 7));
    }

    private String FILE;
    private Integer MIN;
    private Integer MAX;

    public Vibration() {
        for (String file : mVibrations.keySet()) {
            if (Utils.existFile(file)) {
                FILE = file;
                break;
            }
        }

        if (FILE == null) return;
        MIN = mVibrations.get(FILE).getMin();
        MAX = mVibrations.get(FILE).getMax();
    }

    public void setVibration(int value, Context context) {
        boolean enable = Utils.existFile(VIB_ENABLE);
        if (enable) {
            run(Control.write("1", VIB_ENABLE), VIB_ENABLE + "enable", context);
        }
        run(Control.write(String.valueOf(value), FILE), FILE, context);
        if (Utils.existFile(VIB_LIGHT)) {
            run(Control.write(String.valueOf(value - 300 < 116 ? 116 : value - 300), VIB_LIGHT), VIB_LIGHT, context);
        }
        if (enable) {
            run(Control.write("0", VIB_ENABLE), VIB_ENABLE + "disable", context);
        }
    }

    public int getMax() {
        return MAX;
    }

    public int getMin() {
        return MIN;
    }

    public int get() {
        if (FILE != null) {
            return Utils.strToInt(Utils.readFile(FILE).replace("%", ""));
        }
        supported();
        if (FILE == null) return 0;
        return get();
    }

    public int getVibrationValue(){
        int min = getMin();
        int max = getMax();
        float offset = (max - min) / 100f;
        return Math.round((get() - min) / offset);
    }

    public void setVibrationValue(int value, Context context){
        int min = getMin();
        int max = getMax();
        float offset = (max - min) / 100f;
        int final_vib = Math.round(value * offset + min);
        setVibration(final_vib, context);
    }

    public boolean supported() {
        return FILE != null;
    }

    private void run(String command, String id, Context context) {
        Control.runSetting(command, "Vibration", id, context);
    }

    private class MinMax {

        private int mMin;
        private int mMax;
        private String mMinFile;
        private String mMaxFile;

        private MinMax(int min, int max) {
            mMin = min;
            mMax = max;
        }

        private MinMax(String minfile, String maxfile, int min, int max) {
            mMinFile = minfile;
            mMaxFile = maxfile;
            mMin = min;
            mMax = max;
        }

        public int getMin() {
            if (mMinFile != null && Utils.existFile(mMinFile)) {
                return Utils.strToInt(Utils.readFile(mMinFile));
            }
            return mMin;
        }

        public int getMax() {
            if (mMaxFile != null && Utils.existFile(mMaxFile)) {
                return Utils.strToInt(Utils.readFile(mMaxFile));
            }
            return mMax;
        }

    }

}
