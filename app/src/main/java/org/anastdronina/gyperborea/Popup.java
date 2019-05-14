package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Popup extends AppCompatActivity implements View.OnClickListener {
    private EditText editCountryName;
    private EditText editPresidentName;
    private String presidentName = "";
    private String countryName = "";

    private Button popupApply, popupBack;
    private RadioGroup levels;
    private RadioButton chekedRadioButton;
    private RadioButton easyLevel, middleLevel, hardLevel;
    private SharedPreferences allSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        popupApply = findViewById(R.id.popupApply);
        popupBack = findViewById(R.id.popupBack);
        levels = findViewById(R.id.levels);
        easyLevel = findViewById(R.id.easyLevel);
        middleLevel = findViewById(R.id.middleLevel);
        hardLevel = findViewById(R.id.hardLevel);
        editCountryName = findViewById(R.id.editCountryName);
        editPresidentName = findViewById(R.id.editPresidentName);

        popupApply.setOnClickListener(this);
        popupBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!allSettings.getBoolean("NEW_GAME", true)){
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.popupApply:
                countryName = editCountryName.getText().toString();
                presidentName = editPresidentName.getText().toString();

                if (countryName.length() > 0 && presidentName.length() > 0) {
                    SharedPreferences allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
                    allSettings.edit().putBoolean("NEW_GAME", false).apply();
                    allSettings.edit().putString("COUNTRY_NAME", countryName).apply();
                    allSettings.edit().putString("PRESIDENT_NAME", presidentName).apply();

                    int checkedId = levels.getCheckedRadioButtonId();
                    chekedRadioButton = (RadioButton) findViewById(checkedId);
                    if (chekedRadioButton.equals(easyLevel)) {
                        allSettings.edit().putString("LEVEL", "Easy").apply();
                        allSettings.edit().putLong("MONEY_DOLLARS", 300000).apply();
                        allSettings.edit().putLong("MONEY_RUBLES", 30000000).apply();
                    }
                    if (chekedRadioButton.equals(hardLevel)) {
                        allSettings.edit().putString("LEVEL", "Hard").apply();
                        allSettings.edit().putLong("MONEY_DOLLARS", 100000).apply();
                        allSettings.edit().putLong("MONEY_RUBLES", 10000000).apply();
                    }
                    if (chekedRadioButton.equals(middleLevel)) {
                        allSettings.edit().putString("LEVEL", "Middle");
                        allSettings.edit().putLong("MONEY_DOLLARS", 200000).apply();
                        allSettings.edit().putLong("MONEY_RUBLES", 20000000).apply();
                    }

                    intent = new Intent(this, NewGame.class);
                } else {
                    Toast.makeText(this,
                            "Введите название государства и имя правителя. ", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.popupBack:
                intent = new Intent(this, MainActivity.class);
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
