package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("ffmpeg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button tv = findViewById(R.id.sample_text);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    String base = Environment.getExternalStorageDirectory().getPath();
                    Log.e("PATH", base);
                    String[] commands = new String[9];
                    commands[0] = "ffmpeg";
                    //输入
                    commands[1] = "-i";
                    commands[2] =  "/sdcard/input.mp4";
                    //水印
                    commands[3] = "-i";
                    commands[4] = "/sdcard/ic_launcher.png";//此处的图片地址换成带透明通道的视频就可以合成动态视频遮罩。
                    commands[5] = "-filter_complex";
                    commands[6] = "[1:v]scale=300:300[s];[0:v][s]overlay=50:10";
                    //覆盖输出
                    commands[7] = "-y";//直接覆盖输出文件
                    //输出文件
                    commands[8] = base + "/merge.mp4";
                    int result = ffmpegRun(commands);
                    if (result == 0) {
                        Toast.makeText(MainActivity.this, "命令执行完成", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String base = Environment.getExternalStorageDirectory().getPath();
                    Log.e("PATH", base);
                    String[] commands = new String[9];
                    commands[0] = "ffmpeg";
                    //输入
                    commands[1] = "-i";
                    commands[2] = base + "/input.mp4";
                    //水印
                    commands[3] = "-i";
                    commands[4] = base + "/ic_launcher.png";//此处的图片地址换成带透明通道的视频就可以合成动态视频遮罩。
                    commands[5] = "-filter_complex";
                    commands[6] = "[1:v]scale=300:300[s];[0:v][s]overlay=50:10";
                    //覆盖输出
                    commands[7] = "-y";//直接覆盖输出文件
                    //输出文件
                    commands[8] = base + "/merge.mp4";
                    int result = ffmpegRun(commands);
                    if (result == 0) {
                        Toast.makeText(MainActivity.this, "命令执行完成", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public native int ffmpegRun(String[] cmd);
}
