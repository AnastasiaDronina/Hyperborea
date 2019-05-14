package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ResetPreferences extends AppCompatActivity implements View.OnClickListener {

    public static final String ALL_SETTINGS = "allSettings";
    private Button buttonOk, buttonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_preferences);

        buttonOk = findViewById(R.id.buttonOk);
        buttonNo = findViewById(R.id.buttonNo);
        buttonOk.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        SharedPreferences allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        switch (v.getId()) {
            case R.id.buttonOk:
                allSettings.edit().putInt("TURN", 0).apply();
                allSettings.edit().putBoolean("NEW_GAME", true).apply();
                allSettings.edit().putString("PRESIDENT_NAME", "").apply();
                allSettings.edit().putString("COUNTRY_NAME", "").apply();
                allSettings.edit().putString("LEVEL", "Middle").apply();
                allSettings.edit().putLong("MONEY_DOLLARS", 200000).apply();
                allSettings.edit().putInt("MONEY_CENTS", 0).apply();
                allSettings.edit().putLong("MONEY_RUBLES", 20000000).apply();
                allSettings.edit().putInt("MONEY_KOP", 0).apply();
                allSettings.edit().putInt("MONTH_ID", 1).apply();
                allSettings.edit().putInt("YEAR", 2019).apply();
                allSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                allSettings.edit().putInt("SCIENTIST_IN_USE_ID", 0).apply();
                allSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
                allSettings.edit().putInt("FARMER_IN_USE_ID", 0).apply();
                allSettings.edit().putString("SOLD_TECHNOLOGIES", "").apply();

                getApplicationContext().deleteDatabase("hyperborea.db");

                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.buttonNo:
                intent = new Intent(this, MainActivity.class);
            default:
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
