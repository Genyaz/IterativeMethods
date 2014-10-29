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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BifurcationsActivity extends Activity {

    private static final int ITERATIONS_BEFORE_STABILIZATION = 500;
    private static final double START_X = 2.4;
    private static final double END_X = 4;
    private static final int MAX_LIMITS_SIZE = 512;
    private static final double EPSILON = 1e-4;
    private static final int PARTITION = 2000;

    private static List<Double> getLimits(double r) {
        double x0 = 0.5;
        for (int i = 0; i < ITERATIONS_BEFORE_STABILIZATION; i++) {
            x0 = r * x0 * (1 - x0);
        }
        List<Double> limits = new ArrayList<Double>();
        limits.add(x0);
        double x1 = r * x0 * (1 - x0);
        int size = 1;
        while (size < MAX_LIMITS_SIZE && Math.abs(x1 - x0) > EPSILON) {
            size++;
            limits.add(x1);
            x1 = r * x1 * (1 - x1);
        }
        Collections.sort(limits);
        return limits;
    }

    private class CalculationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < PARTITION; i++) {
                double x = START_X + i * (END_X - START_X) / PARTITION;
                List<Double> points = getLimits(x);
                for (int j = 0; j < points.size(); j++) {
                    GraphView.GraphViewData[] data = new GraphView.GraphViewData[2];
                    data[0] = new GraphView.GraphViewData(START_X + i * (END_X - START_X) / PARTITION, points.get(j));
                    data[1] = new GraphView.GraphViewData(START_X + (i + 1) * (END_X - START_X) / PARTITION, points.get(j));
                    GraphViewSeries series = new GraphViewSeries("", new GraphViewSeries.GraphViewSeriesStyle(Color.argb(50, 0, 255, 0), 1), data);
                    bifurcationsGraph.addSeries(series);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            frameLayout.addView(bifurcationsGraph);
        }
    }

    private LineGraphView bifurcationsGraph;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bifurcations_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        bifurcationsGraph = new LineGraphView(this, "Bifurcations Diagram");
        progressBar = (ProgressBar) findViewById(R.id.bifurcations_progressBar);
        frameLayout = (FrameLayout) findViewById(R.id.bifurcations_frame);
        new CalculationTask().execute();
    }
}