package ru.ifmo.ctddev.varlamov;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class SequenceLimitActivity extends Activity implements View.OnClickListener {

    private EditText rEdit, x0Edit, iterationsEdit;
    private LineGraphView graphView;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private GraphViewSeries graphViewSeries;
    private Button runButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_limit_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        rEdit = (EditText) findViewById(R.id.sequence_limit_rtext);
        x0Edit = (EditText) findViewById(R.id.sequence_limit_x0text);
        iterationsEdit = (EditText) findViewById(R.id.sequence_limit_iterations);
        progressBar = (ProgressBar) findViewById(R.id.sequence_limit_progressBar);
        progressBar.setVisibility(View.GONE);
        frameLayout = (FrameLayout) findViewById(R.id.sequence_limit_frame);
        graphView = new LineGraphView(this, "Sequence limits");
        graphView.setVisibility(View.GONE);
        graphView.setDataPointsRadius(10);
        graphViewSeries = new GraphViewSeries("Sequence",
                new GraphViewSeries.GraphViewSeriesStyle(Color.argb(255, 0, 255, 0), 3),
                new GraphViewDataInterface[0]);
        graphView.addSeries(graphViewSeries);
        frameLayout.addView(graphView);
        runButton = (Button) findViewById(R.id.sequence_limit_run_button);
        runButton.setOnClickListener(this);
    }

    @Override
    public synchronized void onClick(View v) {
        runButton.setClickable(false);
        frameLayout.bringChildToFront(progressBar);
        progressBar.setVisibility(View.VISIBLE);
        double r = 0;
        String s = rEdit.getText().toString();
        if (!s.isEmpty()) {
            r = Double.parseDouble(s);
        }
        double x0 = 0;
        s = x0Edit.getText().toString();
        if (!s.isEmpty()) {
            x0 = Double.parseDouble(s);
        }
        int iterations = 0;
        s = iterationsEdit.getText().toString();
        if (!s.isEmpty()) {
            iterations = Integer.parseInt(s);
        }
        GraphView.GraphViewData[] values = new GraphView.GraphViewData[iterations + 1];
        values[0] = new GraphView.GraphViewData(0, x0);
        for (int i = 1; i <= iterations; i++) {
            x0 = r * x0 * (1 - x0);
            values[i] = new GraphView.GraphViewData(i, x0);
        }
        graphViewSeries.resetData(values);
        progressBar.setVisibility(View.GONE);
        graphView.setVisibility(View.VISIBLE);
        frameLayout.bringChildToFront(graphView);
        runButton.setClickable(true);
    }
}