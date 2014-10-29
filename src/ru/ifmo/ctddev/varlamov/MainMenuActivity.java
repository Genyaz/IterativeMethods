package ru.ifmo.ctddev.varlamov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainMenuActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_activity);
        Intent intent = new Intent(this, SequenceLimitActivity.class);
        startActivity(intent);
    }
}
