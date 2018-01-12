
package com.example.administrator.a2rectangle1line;


import com.example.administrator.a2rectangle1line.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
/**
 * Created by administrator on 10/1/18.
 */

//scale detector



public class DrawBox extends View {

    private static final String TAG = "DrawBox";
    //LayoutInflater li;
    Activity cont;

    //used for detecting doubletap before drag
    boolean doubletap_flag=false;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector gestureDetector;


    private float mScaleFactor = 1.f;

    /** Main bitmap */

    //private Bitmap mBitmap = null;

    //private Rect mMeasuredRect;


    List<BoxArea> box_relation_list = new ArrayList<BoxArea>();
    String box_relation_type="";
    /** Stores data about single box */
    //changed to public
    public static class BoxArea {
        float x1, x2, y1, y2;
        int index;

        BoxArea(float x1, float x2, float y1, float y2, int index) {
            this.x1=x1;
            this.x2=x2;
            this.y1=y1;
            this.y2=y2;
            this.index=index;
        }

        List<Line> ass_list = new ArrayList<Line>();
        List<BoxArea> comp_list = new ArrayList<BoxArea>();

        List<BoxArea> inh_list = new ArrayList<BoxArea>();
        List<BoxArea> rea_list = new ArrayList<BoxArea>();

        BoxArea parent = null;



        @Override
        public String toString() {
            return "Box[" + x1 + ", " + x2 + ", " + y1 + ", " + y2 + "]";
        }
    }


    public static class Line {
        float x1, x2, y1, y2;

        Line(float x1, float x2, float y1, float y2) {
            this.x1=x1;
            this.x2=x2;
            this.y1=y1;
            this.y2=y2;
        }

        @Override
        public String toString() {
            return "Line[" + x1 + ", " + x2 + ", " + y1 + ", " + y2 + "]";
        }
    }

    /** Paint to draw boxes */
    private Paint mBoxPaint;

    //private final Random mRadiusGenerator = new Random();
    //?????

    // Radius limit in pixels
    //private final static int RADIUS_LIMIT = 100;

    //private static final int BOXES_LIMIT = 3;
    public static final int BOXES_LIMIT = 100;
    public static final int LINES_LIMIT = 100;

    /** All available boxes */
    //changed to public
    public HashSet<BoxArea> mBoxes = new HashSet<BoxArea>(BOXES_LIMIT);
    public HashSet<Line> mLines = new HashSet<Line>(LINES_LIMIT);

  //  public HashMap<BoxArea, BoxArea> mParents = new HashMap<BoxArea, BoxArea>(BOXES_LIMIT)

    //public SparseArray<BoxArea> mBoxPointer = new SparseArray<BoxArea>(BOXES_LIMIT);

    /**
     * Default constructor
     *
     * @param ct {@link android.content.Context}
     */
    public DrawBox(final Context ct) {
        super(ct);

        init(ct);
    }

    public DrawBox(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);

        init(ct);
    }

    public DrawBox(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);

        init(ct);
    }

    private void init(final Context ct) {
        // Generate bitmap used for background
        //mBitmap = BitmapFactory.decodeResource(ct.getResources(), R.drawable.rectangle);


        cont = (Activity)ct;
        mBoxPaint = new Paint();

        mBoxPaint.setColor(Color.BLUE);
        mBoxPaint.setStrokeWidth(10);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setTextSize(20);

        mScaleDetector=new ScaleGestureDetector(ct, new ScaleListener());
        gestureDetector=new GestureDetector(ct, new GestureListener());

    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(doubletap_flag==false) {
                System.out.println("bsdk1");

                mScaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
                System.out.println("bsdk2");

                invalidate();
            }
            return true;
        }
    }

    ////this gesture listener used for 1. double tap
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX()/mScaleFactor;
            float y = e.getY()/mScaleFactor;

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            doubletap_flag=true;
            return true;
        }
    }


    @Override
    public void onDraw(final Canvas canv) {

        canv.save();
        //canv.translate(mScaleFactor, mScaleFactor);
        canv.scale(mScaleFactor, mScaleFactor, 0, 0);

        // background bitmap to cover all area
        //canv.drawBitmap(mBitmap, null, mMeasuredRect, null);

        canv.drawRect(canv.getHeight(), 0, canv.getWidth(), 0, mBoxPaint);

        for (BoxArea box : mBoxes) {
            //canv.drawCircle(circle.centerX, circle.centerY, circle.radius, mBoxPaint);//????
            canv.drawRect(box.x1, box.y1, box.x2, box.y2 ,mBoxPaint);
            canv.drawText(Integer.toString(box.index), box.x1 + 50, box.y1 + 50, mBoxPaint);

        }

        for (Line l : mLines) {
            canv.drawLine(l.x1, l.y1, l.x2, l.y2 ,mBoxPaint);
        }

        canv.restore();

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = true;
        BoxArea touchedBox;
        float yTouch;
        float xTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();
        float oldcentrex, oldcentrey;

        mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);


        System.out.println("add1");


        // get touch event coordinates and make transparent box from it
        //if(!mScaleDetector.isInProgress()) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                //clearBoxPointer();//?????

                System.out.println("add2");

                xTouch = event.getX(0)/mScaleFactor;
                yTouch = event.getY(0)/mScaleFactor;

                // check if we've touched inside some box
                touchedBox = obtaintouchedBox(xTouch, yTouch); ////????????
                if (touchedBox != null) {
                    System.out.println("add3");
                    //setContentView(R.layout.activity_main);
                    //Layout l = R.layout.activity_main
                    //LinearLayout ll1 = (LinearLayout) li.inflate(R.layout.activity_main, null);
                    //View v = (View) findViewById(R.id.);
                    Button bin_ass_btn = (Button) cont.findViewById(R.id.bin_ass_btn);
                    Button bin_comp_btn = (Button) cont.findViewById(R.id.bin_comp_btn);

                    //Button bin_ass_btn=(Button)findViewById(R.id.bin_ass_btn);
                    if (bin_ass_btn.isClickable() == false) {
                        System.out.println("add4");
                        box_relation_list.add(touchedBox);
                        if (box_relation_list.size() == 2) {
                            System.out.println("add5");
                            bin_ass_btn.setClickable(true);
                            create_line_between_two_boxes(box_relation_list);
                        }
                        invalidate();
                        handled = true;

                    } else if (bin_comp_btn.isClickable() == false) {
                        System.out.println("add4");
                        box_relation_list.add(touchedBox);
                        if (box_relation_list.size() == 2) {
                            System.out.println("add5");
                            bin_comp_btn.setClickable(true);
                            create_box_inside_box(box_relation_list);
                        }
                        invalidate();
                        handled = true;

                    }
                    else {
                        System.out.println("add6");
                        oldcentrex = (touchedBox.x1 + touchedBox.x2) / 2;
                        /*oldcentrey = (touchedBox.y1 + touchedBox.y2) / 2;
                        touchedBox.x1 = touchedBox.x1 + (xTouch - oldcentrex);
                        touchedBox.x2 = touchedBox.x2 + (xTouch - oldcentrex);
                        touchedBox.y1 = touchedBox.y1 + (yTouch - oldcentrey);
                        touchedBox.y2 = touchedBox.y2 + (yTouch - oldcentrey);

                        //
                        for (Line l : touchedBox.ass_list) {
                            if (l.x1 == oldcentrex) {
                                l.x1 = (touchedBox.x1 + touchedBox.x2) / 2;
                                l.y1 = (touchedBox.y1 + touchedBox.y2) / 2;
                            } else if (l.x2 == oldcentrex) {
                                l.x2 = (touchedBox.x1 + touchedBox.x2) / 2;
                                l.y2 = (touchedBox.y1 + touchedBox.y2) / 2;
                            }
                        }
                        //

                        //
                        print_boxes_inside_box(touchedBox, xTouch-oldcentrex, yTouch-oldcentrey);
                        //

                        //mBoxPointer.put(event.getPointerId(0), touchedBox);

                        invalidate();*/
                        handled = true;
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check boxes
                pointerId = event.getPointerId(actionIndex);

                System.out.println("add7");

                xTouch = event.getX(actionIndex)/mScaleFactor;
                yTouch = event.getY(actionIndex)/mScaleFactor;

                // check if we've touched inside some box
                touchedBox = obtaintouchedBox(xTouch, yTouch);
                if (touchedBox != null) {

                    System.out.println("add8");

                    //mBoxPointer.put(pointerId, touchedBox); //???why put box in pointerlist before changing x1 x2 y1 y2 values????

                    //
                    oldcentrex = (touchedBox.x1 + touchedBox.x2) / (2);
                    oldcentrey = (touchedBox.y1 + touchedBox.y2) / (2);
//                    touchedBox.x1 = touchedBox.x1 + (xTouch - oldcentrex);
//                    touchedBox.x2 = touchedBox.x2 + (xTouch - oldcentrex);
//                    touchedBox.y1 = touchedBox.y1 + (yTouch - oldcentrey);
//                    touchedBox.y2 = touchedBox.y2 + (yTouch - oldcentrey);
/*
                    if (touchedBox.parent!=null) {
                        if (touchedBox.x1 + (xTouch - oldcentrex) >= touchedBox.parent.x1 && touchedBox.x2 + (xTouch - oldcentrex) <= touchedBox.parent.x2 && touchedBox.y1 + (yTouch - oldcentrey) <= touchedBox.parent.y1 && touchedBox.y2 + (yTouch - oldcentrey) >= touchedBox.parent.y2) {
                            touchedBox.x1 = touchedBox.x1 + (xTouch - oldcentrex);
                            touchedBox.x2 = touchedBox.x2 + (xTouch - oldcentrex);
                            touchedBox.y1 = touchedBox.y1 + (yTouch - oldcentrey);
                            touchedBox.y2 = touchedBox.y2 + (yTouch - oldcentrey);
                        }
                    }
                    else {*/
                        touchedBox.x1 = touchedBox.x1 + (xTouch - oldcentrex);
                        touchedBox.x2 = touchedBox.x2 + (xTouch - oldcentrex);
                        touchedBox.y1 = touchedBox.y1 + (yTouch - oldcentrey);
                        touchedBox.y2 = touchedBox.y2 + (yTouch - oldcentrey);
                    //
                    //
                    for (Line l : touchedBox.ass_list) {
                        if (l.x1 == oldcentrex) {
                            l.x1 = (touchedBox.x1 + touchedBox.x2) / 2;
                            l.y1 = (touchedBox.y1 + touchedBox.y2) / 2;
                        } else if (l.x2 == oldcentrex) {
                            l.x2 = (touchedBox.x1 + touchedBox.x2) / 2;
                            l.y2 = (touchedBox.y1 + touchedBox.y2) / 2;
                        }
                    }
                    //

                    //
                    print_boxes_inside_box(touchedBox, xTouch-oldcentrex, yTouch-oldcentrey);
                    //

                    invalidate();
                    handled = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                System.out.println("add9");
                Log.w(TAG, "Move");

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex);

                    System.out.println("add10");

                    xTouch = event.getX(actionIndex)/mScaleFactor;
                    yTouch = event.getY(actionIndex)/mScaleFactor;

                    //touchedBox = mBoxPointer.get(pointerId);
                    touchedBox = obtaintouchedBox(xTouch, yTouch);

                    if (null != touchedBox && doubletap_flag == true) {

                        //for composition
                        if (box_relation_list.isEmpty())
                        {
                            box_relation_list.add(touchedBox);
                            box_relation_type="comp";
                        }
                        /*
                        if(!box_relation_list.contains(touchedBox) && box_relation_list.size()<2 && box_relation_type!="ass") {
                            box_relation_list.add(touchedBox);
                            System.out.println(box_relation_list.toString());
                            box_relation_type = "comp";
                        }
                        if (box_relation_list.size()==2 && box_relation_type == "comp") {
                            create_box_inside_box(box_relation_list);
                            System.out.println(box_relation_list.toString());
                        }
*/
                        //
                        System.out.println("add11");
                        oldcentrex = (touchedBox.x1 + touchedBox.x2) / 2;
                        oldcentrey = (touchedBox.y1 + touchedBox.y2) / 2;
                        /*if (touchedBox.parent!=null) {
                            if (touchedBox.x1 + (xTouch - oldcentrex) >= touchedBox.parent.x1 && touchedBox.x2 + (xTouch - oldcentrex) <= touchedBox.parent.x2 && touchedBox.y1 + (yTouch - oldcentrey) <= touchedBox.parent.y1 && touchedBox.y2 + (yTouch - oldcentrey) >= touchedBox.parent.y2) {
                                touchedBox.x1 = touchedBox.x1 + (xTouch - oldcentrex);
                                touchedBox.x2 = touchedBox.x2 + (xTouch - oldcentrex);
                                touchedBox.y1 = touchedBox.y1 + (yTouch - oldcentrey);
                                touchedBox.y2 = touchedBox.y2 + (yTouch - oldcentrey);
                            }
                        }
                        else {*/
                            touchedBox.x1 = touchedBox.x1 + (xTouch - oldcentrex);
                            touchedBox.x2 = touchedBox.x2 + (xTouch - oldcentrex);
                            touchedBox.y1 = touchedBox.y1 + (yTouch - oldcentrey);
                            touchedBox.y2 = touchedBox.y2 + (yTouch - oldcentrey);
//                        if(touchedBox.parent != null){
//                            if (touchedBox.x1 < touchedBox.parent.x1){
//                                touchedBox.x1 = touchedBox.parent.x1;
//                                if (touchedBox.y1 < touchedBox.parent.y1){
//                                    touchedBox.y1 = touchedBox.parent.y1;
//                                }
//                                //create x2, y2
//                            }
//                            touchedBox.x1 = Math.min(touchedBox.x1, touchedBox.parent.x1);
//                            touchedBox.x2 = Math.max(touchedBox.x2, touchedBox.parent.x2);
//                            touchedBox.y1 = Math.min(touchedBox.y1, touchedBox.parent.y1);
//                            touchedBox.y2 = Math.max(touchedBox.y2, touchedBox.parent.y2);


                        /* BoxArea alredayBox = obtaintouchedBox((touchedBox.x1+touchedBox.x2)/2, (touchedBox.y1+touchedBox.y2)/2);
                        {
                            box_relation_list.add(alredayBox);
                            box_relation_list.add(touchedBox);
                            {
                                System.out.println("add5");
                                //bin_ass_btn.setClickable(true);
                                create_line_between_two_boxes(box_relation_list);
                            }
                        }*/


                        //

                        //
                        for (Line l : touchedBox.ass_list) {
                            if (l.x1 == oldcentrex) {
                                l.x1 = (touchedBox.x1 + touchedBox.x2) / 2;
                                l.y1 = (touchedBox.y1 + touchedBox.y2) / 2;
                            } else if (l.x2 == oldcentrex) {
                                l.x2 = (touchedBox.x1 + touchedBox.x2) / 2;
                                l.y2 = (touchedBox.y1 + touchedBox.y2) / 2;
                            }
                        }
                        //

                        //
                        print_boxes_inside_box(touchedBox, xTouch - oldcentrex, yTouch - oldcentrey);
                        //

                        handled = false;
                    } else if (null != touchedBox && doubletap_flag == false) {

                        if (box_relation_list.isEmpty())
                        {
                            box_relation_list.add(touchedBox);
                            box_relation_type="ass";
                        }
                        /*
                        if(!box_relation_list.contains(touchedBox) && box_relation_list.size()<2 && box_relation_type!="comp") {
                            box_relation_list.add(touchedBox);
                            System.out.println(box_relation_list.toString());
                            box_relation_type = "ass";
                        }
                        if (box_relation_list.size()==2 && box_relation_type == "ass")
                        {
                            create_line_between_two_boxes(box_relation_list);
                            System.out.println(box_relation_list.toString());
                        }*/

                        /*oldcentrex = (touchedBox.x1 + touchedBox.x2) / 2;
                        oldcentrey = (touchedBox.y1 + touchedBox.y2) / 2;
                        float a  = touchedBox.x1 + (xTouch - oldcentrex);
                        float b = touchedBox.x2 + (xTouch - oldcentrex);
                        float c = touchedBox.y1 + (yTouch - oldcentrey);
                        float d = touchedBox.y2 + (yTouch - oldcentrey);


                        BoxArea alreadyBox = obtaintouchedBox((a+b)/2, (c+d)/2);
                        box_relation_list.add(alreadyBox);
                        {
                            System.out.println("add5");
                            //bin_ass_btn.setClickable(true);
                            create_line_between_two_boxes(box_relation_list);
                        }*/

                        handled = true;
                    }
                    else if (null == touchedBox && doubletap_flag==false)
                    {
                        handled=true;
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                System.out.println("add12");
                //clearBoxPointer();


                xTouch = event.getX(actionIndex)/mScaleFactor;
                yTouch = event.getY(actionIndex)/mScaleFactor;
                BoxArea touchedBox1 = obtaintouchedBox(xTouch, yTouch);

                if (doubletap_flag==true)
                    doubletap_flag=false;

                if(box_relation_type=="comp" && touchedBox1!=null && touchedBox1!=box_relation_list.get(0))
                {
                    box_relation_list.add(touchedBox1);
                    create_box_inside_box(box_relation_list);
                }

                if(box_relation_type=="ass" && touchedBox1!=null && touchedBox1!=box_relation_list.get(0))
                {
                    box_relation_list.add(touchedBox1);
                    create_line_between_two_boxes(box_relation_list);

                }

                box_relation_list.clear();
                box_relation_type="";
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // not general pointer was up
                System.out.println("add13");
                pointerId = event.getPointerId(actionIndex);

                //mBoxPointer.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                System.out.println("add14");
                handled = true;
                break;

            default:
                // do nothing
                break;
        }

        //return super.onTouchEvent(event) || handled;
        return handled;
    }

    /**
     * Search and creates new (if needed) box based on touch area
     *
     * @param xTouch int x of touch
     * @param yTouch int y of touch
     *
     * @return obtained {@link BoxArea}
     */
    private BoxArea obtaintouchedBox(final float xTouch, final float yTouch) {
        BoxArea touchedBox = gettouchedBox(xTouch, yTouch);

        /*if (null == touchedBox) {
            //touchedBox = new BoxArea(xTouch, yTouch, mRadiusGenerator.nextInt(RADIUS_LIMIT) + RADIUS_LIMIT);
            touchedBox = new BoxArea(xTouch-100, xTouch+100, yTouch+50, yTouch-50);


            if (mBoxes.size() == BOXES_LIMIT) {
                Log.w(TAG, "Clear all boxes, size is " + mBoxes.size());
                // remove first box
                mBoxes.clear();
            }

            Log.w(TAG, "Added box " + touchedBox);
            mBoxes.add(touchedBox);
        }*/

        return touchedBox;
    }

    private void create_line_between_two_boxes(List<BoxArea> box_relation_list) {

        System.out.println("add111");
        //find centres of boxes in the list
        float box1_centre_x = (box_relation_list.get(0).x1 + box_relation_list.get(0).x2)/2;
        float box1_centre_y = (box_relation_list.get(0).y1 + box_relation_list.get(0).y2)/2;
        float box2_centre_x = (box_relation_list.get(1).x1 + box_relation_list.get(1).x2)/2;
        float box2_centre_y = (box_relation_list.get(1).y1 + box_relation_list.get(1).y2)/2;
        System.out.println("add122");


        //create line
        Line l = new Line(box1_centre_x, box2_centre_x, box1_centre_y, box2_centre_y);
        System.out.println("add133");

        box_relation_list.get(0).ass_list.add(l);
        box_relation_list.get(1).ass_list.add(l);

        mLines.add(l);
        System.out.println("add34");

        //empty list
        box_relation_list.clear();
        doubletap_flag=false;

    }

    private void create_box_inside_box(List<BoxArea> box_relation_list) {
        System.out.println("add111");

        //save oldcentre of get(1) box(child)
        float oldcentrex = (box_relation_list.get(0).x1 + box_relation_list.get(0).x2)/2;
        //float oldcentrey = (box_relation_list.get(1).y1 + box_relation_list.get(1).y2)/2;

        //find centres of boxes in the list
        box_relation_list.get(0).x1 = box_relation_list.get(1).x1;
        box_relation_list.get(0).y1 = box_relation_list.get(1).y1;
        box_relation_list.get(0).x2 = box_relation_list.get(1).x1 + (box_relation_list.get(1).x2 - box_relation_list.get(1).x1)/2;
        box_relation_list.get(0).y2 = box_relation_list.get(1).y2 + (box_relation_list.get(1).y1 - box_relation_list.get(1).y2)/2;

        for (Line l: box_relation_list.get(0).ass_list){
                if (l.x1 == oldcentrex) {
                    l.x1 = (box_relation_list.get(0).x1 + box_relation_list.get(0).x2) / 2;
                    l.y1 = (box_relation_list.get(0).y1 + box_relation_list.get(0).y2) / 2;
                } else if (l.x2 == oldcentrex) {
                    l.x2 = (box_relation_list.get(0).x1 + box_relation_list.get(0).x2) / 2;
                    l.y2 = (box_relation_list.get(0).y1 + box_relation_list.get(0).y2) / 2;
            }
        }

        box_relation_list.get(1).comp_list.add(box_relation_list.get(0));

        box_relation_list.get(0).parent = box_relation_list.get(1);

       // mParents.put(box_relation_list.get(0), box_relation_list.get(1));

        //empty list
        box_relation_list.clear();
    }



    /**
     * Determines touched box
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return {@link BoxArea} touched circle or null if no circle has been touched
     */
    private BoxArea gettouchedBox(final float xTouch, final float yTouch) {
        BoxArea touched = null;

        for (BoxArea box : mBoxes) {
            if (box.x1 < xTouch && box.x2 > xTouch && box.y1 < yTouch && box.y2 > yTouch) {
                //if ((circle.centerX - xTouch) * (circle.centerX - xTouch) + (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius * circle.radius) {
                touched = box;
                break;
            }
        }

        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("lkb1");
        //mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        //mMeasuredRect = new Rect(0, 0, 200, 200);
    }


    private void print_boxes_inside_box(BoxArea touchedBox, final float x, final float y){
        for (BoxArea b : touchedBox.comp_list)
        {
            float oldcentrex = (b.x1 + b.x2) / 2;
            //float oldcentrey = (b.y1 + b.y2) / 2;

            b.x1 = b.x1 + x;
            b.x2 = b.x2 + x;
            b.y1 = b.y1 + y;
            b.y2 = b.y2 + y;

            for (Line l: b.ass_list){
                if (l.x1 == oldcentrex) {
                    l.x1 = (b.x1 + b.x2) / 2;
                    l.y1 = (b.y1 + b.y2) / 2;
                } else if (l.x2 == oldcentrex) {
                    l.x2 = (b.x1 + b.x2) / 2;
                    l.y2 = (b.y1 + b.y2) / 2;
                }
            }

            print_boxes_inside_box(b, x, y);
        }
    }

}


/////ERRORS: BLOCK INSIDE BLOCK IS NOT WORKING SOMETIMES
///TO CONNECT ARROW TO INSIDE BLOCK??
//WHEN WE MOVE INSIDE BLOCK OUTSIDE, THE MOVEMENT BECOMES HAYWIRE. SO A TO B COMPOSITION MEANS B TO A COMPOSITION CANNOT EXIST