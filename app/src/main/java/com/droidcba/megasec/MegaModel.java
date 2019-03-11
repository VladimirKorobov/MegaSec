package com.droidcba.megasec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.format.Time;
import com.mega.graphics.DrawObjects.*;
import com.mega.graphics.Renderers.IRenderer;

import java.lang.*;
import java.util.ArrayList;

public class MegaModel extends DrawingModel{

    enum State {Init, Start, Stop}
    private Context context;
    private TransformObject secondArrow;
    private TransformObject minuteArrow;

    private float secondAngle = 0;
    private float minuteAngle = 0;

    private float centerX;
    private float centerY;

    private long milliSeconds = 0;
    private long frozenTime = 0;

    private State state = State.Init;
    private RectF knobRect;
    private RectF resetKnobRect;

    private ArrayList<BitmapObject> digits;
    private BitmapObject digits_point;
    private BitmapObject digits_dpoint;
    private ObjectSequence record;


    RectF getKnobRect() {
        return knobRect;
    }

    RectF getResetKnobRect() {
        return resetKnobRect;
    }

    private Time today = new Time(Time.getCurrentTimezone());

    MegaModel(Context context) {
        this.context = context;
    }

    void Start() {
        if(state == State.Start)
        {
            // Freeze
            frozenTime = System.currentTimeMillis() - milliSeconds;
            state = State.Stop;
            WatchThread.setQuant(100);
        }
        else {
            // start
            milliSeconds = System.currentTimeMillis() - frozenTime;
            state = State.Start;
            WatchThread.setQuant(10);
        }
    }

    void Reset() {
        state = State.Init;
        milliSeconds = 0;
        frozenTime = 0;
    }

    private void LoadDigits() {
        digits = new ArrayList<BitmapObject>();
        digits.add( new BitmapObject(context, R.drawable.digit0));
        digits.add( new BitmapObject(context, R.drawable.digit1));
        digits.add( new BitmapObject(context, R.drawable.digit2));
        digits.add( new BitmapObject(context, R.drawable.digit3));
        digits.add( new BitmapObject(context, R.drawable.digit4));
        digits.add( new BitmapObject(context, R.drawable.digit5));
        digits.add( new BitmapObject(context, R.drawable.digit6));
        digits.add( new BitmapObject(context, R.drawable.digit7));
        digits.add( new BitmapObject(context, R.drawable.digit8));
        digits.add( new BitmapObject(context, R.drawable.digit9));

        digits_point = new BitmapObject(context, R.drawable.digitp);
        digits_dpoint = new BitmapObject(context, R.drawable.digitdp);
    }

    private void AddRecord(float width, float height, float scale) {
        record = new ObjectSequence();
        scale *= 0.7f;
        ScaleObject scaleObject = new ScaleObject(scale, scale, record);
        OffsetObject offsetObject = new OffsetObject(width * 0.1f, height * 0.9f, scaleObject);
        this.Add(offsetObject);
    }
    private float AddWatch(float width, float height, DrawingModel model) {
        // Add case
        BitmapObject bitmap = new BitmapObject(context, R.drawable.watchcase);

        double kx = width / bitmap.getBitmap().getWidth();
        double ky = height / bitmap.getBitmap().getHeight();
        float k = (float) ((kx < ky) ? kx : ky);
        //k *= 0.8f;

        TransformObject transformObject = new TransformObject(bitmap);
        transformObject.getMatrix().postScale(k, k);

        RectF rect = transformObject.getRect();

        centerX = (width < height) ? width / 2 : height / 2;
        //centerX *= 0.8f;
        centerY = height - rect.height() / 2;

        float topBitmap = centerY - rect.height() / 2;

        // knob width
        float knobWidth = width / 4;
        // knob buttom
        PointF knobBottom = new PointF(0, width / 2);

        // reset knob
        PointF resetKnobBottom = new PointF();
        float sin = (float)Math.sin(Math.toRadians(35));
        float cos = (float)Math.cos(Math.toRadians(35));
        resetKnobBottom.x = knobBottom.x * cos - knobBottom.y * sin;
        resetKnobBottom.y = -knobBottom.x * sin + knobBottom.y * cos;

        knobRect = new RectF(-knobWidth/2, -knobWidth/2, knobWidth/2, knobWidth/2);
        knobRect.offset(width/2, height / 2 - width / 2);
        resetKnobRect = new RectF(-knobWidth/2, -knobWidth/2, knobWidth/2, knobWidth/2);
        resetKnobRect.offset(width/2, height/2);
        resetKnobRect.offset(resetKnobBottom.x, -resetKnobBottom.y);



        //knobRect = new RectF(width / 4, topBitmap, 3 * width / 4, topBitmap + rect.height() / 5.0f);
        transformObject.getMatrix().postTranslate(centerX - rect.width() / 2, topBitmap);

        model.Add(transformObject);

        return k;
    }

    private void AddArrows(float width, float height, float factor) {

        // Minute array
        BitmapObject bitmapMinuteArrow = new BitmapObject(context, R.drawable.minutearrow);
        TransformObject transformObjectMinuteArrow = new TransformObject(bitmapMinuteArrow);
        transformObjectMinuteArrow.getMatrix().postScale(factor, factor);
        RectF rect = transformObjectMinuteArrow.getRect();
        float objectXCorner = centerX - rect.width() / 2;
        float objectYCorner = centerY - rect.height();
        transformObjectMinuteArrow.getMatrix().postTranslate(objectXCorner, objectYCorner);
        minuteArrow = new TransformObject(transformObjectMinuteArrow);
        this.Add(minuteArrow);
        // Second array
        BitmapObject bitmapSecondArrow = new BitmapObject(context, R.drawable.secondarrow);
        TransformObject transformObjectSecondArrow = new TransformObject(bitmapSecondArrow);
        transformObjectSecondArrow.getMatrix().postScale(factor, factor);
        rect = transformObjectSecondArrow.getRect();
        objectXCorner = centerX - rect.width() / 2;
        objectYCorner = centerY - rect.height();
        transformObjectSecondArrow.getMatrix().postTranslate(objectXCorner, objectYCorner);
        secondArrow = new TransformObject(transformObjectSecondArrow);
        this.Add(secondArrow);

        // Add knob
        BitmapObject bitmapKnob = new BitmapObject(context, R.drawable.smallknob);
        TransformObject transformObject = new TransformObject(bitmapKnob);
        transformObject.getMatrix().postScale(factor, factor);
        rect = transformObject.getRect();
        objectXCorner = centerX - rect.width() / 2;
        objectYCorner = centerY - rect.height() / 2;
        transformObject.getMatrix().postTranslate(objectXCorner, objectYCorner);
        this.Add(transformObject);
    }
    @Override
    public void Create(float width, float height) {
        LoadDigits();
        this.model.clear();
        BitmapObject bitmapObject = new BitmapObject(width, height, Bitmap.Config.ARGB_8888);
        IRenderer bitmapRenderer = bitmapObject.CreateRenderer(context);
        DrawingModel bitmapModel = new DrawingModel();

        RectObject background = new RectObject();
        background.setLeft(0);
        background.setTop(0);
        background.setRight(width);
        background.setBottom(height);
        background.setColor(0xFF000000);
        bitmapModel.Add(background);

        //AddKnob(width, height, bitmapModel);
        float factor = AddWatch(width, height, bitmapModel);
        bitmapModel.Draw(bitmapRenderer);

        this.Add(bitmapObject);
        bitmapModel.Dispose();

        AddArrows(width, height, factor);
        AddRecord(width, height, factor);
    }

    private void UpdateRecord(long hours, long minutes, long seconds, long hundreds) {
        this.record.getModel().clear();
        // Add hours (up to 99 as for now
        int index = (int)(hours / 10);
        this.record.getModel().add(digits.get(index));
        index = (int)(hours % 10);
        this.record.getModel().add(digits.get(index));
        this.record.getModel().add(digits_dpoint);
        // Add minutes
        index = (int)(minutes / 10);
        this.record.getModel().add(digits.get(index));
        index = (int)(minutes % 10);
        this.record.getModel().add(digits.get(index));
        this.record.getModel().add(digits_dpoint);
        // Add seconds
        index = (int)(seconds / 10);
        this.record.getModel().add(digits.get(index));
        index = (int)(seconds % 10);
        this.record.getModel().add(digits.get(index));
        this.record.getModel().add(digits_point);
        // Add hundreds
        index = (int)(hundreds / 10);
        this.record.getModel().add(digits.get(index));
        index = (int)(hundreds % 10);
        this.record.getModel().add(digits.get(index));
    }

    @Override
    public void Draw(IRenderer renderer) {
        if(state == State.Start)
        {
            long time = System.currentTimeMillis() - milliSeconds;
            long hundreds = (time / 10) % 100;
            long seconds = (time / 1000) % 60;
            long minuts = (time / (60 * 1000)) % 60;
            long hours = (time / (3600 * 1000));

            secondAngle = (time % (60 * 1000)) / (60.0f * 1000) * 360;
            minuteAngle = ((time / (60.0f * 60 * 1000)) % 60) * 360;

            UpdateRecord(hours, minuts, seconds, hundreds);
        }
        else if(state == State.Init)
        {
            secondAngle = 0;
            minuteAngle = 0;
            this.record.getModel().clear();
        }
        secondArrow.getMatrix().reset();
        secondArrow.getMatrix().postRotate(secondAngle, centerX, centerY);
        minuteArrow.getMatrix().reset();
        minuteArrow.getMatrix().postRotate(minuteAngle, centerX, centerY);

        super.Draw(renderer);
    }
}
