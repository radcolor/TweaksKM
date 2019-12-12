package com.theradcolor.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class BrodcastReceiver extends BroadcastReceiver {

    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mcontext = context;
        String action = intent.getStringExtra("action");

        if(action.equals("now")){
            optimize();
        }
        else if(action.equals("stop")){
            context.stopService(new Intent(context,GamingService.class));
        }
        Intent i = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(i);
    }

    public void optimize(){

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
