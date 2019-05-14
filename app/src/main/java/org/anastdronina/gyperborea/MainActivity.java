package org.anastdronina.gyperborea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startNewGame = findViewById(R.id.startNewGame);
        Button aboutGame = findViewById(R.id.aboutGame);
        Button gameSettings = findViewById(R.id.gameSettings);

        startNewGame.setOnClickListener(this);
        aboutGame.setOnClickListener(this);
        gameSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.gameSettings:
                intent = new Intent(this, ResetPreferences.class);
                break;
            case R.id.startNewGame:
                intent = new Intent(this, NewGame.class);
                break;
            case R.id.aboutGame:
                intent = new Intent(this, AboutGame.class);
                break;
            default:
        }
        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
