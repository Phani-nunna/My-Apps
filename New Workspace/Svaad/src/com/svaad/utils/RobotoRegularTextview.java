package com.svaad.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoRegularTextview extends TextView{
	public RobotoRegularTextview(Context context) {
        super(context);
        style(context);
    }

    public RobotoRegularTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context);
    }

    public RobotoRegularTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context);
    }

    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Thin.ttf");
        setTypeface(tf);
    }

}
