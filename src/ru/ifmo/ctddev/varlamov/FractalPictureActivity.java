package ru.ifmo.ctddev.varlamov;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class FractalPictureActivity extends Activity {

    private static final double X_MIN = -10;
    private static final double X_MAX = 10;
    private static final double Y_MIN = -10;
    private static final double Y_MAX = 10;
    private static final int PARTITION = 200;

    private LineGraphView graphView;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;

    private class FractalTask extends AsyncTask<Void, Void, Void> {
        private static final int ITERATIONS = 10;
        private static final double EPSILON = 1e-2;

        @Override
        protected Void doInBackground(Void... params) {
            GraphViewSeries.GraphViewSeriesStyle[] styles = new GraphViewSeries.GraphViewSeriesStyle[3];
            styles[0] = new GraphViewSeries.GraphViewSeriesStyle(Color.argb(255, 255, 0, 0), 1);
            styles[1] = new GraphViewSeries.GraphViewSeriesStyle(Color.argb(255, 0, 255, 0), 1);
            styles[2] = new GraphViewSeries.GraphViewSeriesStyle(Color.argb(255, 0, 0, 255), 1);
            Complex[] roots = new Complex(1, 0).nthRoot(3).toArray(new Complex[0]);
            double x, y;
            Complex z;
            for (int i = 0; i <= PARTITION; i++) {
                for (int j = 0; j <= PARTITION; j++) {
                    x = X_MIN + i * (X_MAX - X_MIN) / PARTITION;
                    y = Y_MIN + j * (Y_MAX - Y_MIN) / PARTITION;
                    z = new Complex(x, y);
                    for (int k = 0; k < ITERATIONS; k++) {
                        z = z.multiply(z).multiply(z).multiply(2).add(1).divide(z.multiply(z).multiply(3));
                    }
                    for (int k = 0; k < 3; k++) {
                        if (z.subtract(roots[k]).abs() < EPSILON) {
                            GraphView.GraphViewData[] values = new GraphView.GraphViewData[2];
                            values[0] = new GraphView.GraphViewData(x, y);
                            values[1] = new GraphView.GraphViewData(x + (X_MAX - X_MIN) / PARTITION, y + (Y_MAX - Y_MIN) / PARTITION);
                            graphView.addSeries(new GraphViewSeries("", styles[k], values));
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            frameLayout.addView(graphView);
            frameLayout.bringChildToFront(graphView);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fractalpicture_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        progressBar = (ProgressBar) findViewById(R.id.fractalpicture_progressBar);
        frameLayout = (FrameLayout) findViewById(R.id.fractalpicture_frame);
        graphView = new LineGraphView(this, "Fractal picture");
        new FractalTask().execute();
    }
}