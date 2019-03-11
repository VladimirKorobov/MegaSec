package com.droidcba.megasec.Views;

import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;
import com.droidcba.megasec.MainActivity;
import com.mega.graphics.DrawObjects.DrawingModel;
import com.mega.graphics.Views.ViewSurface2D;

/**
 * Created by Vladimir on 31.03.2016.
 */
public class View2D extends ViewSurface2D{
    public View2D(Context context, DrawingModel model) {
        super(context, model);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if(m.getActionMasked() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();

            RectF rect = ((MainActivity) this.getContext()).getKmobRect();

            if(rect.left <= x && rect.right >= x && rect.top <= y && rect.bottom >= y) {
                ((MainActivity) this.getContext()).Start();
            }
            else {
                rect = ((MainActivity) this.getContext()).getResetKmobRect();
                if(rect.left <= x && rect.right >= x && rect.top <= y && rect.bottom >= y) {
                    ((MainActivity) this.getContext()).Reset();
                }
            }
        }
        return true;
    }
}
