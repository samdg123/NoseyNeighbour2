package com.example.noseyneighbour.UI_Elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.noseyneighbour.Activities.GraphActivity;
import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.example.noseyneighbour.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CrimeGraph extends View {

    //int[] in format of int[0] = number of crimes, int[1] = year of crimes, int[2] = month of crimes
    private ArrayList<int[]> numCrimesList;
    private int graphWidth;
    private int graphHeight;
    private int xRangeMonths;
    private int xStartValue;
    private int yRange;
    private int yStartValue;
    private int padding;
    private @ColorInt
    int backgroundColour;
    private Context context;
    private final int firstYear = 2015;

    @Override
    protected void onDraw(Canvas canvas) {
        padding = 32;
        setNumCrimesList();
        Paint axisPaint = new Paint();

        axisPaint.setColor(Color.BLUE);
        axisPaint.setStrokeWidth(16);

        this.setBackgroundColor(backgroundColour);

        plotGraphAxis(canvas, axisPaint);

        setXRange();

        addPoints(canvas);
    }

    private void addPoints(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(32);

        float x;
        float y;
        float prevX = 0;
        float prevY = 0;
        final float pointRadius = 16;

        for(int[] point : numCrimesList){
            x = calcPointX(point);
            y = canvas.getHeight()-calcPointY(point);

            if(prevX != 0 && prevY != 0) {
                connectPoints(x, y, prevX, prevY, canvas);
                canvas.drawCircle(prevX, prevY, pointRadius, paint);
                canvas.drawCircle(x, y, pointRadius, paint);
                //canvas.drawPoint(prevX, prevY, paint);
                //canvas.drawPoint(x, y, paint);
            } else {
                canvas.drawCircle(x, y, pointRadius, paint);
            }

            prevX = x;
            prevY = y;
        }
    }

    private void connectPoints(float x, float y, float prevX, float prevY, Canvas canvas){
        Paint paint = new Paint();
        paint.setStrokeWidth(16);
        paint.setColor(Color.BLUE);

        //Remember y=0 is the top of the canvas
        if (y < prevY){
            paint.setColor(Color.GREEN);
        } else if (y > prevY) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.GRAY);
        }

        canvas.drawLine(prevX, prevY, x, y, paint);
    }

    private float calcPointY(int[] point){
        float y = padding;

        y += Math.round((graphHeight/yRange) * (point[0]-yStartValue));

        return y;
    }

    private float calcPointX(int[] point){
        float x;
        int monthsSinceStart;

        monthsSinceStart = ((point[1] * 12) + point[2]) - xStartValue;

        x = padding + (monthsSinceStart*graphWidth)/xRangeMonths;

        return x;
    }

    //private void setYRangeKG(){
    //    DBHandler dbHandler = new DBHandler(context);
    //
    //    yRangeKG = dbHandler.getWeightKGRange();
    //    yStartValue = dbHandler.getMinWeight();
    //}

    private void setXRange(){
        int lastIndex = numCrimesList.size()-1;
        int yearsRange = numCrimesList.get(lastIndex)[1] - numCrimesList.get(0)[1];
        int monthsRange = numCrimesList.get(lastIndex)[2] - numCrimesList.get(0)[2];

        xRangeMonths = (yearsRange*12) + monthsRange;
        xStartValue = (numCrimesList.get(0)[1] * 12) + numCrimesList.get(0)[2];
        Log.d("xrange", "monthrange is " + monthsRange + " and yearsrange is " + yearsRange + "...  and numcrimes is " + lastIndex);
    }

    private void plotGraphAxis(Canvas canvas, Paint paint){
        int stroke = Math.round(paint.getStrokeWidth());

        //adds padding from view border
        graphWidth = canvas.getWidth() - (padding*2);
        graphHeight = canvas.getHeight() - (padding*2);

        //y axis
        canvas.drawLine(padding,padding+graphHeight -(stroke/2),padding,padding, paint);

        //x axis
        canvas.drawLine(padding - (stroke/2),padding+graphHeight,padding+graphWidth,padding+graphHeight, paint);

        //to offset the vertices from axis lines
        graphWidth -= stroke*4;
        graphHeight -= stroke*4;
        padding += stroke*2;
    }

    private void setNumCrimesList(){
        numCrimesList = new ArrayList<>();
        DBHandler dbHandler = new DBHandler(getContext());

        numCrimesList = dbHandler.countCrimesForAllMonths();
        yStartValue = dbHandler.countLowestCrimeMonth();
        yRange = dbHandler.countHighestCrimeMonth() - yStartValue;

        for (int[] crime : numCrimesList) {
            Log.d("CrimeGraph", "number of crimes: " + crime[0] + ", year: " + crime[1] + ", month: " + crime[2]);
        }
    }

    private void init(Context context) {
        this.animate();
        this.context = context;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.color, typedValue, true);
        backgroundColour = typedValue.data;
    }

    public CrimeGraph(Context context) {
        super(context);
        init(context);
    }
    public CrimeGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public CrimeGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public CrimeGraph(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
}
