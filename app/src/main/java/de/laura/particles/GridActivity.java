package de.laura.particles;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GridActivity extends AppCompatActivity implements Runnable {
    SurfaceView surfaceView;
    Handler handler;
    int space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_grid);

        handler = new Handler(getMainLooper());
        surfaceView = findViewById(R.id.grid_surface);
        space = getSharedPreferences("settings", MODE_PRIVATE).getInt("grid_space", 1);
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
    public void run() {
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        if (canvas != null) {
            ByteBuffer buf = ByteBuffer.allocateDirect(canvas.getWidth() * canvas.getHeight() * 4);
            Native.gridRender(canvas.getWidth(), canvas.getHeight(), buf, space);

            Bitmap bmp = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            buf.rewind();
            bmp.copyPixelsFromBuffer(buf);
            canvas.drawBitmap(bmp, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Paint());

            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        } else {
            handler.postDelayed(this, 1000 / 60);
        }
    }
}