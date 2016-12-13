package com.creativept.skyboxforopengles;

import android.content.Context;

import com.creativept.opengl_samle_library.shape.BaseTexture;
import com.creativept.opengl_samle_library.util.TextureUtil;

/**
 * 类名
 * 创建时间 2016/12/13
 * 实现的主要功能
 *
 * @author zjc
 */

public class Texture extends BaseTexture {

    public Texture(Context context) {
        super(context);
    }

    @Override
    protected int getTextureId() {
        return TextureUtil.loadTexture(mContext, R.drawable.test);
    }
}
