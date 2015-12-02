package com.best.class6;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.AsyncQueryHandler;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout background;
    private int currentBackgroundColor;
    private BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init view
        background = (RelativeLayout) findViewById(R.id.background);

        //get current view's background color
        currentBackgroundColor = ((ColorDrawable)background.getBackground()).getColor();


        //execute background AsyncTask
        task = new BackgroundTask();
        task.execute();

    }

    @Override
    protected void onStop() {
        super.onStop();

        task.cancel(true);
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, Integer> {

        public final long SECOND = 1000;
        @Override
        protected Integer doInBackground(Void... params) {

            Random random = new Random(System.currentTimeMillis());
            int count = random.nextInt(5) + 6; // [5, 10]

            for (int i = 0; i < count; i++) {
                try {

                    //sleep for 5 seconds
                    Thread.sleep(5 * SECOND);

                    //check for cancellation
                    if (isCancelled()) break;

                    //get random color//

                    //option 1
                    int color = random.nextInt(Integer.MAX_VALUE);

                    //option2
//                    int r = random.nextInt(256);
//                    int g = random.nextInt(256);
//                    int b = random.nextInt(256);
//                    int color = Color.rgb(r, g, b);

                    publishProgress(color);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return count;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            int color = values[0];


            //create and start animation
            ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(
                    background,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    currentBackgroundColor,
                    color);
            backgroundColorAnimator.setDuration(SECOND);
            backgroundColorAnimator.start();

            currentBackgroundColor = color;
        }

        @Override
        protected void onPostExecute(Integer val) {
            Toast.makeText(getBaseContext(), "The color has changed " + val + " times.", Toast.LENGTH_SHORT).show();
        }

    }
}
