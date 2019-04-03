package com.example.noseyneighbour.UI_Elements;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.ColorInt;
import es.dmoral.toasty.Toasty;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.noseyneighbour.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class CrimeGraph extends View {

    private final int AXIS_COLOR = Color.LTGRAY;
    private final int POINT_COLOR = Color.WHITE;
    private final int POINT_SIZE = 32;

    private final int TEXT_COLOR = Color.WHITE;
    private final int TEXT_SIZE = 40;
    private final int TEXT_SHADOW_RADIUS = 7;
    private final int TEXT_SHADOW_DX = 0;
    private final int TEXT_SHADOW_DY = 0;
    private final int TEXT_SHADOW_COLOR = Color.BLACK;

    //int[] in format of int[0] = number of crimes, int[1] = year of crimes, int[2] = month of crimes
    private ArrayList<int[]> numCrimesList = null;
    private int graphWidth;
    private int graphHeight;
    private int xRangeMonths;
    private int xStartValue;
    private int yRange;
    private int yStartValue;
    private int padding = 40;
    private @ColorInt
    int backgroundColour;
    private Context context;
    private int year = 2018;
    private String category = "";


    @Override
    protected void onDraw(Canvas canvas) {
        padding = 64;

        this.setBackgroundColor(backgroundColour);

        plotGraphAxis(canvas);

        if (numCrimesList == null || numCrimesList.size() < 2) {
            //Toasty.error(context, "Not enough data", Toasty.LENGTH_LONG).show();
            return;
        }

        setYRange();
        setXRange();

        if (yRange == 0 && numCrimesList.get(0)[0] == 0) {
            Toasty.error(context, "No Crimes Found", Toasty.LENGTH_LONG).show();
            return;
        }

        addPoints(canvas);
    }

    private void addPoints(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(POINT_COLOR);
        paint.setStrokeWidth(POINT_SIZE);

        float x;
        float y;
        float prevX = 0;
        float prevY = 0;
        int[] prevPoint = new int[3];
        final float pointRadius = 16;

        for(int[] point : numCrimesList){
            x = calcPointX(point);
            y = canvas.getHeight()-calcPointY(point);

            if(prevX != 0 && prevY != 0) {
                int[] rgb = calculateLineColour(point, prevPoint);
                connectPoints(rgb, x, y, prevX, prevY, canvas);

                if (numCrimesList.size()-1 == numCrimesList.lastIndexOf(point)){
                    canvas.drawCircle(x, y, pointRadius, paint);
                    drawTextOnPoint(point, x, y, canvas);
                }

                canvas.drawCircle(prevX, prevY, pointRadius, paint);
                drawTextOnPoint(prevPoint, prevX, prevY, canvas);

            } else {
                canvas.drawCircle(x, y, pointRadius, paint);
            }

            prevPoint = point;
            prevX = x;
            prevY = y;
        }
    }

    private void drawTextOnPoint(int[] point, float x, float y, Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(TEXT_COLOR);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
        paint.setTextSize(TEXT_SIZE);
        paint.setShadowLayer(TEXT_SHADOW_RADIUS, TEXT_SHADOW_DX,TEXT_SHADOW_DY, TEXT_SHADOW_COLOR);

        String month = new DateFormatSymbols().getMonths()[point[2]-1];

        canvas.drawText(month, x, y+55, paint);
        canvas.drawText(Integer.toString(point[0]), x, y-30, paint);
    }

    private void connectPoints(int[] rgb, float x, float y, float prevX, float prevY, Canvas canvas){
        Paint paint = new Paint();
        paint.setStrokeWidth(16);

        paint.setColor(Color.rgb(rgb[0], rgb[1], rgb[2]));

        canvas.drawLine(prevX, prevY, x, y, paint);
    }

    private int[] calculateLineColour(int[] point, int[] prevPoint){
        int monthsSinceStart = ((point[1] * 12) + point[2]) - xStartValue;
        int prevMonthSinceStart = ((prevPoint[1] * 12) + prevPoint[2]) - xStartValue;

        //int[0] = red, int[1] = green, int[2] = blue
        int[] rgb = new int[3];

        float gradient = ((point[0]-prevPoint[0])/(monthsSinceStart-prevMonthSinceStart))*3;

        if (gradient > 0) {
            //redness
            rgb[0] = Math.round(gradient);
            if (rgb[0] > 255) {
                rgb[0] = 255;
            }

            //lightening the colour up
            rgb[1] = 50;
            //rgb[2] = (255-rgb[0])/2;
        }

        if (gradient < 0) {
            //green-ness
            rgb[1] = Math.round(0-(gradient));
            if (rgb[1] > 255) {
                rgb[1] = 255;
            }

            //lightening the colour up
            rgb[0] = 50;
            //rgb[2] = (255-rgb[1])/2;
        }

        rgb[2] = 50;
        return rgb;
    }

    private float calcPointY(int[] point){
        float y = padding;

        int numAboveLowest = point[0] - yStartValue;

        y += (numAboveLowest*graphHeight/yRange);

        return y;
    }

    private float calcPointX(int[] point){
        float x = padding;

        int monthsSinceStart = ((point[1] * 12) + point[2]) - xStartValue;

        x += (monthsSinceStart*graphWidth)/xRangeMonths;

        return x;
    }

    private void setXRange(){
        int lastIndex = numCrimesList.size()-1;
        int yearsRange = numCrimesList.get(lastIndex)[1] - numCrimesList.get(0)[1];
        int monthsRange = numCrimesList.get(lastIndex)[2] - numCrimesList.get(0)[2];

        xRangeMonths = (yearsRange*12) + monthsRange;
        xStartValue = (numCrimesList.get(0)[1] * 12) + numCrimesList.get(0)[2];
        Log.d("xrange", "monthrange is " + monthsRange + " and yearsrange is " + yearsRange + "...  and numcrimes is " + lastIndex);
    }

    private void setYRange(){
        int highest = 0;
        int lowest = numCrimesList.get(0)[0];

        for (int[] numCrimes : numCrimesList) {
            if (numCrimes[0] > highest) {
                highest = numCrimes[0];

            } else if (numCrimes[0] < lowest) {
                lowest = numCrimes[0];
            }
        }

        yStartValue = lowest;
        yRange = highest - lowest;
    }

    private void plotGraphAxis(Canvas canvas){
        Paint axisPaint = new Paint();
        axisPaint.setColor(AXIS_COLOR);
        axisPaint.setStrokeWidth(16);
        
        int stroke = Math.round(axisPaint.getStrokeWidth());

        //adds padding from view border
        graphWidth = canvas.getWidth() - (padding*2);
        graphHeight = canvas.getHeight() - (padding*2);

        //y axis
        canvas.drawLine(padding,padding+graphHeight -(stroke/2),padding,padding, axisPaint);

        //x axis
        canvas.drawLine(padding - (stroke/2),padding+graphHeight,padding+graphWidth,padding+graphHeight, axisPaint);

        //to offset the vertices from axis lines
        graphWidth -= stroke*4;
        graphHeight -= stroke*4;
        padding += stroke*2;
    }

    public void addToNumCrimesList(int[] numCrimes){
        numCrimesList.add(numCrimes);
    }

    public void setNumCrimesList(ArrayList<int[]> numCrimesList){
        this.numCrimesList = numCrimesList;
        //numCrimesList = new ArrayList<>();
        //DBHandler dbHandler = new DBHandler(getContext());
//
        //if (category != "") {
        //    numCrimesList = dbHandler.countCrimesForMonthsInYear(year, category);
        //    if (numCrimesList.size() == 0) {
        //        return;
        //    }
        //    yStartValue = dbHandler.countLowestCrimeMonth(year, category);
        //    yRange = dbHandler.countHighestCrimeMonth(year, category) - yStartValue;
//
        //} else {
        //    numCrimesList = dbHandler.countCrimesForMonthsInYear(year);
        //    if (numCrimesList.size() == 0) {
        //        return;
        //    }
        //    yStartValue = dbHandler.countLowestCrimeMonth(year);
        //    yRange = dbHandler.countHighestCrimeMonth(year) - yStartValue;
        //}
//
        //for (int[] crime : numCrimesList) {
        //    Log.d("CrimeGraph", "number of crimes: " + crime[0] + ", year: " + crime[1] + ", month: " + crime[2]);
        //}
    }

    private void init(Context context) {
        this.animate();
        this.context = context;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.color, typedValue, true);
        backgroundColour = typedValue.data;
        numCrimesList = new ArrayList<>();
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

    public void setYear(int year) {
        this.year = year;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
