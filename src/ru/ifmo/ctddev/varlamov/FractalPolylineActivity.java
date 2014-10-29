package ru.ifmo.ctddev.varlamov;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LineGraphView;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by Genyaz on 30.10.2014.
 */
public class FractalPolylineActivity extends Activity implements View.OnClickListener {
    private static final int ITERATIONS = 100;
    private static final double EPSILON = 1e-2;

    private FrameLayout frameLayout;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer mRenderer;
    private EditText xEdit, yEdit;

    private GraphicalView chartView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fractal_polyline_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        frameLayout = (FrameLayout) findViewById(R.id.fractal_polyline_frame);
        XYSeries series = new XYSeries("");
        series.add(0, 0);
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.WHITE);
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);
        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.setXAxisMax(10);
        mRenderer.setXAxisMin(-10);
        mRenderer.setYAxisMax(10);
        mRenderer.setYAxisMin(-10);
        mRenderer.setShowGrid(true);
        mRenderer.addSeriesRenderer(renderer);
        chartView = ChartFactory.getLineChartView(this, dataset, mRenderer);
        frameLayout.addView(chartView);
        frameLayout.bringChildToFront(chartView);
        Button button = (Button) findViewById(R.id.fractal_polyline_run);
        button.setOnClickListener(this);
        xEdit = (EditText) findViewById(R.id.fractal_polyline_xedit);
        yEdit = (EditText) findViewById(R.id.fractal_polyline_yedit);
    }

    @Override
    public void onClick(View v) {
        XYSeries series = new XYSeries("");
        Complex z = new Complex(Double.parseDouble(xEdit.getText().toString()), Double.parseDouble(yEdit.getText().toString()));
        for (int i = 0; i < ITERATIONS; i++) {
            z = z.multiply(z).multiply(z).multiply(2).add(1).divide(z.multiply(z).multiply(3));
            series.add(z.getReal(), z.getImaginary());
        }
        dataset.addSeries(series);
        Complex[] roots = new Complex(1, 0).nthRoot(3).toArray(new Complex[0]);
        int[] colors = new int[3];
        colors[0] = Color.RED;
        colors[1] = Color.GREEN;
        colors[2] = Color.BLUE;
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.WHITE);
        for (int i = 0; i < 3; i++) {
            if (z.subtract(roots[i]).abs() < EPSILON) {
                renderer.setColor(colors[i]);
            }
        }
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);
        mRenderer.addSeriesRenderer(renderer);
        chartView.repaint();
    }
}