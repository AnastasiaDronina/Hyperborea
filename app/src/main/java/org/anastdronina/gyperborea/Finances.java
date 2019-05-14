package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Finances extends AppCompatActivity implements View.OnClickListener {

    private Button btnRubToDol, btnDolToRub, btnRubToDolCount, btnDolToRubCount;
    private ImageButton btnToPeople;
    private EditText etRubToConvert, etDolToConvert, etRubConverted, etDolConverted;
    private AlertDialog uSureDialogRub, uSureDialogDol;
    private TextView tvForDialogRub, tvForDialogDol, tvRulesFinances, date, moneyD, moneyR;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    SQLiteDatabase db;
    private double coef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        coef = 0.0;
        SQLiteDatabase db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            double finCoef = res.getDouble(18);
            coef += finCoef;
        }
        coef = Math.round(coef * 10.0) / 10.0;

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        btnRubToDol = findViewById(R.id.btn_rub_to_dol);
        btnDolToRub = findViewById(R.id.btn_dol_to_rub);
        btnRubToDolCount = findViewById(R.id.btn_rub_to_dol_count);
        btnDolToRubCount = findViewById(R.id.btn_dol_to_rub_count);
        btnToPeople = findViewById(R.id.btnToPeople);
        etRubToConvert = findViewById(R.id.et_rub_to_convert);
        etDolToConvert = findViewById(R.id.et_dol_to_convert);
        etRubConverted = findViewById(R.id.et_rub_converted);
        etDolConverted = findViewById(R.id.et_dol_converted);
        uSureDialogRub = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        uSureDialogRub.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogRub = new TextView(this);
        uSureDialogDol = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        uSureDialogDol.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogDol = new TextView(this);
        tvRulesFinances = findViewById(R.id.tv_rules_finances);
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);
        dateAndMoney = new DateAndMoney();

        btnRubToDolCount.setOnClickListener(this);
        btnDolToRubCount.setOnClickListener(this);
        btnRubToDol.setOnClickListener(this);
        btnDolToRub.setOnClickListener(this);
        btnToPeople.setOnClickListener(this);

        uSureDialogRub.setTitle("Подтвердите действие");
        uSureDialogRub.setView(tvForDialogRub);
        uSureDialogDol.setTitle("Подтвердите действие");
        uSureDialogDol.setView(tvForDialogDol);

        uSureDialogRub.setButton(DialogInterface.BUTTON_POSITIVE, "Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String rubles = Long.toString(allSettings.getLong("MONEY_RUBLES", 20000000));
                String kop;
                if (allSettings.getInt("MONEY_KOP", 0) < 10) {
                    kop = "0" + allSettings.getInt("MONEY_KOP", 0);
                } else kop = Integer.toString(allSettings.getInt("MONEY_KOP", 0));
                BigDecimal currentRubbles = new BigDecimal(rubles + "." + kop);

                String dollars = Long.toString(allSettings.getLong("MONEY_DOLLARS", 20000));
                String cents;
                if (allSettings.getInt("MONEY_CENTS", 0) < 10) {
                    cents = "0" + allSettings.getInt("MONEY_CENTS", 0);
                } else cents = Integer.toString(allSettings.getInt("MONEY_CENTS", 0));
                BigDecimal currentDollars = new BigDecimal(dollars + "." + cents);

                BigDecimal rubblesFromEditText = new BigDecimal(etRubToConvert.getText().toString());
                BigDecimal dollarsFromEditText = new BigDecimal(etDolConverted.getText().toString());

                String[] partsResRub = currentRubbles.subtract(rubblesFromEditText).toString().split("\\.");
                String[] partsResDol = currentDollars.add(dollarsFromEditText).toString().split("\\.");

                allSettings.edit().putLong("MONEY_DOLLARS", Integer.parseInt(partsResDol[0])).apply();
                allSettings.edit().putInt("MONEY_CENTS", Integer.parseInt(partsResDol[1])).apply();
                allSettings.edit().putLong("MONEY_RUBLES", Integer.parseInt(partsResRub[0])).apply();
                allSettings.edit().putInt("MONEY_KOP", Integer.parseInt(partsResRub[1])).apply();

                moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
                moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
            }
        });

        uSureDialogDol.setButton(DialogInterface.BUTTON_POSITIVE, "Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String rubles = Long.toString(allSettings.getLong("MONEY_RUBLES", 20000000));
                String kop;
                if (allSettings.getInt("MONEY_KOP", 0) < 10) {
                    kop = "0" + allSettings.getInt("MONEY_KOP", 0);
                } else kop = Integer.toString(allSettings.getInt("MONEY_KOP", 0));
                BigDecimal currentRubbles = new BigDecimal(rubles + "." + kop);

                String dollars = Long.toString(allSettings.getLong("MONEY_DOLLARS", 20000));
                String cents;
                if (allSettings.getInt("MONEY_CENTS", 0) < 10) {
                    cents = "0" + allSettings.getInt("MONEY_CENTS", 0);
                } else cents = Integer.toString(allSettings.getInt("MONEY_CENTS", 0));
                BigDecimal currentDollars = new BigDecimal(dollars + "." + cents);

                BigDecimal rubblesFromEditText = new BigDecimal(etRubConverted.getText().toString());
                BigDecimal dollarsFromEditText = new BigDecimal(etDolToConvert.getText().toString());

                String[] resRub = currentRubbles.add(rubblesFromEditText).toString().split("\\.");
                String[] resDol = currentDollars.subtract(dollarsFromEditText).toString().split("\\.");

                allSettings.edit().putLong("MONEY_DOLLARS", Integer.parseInt(resDol[0])).apply();
                allSettings.edit().putInt("MONEY_CENTS", Integer.parseInt(resDol[1])).apply();
                allSettings.edit().putLong("MONEY_RUBLES", Integer.parseInt(resRub[0])).apply();
                allSettings.edit().putInt("MONEY_KOP", Integer.parseInt(resRub[1])).apply();

                moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
                moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
            }
        });
        tvRulesFinances.setText("Коэффициент улучшения курса: " + coef + "\nПокупка $ - " + (70.0 - coef) + "0 руб \nПродажа $ - " + (60.0 + coef) + "0 руб");
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
        long moneyDollars = allSettings.getLong("MONEY_DOLLARS", 200000);
        long moneyRubles = allSettings.getLong("MONEY_RUBLES", 20000000);
        int moneyCents = allSettings.getInt("MONEY_CENTS", 00);
        int moneyKop = allSettings.getInt("MONEY_KOP", 00);
        String cents, kop;
        if (moneyCents < 10) {
            cents = "0" + moneyCents;
        } else cents = Integer.toString(moneyCents);
        if (moneyKop < 10) {
            kop = "0" + moneyKop;
        } else kop = Integer.toString(moneyKop);

        BigDecimal rub = new BigDecimal(moneyRubles + "." + kop);
        BigDecimal dol = new BigDecimal(moneyDollars + "." + cents);

        switch (v.getId()) {
            case R.id.btnToPeople:
                Intent intent = new Intent(this, People.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_rub_to_dol_count:
                if(isNumeric(etRubToConvert.getText().toString())){
                    if (new BigDecimal(etRubToConvert.getText().toString()).compareTo(rub) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etRubToConvert) == false) {
                        showErrorMsg("kop");
                    } else {
                        checkDecimalPart(etRubToConvert);
                    }
                } else showErrorMsg("kop");
                break;
            case R.id.btn_dol_to_rub_count:
                if(isNumeric(etDolToConvert.getText().toString())){
                    if (new BigDecimal(etDolToConvert.getText().toString()).compareTo(dol) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etDolToConvert) == false) {
                        showErrorMsg("cent");
                    } else {
                        checkDecimalPart(etDolToConvert);
                    }
                } else showErrorMsg("cent");
                break;
            case R.id.btn_rub_to_dol:
                if(isNumeric(etRubToConvert.getText().toString())){
                    if (new BigDecimal(etRubToConvert.getText().toString()).compareTo(rub) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etRubToConvert) == false) {
                        showErrorMsg("kop");
                    } else {
                        checkDecimalPart(etRubToConvert);
                        tvForDialogRub.setText(etRubToConvert.getText().toString() + "  \u20BD -----> " + etDolConverted.getText().toString()
                                + " $\n\nУверены, что хотите конвертировать?");
                        uSureDialogRub.show();
                    }
                } else {
                    showErrorMsg("kop");
                }
                break;
            case R.id.btn_dol_to_rub:
                if(isNumeric(etDolToConvert.getText().toString())){
                    if (new BigDecimal(etDolToConvert.getText().toString()).compareTo(dol) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etDolToConvert) == false) {
                        showErrorMsg("cent");
                    } else {
                        checkDecimalPart(etDolToConvert);
                        tvForDialogDol.setText(etDolToConvert.getText().toString() + " $ -----> " + etRubConverted.getText().toString()
                                + " \u20BD\n\nУверены, что хотите конвертировать?");
                        uSureDialogDol.show();
                    }
                } else showErrorMsg("cent");
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean checkDecimalPart(EditText et) {
        if (et.equals(etRubToConvert)) {
            String rubInput = etRubToConvert.getText().toString();
            if(isNumeric(rubInput)){
                String[] parts = rubInput.split("\\.");
                if (parts.length <= 1) {
                    return false;
                } else if (new BigDecimal(parts[0]).compareTo(new BigDecimal(1)) == -1) {
                    return false;
                } else {
                    if (2 == parts[1].length()) {
                        BigDecimal resultRubToDol = new BigDecimal(rubInput).multiply(new BigDecimal(1 / (70.0 - coef))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        etDolConverted.setText(resultRubToDol.toString());
                        return true;
                    } else return false;
                }
            } else return false;
        } else if (et.equals(etDolToConvert)) {
            String dolInput = etDolToConvert.getText().toString();
            if(isNumeric(dolInput)){
                String[] parts2 = dolInput.split("\\.");
                if (parts2.length <= 1) {
                    return false;
                } else {
                    if (2 == parts2[1].length()) {
                        BigDecimal resultDolToRub = new BigDecimal(dolInput).multiply(new BigDecimal(60.0 + coef)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        etRubConverted.setText(resultDolToRub.toString());
                        return true;
                    } else return false;
                }
            } else {
                return false;
            }
        } else return false;
    }

    public boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    public void showErrorMsg(String currency) {
        if (currency.equals("cent")) {
            Toast.makeText(getApplicationContext(), "Неверный формат! \nПример заполнения: 54000.55 или 500.00 Цифры после точки - это центы. ", Toast.LENGTH_LONG).show();
        }
        if (currency.equals("kop")) {
            Toast.makeText(getApplicationContext(), "Неверный формат! \nПример заполнения: 500.00 или 54000.55. Цифры после точки - это копейки. ", Toast.LENGTH_LONG).show();
        }
    }

}
