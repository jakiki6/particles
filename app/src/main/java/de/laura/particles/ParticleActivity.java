package de.laura.particles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class ParticleActivity extends AppCompatActivity implements Runnable {
    SurfaceView surfaceView;
    Handler handler;
    ArrayList<Float> touchesX = new ArrayList<>();
    ArrayList<Float> touchesY = new ArrayList<>();
    float posX[];
    float posY[];
    ByteBuffer buf;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_particle);

        surfaceView = findViewById(R.id.particle_surface);
        handler = new Handler(getMainLooper());

        posX = new float[Native.particleGetCount()];
        posY = new float[Native.particleGetCount()];

        Random rng = new Random();
        for (int i = 0; i < Native.particleGetCount(); i++) {
            posX[i] = rng.nextFloat();
            posY[i] = rng.nextFloat();
        }

        Native.particleInit(getSharedPreferences("settings", MODE_PRIVATE).getInt("particle_count", 100000));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchesX.clear();
        touchesY.clear();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (int i = 0; i < event.getPointerCount(); i++) {
                touchesX.add(event.getX(i) / surfaceView.getHolder().getSurfaceFrame().width());
                touchesY.add(event.getY(i) / surfaceView.getHolder().getSurfaceFrame().height());
            }
        }

        return super.onTouchEvent(event);
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
        Native.particleDeInit();
    }

    private float[] cast(ArrayList<Float> src) {
        float[] dest = new float[src.size()];
        for (int i = 0; i < src.size(); i++) dest[i] = src.get(i);
        return dest;
    }

    public void process() {
        Native.particleTick(touchesX.size(), cast(touchesX), cast(touchesY), posX, posY);
    }

    @Override
    public void run() {
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        if (canvas != null) {
            process();

            int width = surfaceView.getHolder().getSurfaceFrame().width();
            int height = surfaceView.getHolder().getSurfaceFrame().height();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            if (buf == null) {
                buf = ByteBuffer.allocateDirect(bmp.getByteCount());
            }
            Native.particleRender(posX, posY, width, height, buf);
            bmp.copyPixelsFromBuffer(buf);
            buf.rewind();
            canvas.drawBitmap(bmp, new Rect(0, 0, width, height), new Rect(0, 0, width, height), new Paint());

            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }

        handler.postDelayed(this, 1000 / 60);
    }
}