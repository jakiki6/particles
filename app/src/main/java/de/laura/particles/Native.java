package de.laura.particles;

import android.util.Pair;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Native {
    public static native void particleInit(int particleCount);
    public static native void antInit(int antCount, int stepsPerFrame, int width, int height, ByteBuffer buf);
    public static native void particleDeInit();
    public static native void antDeInit();
    public static native void particleTick(int touchCount, float[] touchesX, float[] touchesY, float[] posX, float[] posY);
    public static native void antTick(int width, int height, ByteBuffer buf);
    public static native void particleRender(float[] posX, float[] posY, int width, int height, ByteBuffer buf);
    public static native void gridRender(int width, int height, ByteBuffer buf, int space);
    public static native int particleGetCount();
}
