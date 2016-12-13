package com.creativept.skyboxforopengles;

import android.content.Context;

import com.creativept.opengl_samle_library.shape.BaseSkyBox;
import com.creativept.opengl_samle_library.util.TextureUtil;

/**
 * Created by season on 2016/12/8.
 */

public class SkyBox extends BaseSkyBox {


    public SkyBox(Context context) {
        super(context);
    }

    @Override
    protected int getTextureId() {
        return TextureUtil.loadCubeMap(mContext,
                new int[]{R.drawable.left, R.drawable.right,
                        R.drawable.bottom, R.drawable.top,
                        R.drawable.front, R.drawable.back});
    }

}
