package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Supply extends AppCompatActivity implements View.OnClickListener {

    private Button btnStock, btnAgriculture, btnMarket, btnNutrition, btnDelivery;
    private TextView date, moneyD, moneyR;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);

        btnStock = findViewById(R.id.btnStock);
        btnAgriculture = findViewById(R.id.btnAgriculture);
        btnMarket = findViewById(R.id.btnMarket);
        btnNutrition = findViewById(R.id.btnNutrition);
        btnDelivery = findViewById(R.id.btnDelivery);
        date = findViewById(R.id.date);
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

        btnStock.setOnClickListener(this);
        btnAgriculture.setOnClickListener(this);
        btnMarket.setOnClickListener(this);
        btnNutrition.setOnClickListener(this);
        btnDelivery.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnStock:
                intent = new Intent(this, Stock.class);
                break;
            case R.id.btnAgriculture:
                intent = new Intent(this, Agriculture.class);
                break;
            case R.id.btnMarket:
                intent = new Intent(this, Market.class);
                break;
            case R.id.btnNutrition:
                Toast.makeText(getApplicationContext(), "Еще не доступно в демо-версии. ", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnDelivery:
                Toast.makeText(getApplicationContext(), "Еще не доступно в демо-версии. ", Toast.LENGTH_LONG).show();
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
