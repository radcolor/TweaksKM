package com.theradcolor.kernel.utils.kernel;

import android.content.Context;
import androidx.annotation.NonNull;

import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

public class Battery {

    private static final String FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge";

    private static final String POWER_SUPPLY = "/sys/class/power_supply";
    private static final String CHARGING_CURRENT = POWER_SUPPLY + "/battery/current_now";
    private static final String CHARGE_STATUS = POWER_SUPPLY + "/battery/status";
    private static final String CHARGE_SOURCE = POWER_SUPPLY + "/battery/batt_charging_source";
    private static final String BCL = POWER_SUPPLY + "/battery/batt_slate_mode";
    private static final String HEALTH = POWER_SUPPLY + "/battery/health";
    private static final String LEVEL = POWER_SUPPLY + "/battery/capacity";
    private static final String VOLTAGE = POWER_SUPPLY + "/battery/voltage_now";
    private static final String ENABLE_CHARGING = POWER_SUPPLY + "/battery/charging_enabled";
    private static final String FASTCHG_TYPE = POWER_SUPPLY + "/battery/charge_type";
    private static final String CHARGE_TYPE = POWER_SUPPLY + "/usb/type";
    private static final String OP_OTG_SWITCH = POWER_SUPPLY + "/usb/otg_switch";

    public static String getChargingStatus() {
        int chargingrate = Utils.strToInt(Utils.readFile(CHARGING_CURRENT));
        if (chargingrate > 10000) {
            return String.valueOf(chargingrate / 1000);
        } else if (chargingrate < -10000) {
            return String.valueOf(chargingrate / -1000);
        } else if (chargingrate < 0 && chargingrate > -1000) {
            return String.valueOf(chargingrate * -1);
        } else {
            return String.valueOf(chargingrate);
        }
    }

    public String FastChargeStatus() {
        int ffc = Utils.strToInt(Utils.readFile(FAST_CHARGE));
        if(ffc==0){ return "Disabled"; }
        else if(ffc==1){ return "Enabled"; }
        return null;
    }

    private void run(String command, String id, Context context) {
        Control.runSetting(command, "BATTERY", id, context);
    }

    public void ForceFastChargeEnable(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", FAST_CHARGE), FAST_CHARGE, context);
    }

    public static String ChargingStatus() {
        return Utils.readFile(CHARGE_STATUS);
    }

    public static int ChargingType() {
        return Utils.strToInt(Utils.readFile(CHARGE_SOURCE));
    }

    public static boolean hasBatteryHealth() {
        return Utils.existFile(HEALTH);
    }

    public static String BatteryHealth() {
        return Utils.readFile(HEALTH);
    }

    public static boolean hasBatteryLevel() {
        return Utils.existFile(LEVEL);
    }

    public static boolean hasBatteryVoltage() {
        return Utils.existFile(VOLTAGE);
    }

    public static String BatteryVoltage() {
        int voltage = Utils.strToInt(Utils.readFile(VOLTAGE));
        return String.valueOf(voltage / 1000);
    }

    public static String ChargerType() {
        return Utils.readFile(CHARGE_TYPE);
    }

}
