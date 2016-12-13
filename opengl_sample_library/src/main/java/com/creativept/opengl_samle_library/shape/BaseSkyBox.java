package com.creativept.opengl_samle_library.shape;

import android.content.Context;
import android.util.Log;

import com.creativept.opengl_samle_library.R;
import com.creativept.opengl_samle_library.util.CommonUtil;
import com.creativept.opengl_samle_library.util.ShaderProgramUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * 类名
 * 创建时间 2016/12/13
 * 实现的主要功能
 *
 * @author zjc
 */

public abstract class BaseSkyBox extends Shape {

    private static final int POSITON_COMPONENT_COUNT = 3;

    private final float[] vertexArray = new float[]{
            -1, 1, 1,//0
            1, 1, 1,//1
            -1, -1, 1,//2
            1, -1, 1,//3
            -1, 1, -1,//4
            1, 1, -1,//5
            -1, -1, -1,//6
            1, -1, -1 //7
    };
    private final byte[] indexArray = {
            //front
            1, 3, 0,
            0, 3, 2,

            //back
            4, 6, 5,
            5, 6, 7,

            //left
            0, 2, 4,
            4, 2, 6,

            //right
            5, 7, 1,
            1, 7, 3,

            //top
            5, 1, 4,
            4, 1, 0,

            //bottom
            6, 2, 7,
            7, 2, 3
    };


    private ByteBuffer indexBuffer;
    private FloatBuffer vertexBuffer;
    private int mPositionHandler;
    private int mMVPMatrixHandler;
    private int uTextureUnitHandler;
    private int mSkyboxTexture;

    public BaseSkyBox(Context context) {
        super(context);
        initData();
    }

    @Override
    protected void createProgram() {
        mProgram = ShaderProgramUtil.newLinkProgram(
                ShaderProgramUtil.loadShader(mContext, GL_VERTEX_SHADER, R.raw.skybox_vertex_shader),
                ShaderProgramUtil.loadShader(mContext, GL_FRAGMENT_SHADER, R.raw.skybox_fragment_shader)
        );
        mPositionHandler = glGetAttribLocation(mProgram, "a_Position");
        mMVPMatrixHandler = glGetUniformLocation(mProgram, "u_Matrix");
        uTextureUnitHandler = glGetUniformLocation(mProgram, "u_TextureUnit");
    }

    @Override
    protected void initData() {
        mSkyboxTexture = getTextureId();
        if (mSkyboxTexture == 0) {
            Log.e("BaseSkyBox", "天空盒纹理未设置");
            return;
        }
        vertexBuffer = CommonUtil.newFloatBuffer(vertexArray);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indexArray.length)
                .order(ByteOrder.nativeOrder())
                .put(indexArray);
        indexBuffer.position(0);

        createProgram();
    }

    private boolean isInit;

    @Override
    public void draw(float[] mMVPMatrix) {
        if (mSkyboxTexture == 0) {
            Log.e("BaseSkyBox", "天空盒纹理未设置成功");
            return;
        }
        //indexArray描绘的指引
        glUseProgram(mProgram);
        glUniformMatrix4fv(mMVPMatrixHandler, 1, false, mMVPMatrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, mSkyboxTexture);
        glUniform1i(uTextureUnitHandler, 0);
        glVertexAttribPointer(mPositionHandler, POSITON_COMPONENT_COUNT,
                GL_FLOAT, false, 0, vertexBuffer);
        glEnableVertexAttribArray(mPositionHandler);
        glDrawElements(GL_TRIANGLES, indexArray.length, GL_UNSIGNED_BYTE, indexBuffer);
    }

    /**
     * 加载六个面的纹理
     *
     * @return 纹理的处理ID
     */
    protected abstract int getTextureId();


}
