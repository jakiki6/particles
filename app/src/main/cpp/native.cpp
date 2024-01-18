extern "C" {

#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <time.h>
#include <jni.h>

float *xvel;
float *yvel;
int *xant;
int *yant;
int *rant;
int PARTICLE_COUNT = 100000;
int ANT_COUNT = 100;
int STEPS_PER_FRAME = 1000;

JNIEXPORT void JNICALL
Java_de_laura_particles_Native_particleInit(JNIEnv *env, jclass clazz, jint particleCount) {
    PARTICLE_COUNT = particleCount;

    xvel = (float *) calloc(PARTICLE_COUNT, sizeof(float));
    yvel = (float *) calloc(PARTICLE_COUNT, sizeof(float));

    for (int i = 0; i < PARTICLE_COUNT; i++) {
        xvel[i] = 0;
        yvel[i] = 0;
    }
}

JNIEXPORT void JNICALL
Java_de_laura_particles_Native_antInit(JNIEnv *env, jclass clazz, jint antCount, jint stepsPerFrame, jint width, jint height, jobject buf) {
    uint32_t *colors = (uint32_t *) (jbyte *) env->GetDirectBufferAddress(buf);

    ANT_COUNT = antCount;
    STEPS_PER_FRAME = stepsPerFrame;
    xant = (int *) calloc(ANT_COUNT, sizeof(int));
    yant = (int *) calloc(ANT_COUNT, sizeof(int));
    rant = (int *) calloc(ANT_COUNT, sizeof(int));

    srandom(time(NULL));
    for (int i = 0; i < ANT_COUNT; i++) {
        xant[i] = random() % width;
        yant[i] = random() % height;
        rant[i] = random() % 4;
    }
}

JNIEXPORT void JNICALL
Java_de_laura_particles_Native_antDeInit(JNIEnv *env, jclass clazz) {
    free(xant);
    free(yant);
    free(rant);
}

JNIEXPORT void JNICALL
Java_de_laura_particles_Native_particleDeInit(JNIEnv *env, jclass clazz) {
    free(xvel);
    free(yvel);
}

JNIEXPORT void JNICALL
Java_de_laura_particles_Native_particleTick(JNIEnv *env, jclass clazz, jint num_touches,
                                                 jfloatArray touchesXarray,
                                                 jfloatArray touchesYarray, jfloatArray posX,
                                                 jfloatArray posY) {
    float *xpos = (float *) env->GetFloatArrayElements(posX, NULL);
    float *ypos = (float *) env->GetFloatArrayElements(posY, NULL);
    float *touchesX = (float *) env->GetFloatArrayElements(touchesXarray, NULL);
    float *touchesY = (float *) env->GetFloatArrayElements(touchesYarray, NULL);

    for (jint i = 0; i < num_touches; i++) {
        float touchX = touchesX[i];
        float touchY = touchesY[i];

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            float dx = touchX - xpos[i];
            float dy = touchY - ypos[i];

            if (dx == 0 || dy == 0) continue;

            float d1 = dx * dx + dy * dy;
            float d2 = 1 / d1;
            float d3 = (float) sqrt(d1);

            xvel[i] += d2 * (dx / d3) * 0.0001;
            yvel[i] += d2 * (dy / d3) * 0.0001;

        }
    }

    for (int i = 0; i < PARTICLE_COUNT; i++) {
        xpos[i] = (xpos[i] + xvel[i]);
        ypos[i] = (ypos[i] + yvel[i]);

        xvel[i] *= 0.9;
        yvel[i] *= 0.9;

        while (xpos[i] < 0) xpos[i] += 1;
        while (ypos[i] < 0) ypos[i] += 1;
        while (xpos[i] > 1) xpos[i] -= 1;
        while (ypos[i] > 1) ypos[i] -= 1;
    }

    env->ReleaseFloatArrayElements(posX, xpos, 0);
    env->ReleaseFloatArrayElements(posY, ypos, 0);
}

JNIEXPORT void JNICALL Java_de_laura_particles_Native_antTick(JNIEnv *env, jclass clazz, jint width, jint height, jobject buf) {
    uint32_t *colors = (uint32_t *) (jbyte *) env->GetDirectBufferAddress(buf);

    for (int j = 0; j < 1024; j++) {
        for (int i = 0; i < ANT_COUNT; i++) {
            int pixel = colors[xant[i] + yant[i] * width];
            if (pixel == 0xffffffff) {
                rant[i] = rant[i] == 3 ? 0 : rant[i] + 1;
                colors[xant[i] + yant[i] * width] = 0xff000000;
            } else {
                rant[i] = rant[i] == 0 ? 3 : rant[i] - 1;
                colors[xant[i] + yant[i] * width] = 0xffffffff;
            }

            switch (rant[i]) {
                case 0:
                    yant[i] -= 1;
                    break;
                case 1:
                    xant[i] += 1;
                    break;
                case 2:
                    yant[i] += 1;
                    break;
                case 3:
                    xant[i] -= 1;
            }

            if (xant[i] < 0) {
                xant[i] = width - 1;
            } else if (xant[i] >= width) {
                xant[i] = 0;
            }

            if (yant[i] < 0) {
                yant[i] = height - 1;
            } else if (yant[i] >= height) {
                yant[i] = 0;
            }
        }
    }
}

JNIEXPORT void JNICALL Java_de_laura_particles_Native_particleRender(JNIEnv *env, jclass clazz, jfloatArray posX, jfloatArray posY, jint width, jint height, jobject buf) {
    float *xpos = (float *) env->GetFloatArrayElements(posX, NULL);
    float *ypos = (float *) env->GetFloatArrayElements(posY, NULL);
    uint32_t *colors = (uint32_t *) (jbyte *) env->GetDirectBufferAddress(buf);
    for (int i = 0; i < width * height; i++) {
        colors[i] = 0xff000000;
    }

    for (int i = 0; i < PARTICLE_COUNT; i++) {
        int x = xpos[i] * width;
        int y = ypos[i] * height;
        colors[x + y * width] = 0xffffffff;
    }
}

JNIEXPORT void JNICALL Java_de_laura_particles_Native_gridRender(JNIEnv *env, jclass clazz, jint width, jint height, jobject buf, jint space) {
    uint32_t *colors = (uint32_t *) (jbyte *) env->GetDirectBufferAddress(buf);
    for (int i = 0; i < width * height; i++) {
        colors[i] = (i / space + ((i / width / space) & 1)) & 1 ? 0xffffffff : 0xff000000;
    }
}

JNIEXPORT jint JNICALL Java_de_laura_particles_Native_particleGetCount(JNIEnv *env, jclass clazz) {
    return PARTICLE_COUNT;
}

}