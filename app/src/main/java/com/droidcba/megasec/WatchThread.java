package com.droidcba.megasec;

import android.os.Handler;
import com.mega.graphics.Threads.MainThread;

public class WatchThread extends MainThread{

    WatchThread(Handler handler) {
        super(handler);
    }
    private static long quant = 100;
    private static long getQuant()
    {
        return quant;
    }
    static void setQuant(long _quant) {
        quant = _quant;
    }

    @Override
    public void ThreadBody(Handler handler) {
        try {
            Thread.sleep(getQuant());
        }
        catch (InterruptedException e) {

        }
        super.ThreadBody(handler);
    }
}
