package de.laura.particles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class AntActivity extends AppCompatActivity implements Runnable {

    SurfaceView surfaceView;
    Handler handler;
    Bitmap bmp;
    ByteBuffer buf;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ant);

        surfaceView = findViewById(R.id.ant_surface);
        handler = new Handler(getMainLooper());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        handler.postDelayed(this, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Native.antDeInit();
    }

    private float[] cast(ArrayList<Float> src) {
        float[] dest = new float[src.size()];
        for (int i = 0; i < src.size(); i++) dest[i] = src.get(i);
        return dest;
    }

    @Override
    public void run() {
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        if (canvas != null) {
            int width = surfaceView.getHolder().getSurfaceFrame().width();
            int height = surfaceView.getHolder().getSurfaceFrame().height();

            if (bmp == null) {
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                buf = ByteBuffer.allocateDirect(bmp.getByteCount());
                SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
                Native.antInit(pref.getInt("ant_count", 100), pref.getInt("ant_steps", 1000), width, height, buf);
            }

            Native.antTick(width, height, buf);
            bmp.copyPixelsFromBuffer(buf);
            buf.rewind();
            canvas.drawBitmap(bmp, new Rect(0, 0, width, height), new Rect(0, 0, width, height), new Paint());

            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }

        handler.postDelayed(this, 1);
    }
}