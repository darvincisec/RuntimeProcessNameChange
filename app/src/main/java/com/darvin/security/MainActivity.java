package com.darvin.security;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = findViewById(R.id.sample_text);
        tv.setText(getCurrentProcessName());

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                tv.setText(getCurrentPSname());
            }
        });

    }

    /*
        This function gets the same processname irrespective of the process name change done
        in the native library
     */
    private String getCurrentProcessName(){
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid)
            {
                return processInfo.processName;
            }
        }
        return null;
    }
    /*
        This function gets the process name from the ps output to indicate the change in process
        name after the native library is loaded.
     */
    private String getCurrentPSname(){
        int uid = android.os.Process.myUid();
        Runtime runtime = Runtime.getRuntime();
        BufferedReader reader = null;
        StringTokenizer tokenizer = null;
        String processName = null;
        try {
            Process p = runtime.exec("ps -u " + uid );
            if(p != null) {
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                reader.readLine(); //To ignore the header
                tokenizer = new StringTokenizer(reader.readLine());
                int n = tokenizer.countTokens();
                int i = 1;
                while (i < n) {
                    tokenizer.nextToken();
                    i++;
                }
                processName = tokenizer.nextToken();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processName;
    }

}
