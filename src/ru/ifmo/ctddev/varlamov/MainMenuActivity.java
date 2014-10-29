package ru.ifmo.ctddev.varlamov;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainmenu_to_bifurcations:
                Intent intent = new Intent(this, BifurcationsActivity.class);
                startActivity(intent);
                break;
            case R.id.mainmenu_to_sequencelimits:
                intent = new Intent(this, SequenceLimitActivity.class);
                startActivity(intent);
                break;
            case R.id.mainmenu_to_sequencepolyline:
                intent = new Intent(this, SequencePolylineActivity.class);
                startActivity(intent);
                break;
            case R.id.mainmenu_to_fractalpicture:
                intent = new Intent(this, FractalPictureActivity.class);
                startActivity(intent);
                break;
            case R.id.mainmenu_to_fractal_polyline:
                intent = new Intent(this, FractalPolylineActivity.class);
                startActivity(intent);
                break;
        }
    }
}
