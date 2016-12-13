package com.creativept.skyboxforopengles;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.creativept.opengl_samle_library.util.CommonUtil;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    private SkyBoxRender skyboxRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glSurfaceView = new GLSurfaceView(this);

        final boolean supportsEs2 =
                CommonUtil.checkIfSupportES2(this);

        final SkyBoxRender skyboxRenderer = new SkyBoxRender(this);

        if (supportsEs2) {
            initView();
            initEvent();
            setContentView(glSurfaceView);
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void initView() {
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);

        skyboxRenderer = new SkyBoxRender(this);
        glSurfaceView.setRenderer(skyboxRenderer);
        rendererSet = true;
    }

    private void initEvent() {
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            float previousX, previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        previousX = event.getX();
                        previousY = event.getY();
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        final float deltaX = event.getX() - previousX;
                        final float deltaY = event.getY() - previousY;

                        previousX = event.getX();
                        previousY = event.getY();

                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                skyboxRenderer.handleTouchDrag(
                                        deltaX, deltaY);
                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
