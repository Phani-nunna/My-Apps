package com.svaad.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoRegular extends TextView{
	public RobotoRegular(Context context) {
        super(context);
        style(context);
    }

    public RobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public RobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf");
        setTypeface(tf);
    }

}
