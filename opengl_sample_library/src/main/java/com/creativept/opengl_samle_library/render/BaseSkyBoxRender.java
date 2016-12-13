package com.creativept.opengl_samle_library.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.creativept.opengl_samle_library.shape.BaseSkyBox;
import com.creativept.opengl_samle_library.util.CommonUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

/**
 * 类名
 * 创建时间 2016/12/13
 * 实现的主要功能
 *
 * @author zjc
 */

public abstract class BaseSkyBoxRender implements GLSurfaceView.Renderer {


    protected final Context context;
    private BaseSkyBox mSkyBox;
    private float angle, z_near, z_far;
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] skybox_modelMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];


    public BaseSkyBoxRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mSkyBox = getSkyBox();
        angle = getLookAngle();
        z_near = getZ_NEAR();
        z_far = getZ_FAR();
        surfaceCreated(gl10, eglConfig);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);

        CommonUtil.perspectiveM(projectionMatrix, angle, (float) width
                / (float) height, z_near, z_far);
        setLookMatrix(viewMatrix);

        surfaceChanged(gl10, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
//        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT);// | GL_DEPTH_BUFFER_BIT
        if (mSkyBox != null) {
            updateSkyBoxMatrix(skybox_modelMatrix);
            multiplyMM(modelViewMatrix, 0, viewMatrix, 0, skybox_modelMatrix, 0);
            multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
            mSkyBox.draw(viewProjectionMatrix);
        }

        drawFrame(gl10, projectionMatrix, viewMatrix);
    }

    protected abstract BaseSkyBox getSkyBox();

    protected abstract void setLookMatrix(float[] viewMatrix);

    protected abstract void updateSkyBoxMatrix(float[] skyboxMatrix);

    protected abstract float getLookAngle();

    protected abstract float getZ_NEAR();

    protected abstract float getZ_FAR();

    protected float[] getSkyBoxMatrix() {
        return skybox_modelMatrix;
    }

    protected float[] getViewMatrix() {
        return viewMatrix;
    }

    protected float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public abstract void surfaceCreated(GL10 gl10, EGLConfig eglConfig);

    public abstract void surfaceChanged(GL10 gl10, int width, int height);

    public abstract void drawFrame(GL10 gl10, float[] projectionMatrix, float[] viewMatrix);

}
