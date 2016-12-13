package com.creativept.skyboxforopengles;

import android.content.Context;

import com.creativept.opengl_samle_library.render.BaseSkyBoxRender;
import com.creativept.opengl_samle_library.shape.BaseSkyBox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * 类名
 * 创建时间 2016/12/13
 * 实现的主要功能
 *
 * @author zjc
 */

public class SkyBoxRender extends BaseSkyBoxRender {

    private float xRotation, yRotation;
    private final float[] texture_modelMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private Texture texture;

    public SkyBoxRender(Context context) {
        super(context);
    }

    public void handleTouchDrag(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;

        if (yRotation < -90) {
            yRotation = -90;
        } else if (yRotation > 90) {
            yRotation = 90;
        }
    }

    @Override
    protected BaseSkyBox getSkyBox() {
        return new SkyBox(context);
    }

    @Override
    protected void setLookMatrix(float[] viewMatrix) {
        setLookAtM(viewMatrix, 0, 0f, 0f, 0.01f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    protected void updateSkyBoxMatrix(float[] skyboxMatrix) {
        setIdentityM(skyboxMatrix, 0);
        rotateM(skyboxMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(skyboxMatrix, 0, -xRotation, 0f, 1f, 0f);
    }

    @Override
    protected float getLookAngle() {
        return 45;
    }

    @Override
    protected float getZ_NEAR() {
        return 0.01f;
    }

    @Override
    protected float getZ_FAR() {
        return 10f;
    }

    @Override
    public void surfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        texture = new Texture(context);
    }

    @Override
    public void surfaceChanged(GL10 gl10, int width, int height) {

    }

    @Override
    public void drawFrame(GL10 gl10, float[] projectionMatrix, float[] viewMatrix) {
        setIdentityM(texture_modelMatrix, 0);
        rotateM(texture_modelMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(texture_modelMatrix, 0, -xRotation, 0f, 1f, 0f);
        translateM(texture_modelMatrix, 0, 0f, 0f, -5f);
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, texture_modelMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
        texture.draw(viewProjectionMatrix);
    }
}
