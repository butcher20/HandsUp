package com.presentech.handsup;

/*Created by Jack Bardsley*/

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.util.List;


public class ReviewFeedbackGMB extends Activity {

    public double slide;         //Number of slides
    int j = 0;              //currently displayed slide responses
    public PieChart slideGMB;
    //feedbackDatabaseHandler database;
    List<SingleFeedback> singleFeedbackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_feedback_gmb);

        getPiePlots();

    }


    public void getPiePlots() {
        //get list of objects from database
        feedbackDatabaseHandler database = new feedbackDatabaseHandler(getApplicationContext(), "TestFeedbackList", null, 1, "FP");
        List<SingleFeedback> singleFeedbackList = database.getAllFeedback();

        //convert list of feedback objects to array of feedbackobjects
        SingleFeedback[] feedbackArray = new SingleFeedback[singleFeedbackList.size()];
        singleFeedbackList.toArray(feedbackArray);



        //get number of slides
        slide = feedbackArray[0].getSLIDE();

        for (int i = 0; i < feedbackArray.length; i++) {
            if (feedbackArray[i].getSLIDE() > slide){
                slide = feedbackArray[i].getSLIDE();
            }
        }

        int sNo = (int) slide;

        int[] answerA = new int[sNo];           /*Initialise array for A answers for questions*/
        int[] answerB = new int[sNo];           /*Initialise array for B answers for questions*/
        int[] answerC = new int[sNo];           /*Initialise array for C answers for questions*/

        int a = 0;
        int b = 0;
        int c = 0;

        /*iterate through slides*/
        for (int i = 0; i < sNo; i++) {
            a = b = c = 0;
            /*iterate through objects to find whether value is A, B or C*/
            for (int k = 0; k < feedbackArray.length; k++) {
                if ((int) feedbackArray[k].getSLIDE() == i + 1) {
                    if (feedbackArray[k].getGOOD_MEH_BAD() == 1) {
                        a++;
                        answerA[i] = a;
                        break;
                    }
                    if (feedbackArray[k].getGOOD_MEH_BAD() == 2) {
                        b++;
                        answerB[i] = b;
                        break;
                    }
                    if (feedbackArray[k].getGOOD_MEH_BAD() == 3) {
                        c++;
                        answerC[i] = c;
                        break;
                    }

                }
            }
        }

        slideGMB = (PieChart) findViewById(R.id.plot); //get pie plot

        SegmentFormatter segmentFormat = new SegmentFormatter();
        segmentFormat.configure(this, R.xml.segment_formatter);


        slideGMB.setTitle("Responses from Slide Number " + Integer.toString(j + 1));

        //set button visibilities depending on question number
        if (j == 0) {
            findViewById(R.id.slide_down_button).setVisibility(View.GONE);
        }
        else
            findViewById(R.id.slide_down_button).setVisibility(View.VISIBLE);

        if (j == sNo-1) {
            findViewById(R.id.slide_up_button).setVisibility(View.GONE);
        }
        else
            findViewById(R.id.slide_up_button).setVisibility(View.VISIBLE);


        //add segments
        if (answerA[j] > 0) {
            Segment segA = new Segment("Good = "+answerA[j], answerA[j]);
            slideGMB.addSeries(segA, segmentFormat);
        }
        if (answerB[j] > 0) {
            Segment segB = new Segment("Meh = " +answerB[j], answerB[j]);
            slideGMB.addSeries(segB, segmentFormat);
        }
        if (answerC[j] > 0) {
            Segment segC = new Segment("Bad = " + answerC[j], answerC[j]);
            slideGMB.addSeries(segC, segmentFormat);
        }

    }

    //increment question number (array index "j") and redraw plots
    public void slideNoUp(View v) {
        j++;
        slideGMB.clear();
        getPiePlots();
        slideGMB.redraw();
    }

    public void slideNoDown(View v) {
        j--;
        slideGMB.clear();
        getPiePlots();
        slideGMB.redraw();

    }
}