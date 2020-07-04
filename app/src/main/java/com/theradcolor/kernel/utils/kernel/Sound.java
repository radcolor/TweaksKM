package com.theradcolor.kernel.utils.kernel;

import android.content.Context;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;
import java.util.ArrayList;
import java.util.List;

public class Sound {

    private static final String SOUND_CONTROL = "/sys/kernel/sound_control";
    private static final String HEADPHONE_FLAR = SOUND_CONTROL + "/headphone_gain";
    private static final String MICROPHONE_FLAR = SOUND_CONTROL + "/mic_gain";
    private static final String SPEAKER_FLAR = SOUND_CONTROL + "/speaker_gain";
    private static final String EARPIECE_FLAR = SOUND_CONTROL + "/earpiece_gain";

    private final List<String> mFlarLimits = new ArrayList<>();
    private final List<String> mFlarHpLimits = new ArrayList<>();

    {
        for (int i = -10; i < 21; i++) {
            mFlarLimits.add(String.valueOf(i));
        }

        for (int i = -40; i < 21; i++) {
            mFlarHpLimits.add(String.valueOf(i));
        }
    }

    public String getMicrophoneFlar() {
        return Utils.readFile(MICROPHONE_FLAR);
    }

    public List<String> getMicrophoneFlarLimits() {
        return mFlarLimits;
    }

    public static String getHeadphoneFlar(String channel) {
        String[] values = Utils.readFile(HEADPHONE_FLAR).split(" ");
        int gainLeft = Utils.strToInt(values[0]),
                gainRight = Utils.strToInt(values[1]);
        switch (channel) {
            case "all":
            case "left":
                if (gainLeft >= 216) {
                    return String.valueOf(gainLeft - 256);
                } else {
                    return String.valueOf(gainLeft);
                }
            case "right":
                if (gainRight >= 216) {
                    return String.valueOf(gainRight - 256);
                } else {
                    return String.valueOf(gainRight);
                }
        }
        return "";
    }

    public  void setHeadphoneFlarAll(String value, Context context) {
        SoundRun(value + " " + value, HEADPHONE_FLAR, HEADPHONE_FLAR, context);
    }

    public static void setHeadphoneFlar(String channel, String value, Context context) {
        switch (channel) {
            case "left":
                String currentGainRight = getHeadphoneFlar("right");
                SoundRun(value + " " + currentGainRight, HEADPHONE_FLAR, HEADPHONE_FLAR, context);
                break;
            case "right":
                String currentGainLeft = getHeadphoneFlar("left");
                SoundRun(currentGainLeft + " " + value, HEADPHONE_FLAR, HEADPHONE_FLAR, context);
                break;
        }
    }


    public static void setMicrophoneFlar(String value, Context context) {
        run(Control.write(value, MICROPHONE_FLAR), MICROPHONE_FLAR, context);
    }

    private static void SoundRun(String value, String path, String id, Context context) {
        int checksum = value.contains(" ") ?
                getChecksum(Utils.strToInt(value.split(" ")[0]),
                        Utils.strToInt(value.split(" ")[1])) :
                getChecksum(Utils.strToInt(value), 0);
        run(Control.write(value + " " + checksum, path), id, context);
        run(Control.write(value, path), id + "nochecksum", context);
    }

    private static int getChecksum(int arg0, int arg1) {
        return (Integer.MAX_VALUE ^ (arg0 & 0xff) + (arg1 & 0xff));
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, "SOUND", id, context);
    }

}
