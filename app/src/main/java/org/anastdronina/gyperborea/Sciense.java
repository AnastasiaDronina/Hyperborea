package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Sciense extends AppCompatActivity implements View.OnClickListener {

    private Button btnTecnologies, btnSellTecnologies;
    private DateAndMoney dateAndMoney;
    private SharedPreferences allSettings;
    private TextView date, moneyR, moneyD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sciense);

        dateAndMoney = new DateAndMoney();
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        btnTecnologies = findViewById(R.id.btn_tecnologies);
        btnSellTecnologies = findViewById(R.id.btn_sell_tecnologies);
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);

        btnTecnologies.setOnClickListener(this);
        btnSellTecnologies.setOnClickListener(this);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_tecnologies:
                intent = new Intent(this, Tecnologies.class);
                break;
            case R.id.btn_sell_tecnologies:
                intent = new Intent(this, SellTechnology.class);
                break;
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
