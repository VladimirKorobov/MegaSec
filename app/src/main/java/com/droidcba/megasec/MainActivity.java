package com.droidcba.megasec;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import com.droidcba.megasec.Views.View2D;
import com.droidcba.megasec.Views.ViewGL;
import com.mega.graphics.Views.IViewSurface;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends Activity{

    static Handler handler;
    WatchThread mainThread;
    private MegaModel megaModel;
    private IViewSurface view;

    private final Lock lock = new ReentrantLock();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        megaModel = new MegaModel(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        view = new ViewGL(this, megaModel);
        //view = new View2D(this, megaModel);

        LinearLayout layout = findViewById(R.id.mainLayout);
        layout.addView(view.getView());

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        handler = new Handler() {
            @Override
            public void
            handleMessage(Message msg)
            {
                {
                    lock.lock();
                    try
                    {
                        if(msg.what == 1)
                        {
                            view.Invalidate();
                        }
                    }
                    finally
                    {
                        lock.unlock();
                    }
                }
            }
        };
        mainThread = new WatchThread(handler);
        mainThread.start();
    }

    public RectF getKmobRect() {
        return megaModel.getKnobRect();
    }

    public RectF getResetKmobRect() {
        return megaModel.getResetKnobRect();
    }

    public void Start() {
        megaModel.Start();
    }
    public void Reset() {
        megaModel.Reset();
    }


    private void EndMainThread() {
        mainThread.EndThread();
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
        }
    }
    public void CloseApp() {
        EndMainThread();
        finish();
    }

    @Override
    public void onDestroy() {
        EndMainThread();
        super.onDestroy();
    }
}
