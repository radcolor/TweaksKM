package com.theradcolor.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class BrodcastReceiver extends BroadcastReceiver {

    Process process;
    MediaPlayer mediaPlayer;
    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mcontext = context;
        String action = intent.getStringExtra("action");

        if(action.equals("now")){
            optimize();
        }
        else if(action.equals("start")){
            start();
        }
        else if(action.equals("stop")){
            stop();
        }
        Intent i = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(i);

    }

    public void optimize(){
        execCommandLine("adb shell"+ "\n"+
                "am force-stop am force-stop com.android.chrome"+"\n"+
                "am force-stop com.instagram.android"+"\n"+
                "am force-stop org.telegram.messenger"+"\n"+
                "am force-stop com.whatsapp"+"\n"+
                "am force-stop com.google.android.gm"+"\n"+
                "am force-stop com.UCMobile.intl"+"\n"+
                "am force-stop com.twitter.android"+"\n"+
                "am force-stop cn.xender"+"\n"+
                "am force-stop com.snaptube.premium"+"\n"+
                "am force-stop com.google.android.youtube"+"\n"+
                "am force-stop com.android.vending"+"\n"+
                "am force-stop com.google.android.app.photos"+"\n"+
                "am force-stop com.google.android.googlequicksearchbox"+"\n");
    }

    public void  stop(){
    }

    public void start(){
    }

    void execCommandLine(String command)
    {
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        OutputStreamWriter osw = null;

        try
        {
            proc = runtime.exec("su");
            osw = new OutputStreamWriter(proc.getOutputStream());
            osw.write(command);
            osw.flush();
            osw.close();
        }
        catch (IOException ex)
        {
            Log.e("execCommandLine()", "Command resulted in an IO Exception: " + command);
            return;
        }
        finally
        {
            if (osw != null)
            {
                try
                {
                    osw.close();
                }
                catch (IOException e){}
            }
        }

        try
        {
            proc.waitFor();
        }
        catch (InterruptedException e){}

        if (proc.exitValue() != 0)
        {
            Log.e("execCommandLine()", "Command returned error: " + command + "\n  Exit code: " + proc.exitValue());
        }
    }


}
