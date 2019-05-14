package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class NewGame extends AppCompatActivity implements View.OnClickListener {

    private Boolean ifNewGame;

    private Button btnPopulation, btnFinances, btnScience, btnSupply;
    private FloatingActionButton btnNextTurn;
    private TextView moneyD, moneyR, date, tvForDialogNextTurn, tvForDialogGameOver, tvNotEnoughtMoney;
    private DateAndMoney dateAndMoney;
    private DatabaseHelper myDb;
    private SharedPreferences allSettings;
    private SQLiteDatabase db;
    private AlertDialog dialogNextTurn, dialogGameOver, dialogNotEnoughtMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        dateAndMoney = new DateAndMoney();
        ifNewGame = allSettings.getBoolean("NEW_GAME", true);
        if (ifNewGame) {
            Intent popupIntent = new Intent(this, Popup.class);
            startActivity(popupIntent);

            myDb = new DatabaseHelper(this);

            //filling population table in db
            myDb.insertPeopleData("Дмитрий", "Аксенов", 0, 0, 21, 0, 0, 0, 1, 0,
                    0, 1, 0, "Жаворонок", "Торопыжка", "Некультурный", 0, 0.0);
            myDb.insertPeopleData("Денис", "Сергеев", 0, 0, 53, 0, 1, 0, 0, 1,
                    0, 0, 0, "Способный ученик", "Технарь", "Бездонный желудок", 0, 0.0);
            myDb.insertPeopleData("Степан", "Одинцов", 0, 0, 28, 0, 0, 0, 0, 1,
                    1, 0, 0, "Декоратор", "Гурман", "Брезгливый", 0, 0.0);
            myDb.insertPeopleData("Вадим", "Дьячков", 0, 0, 34, 0, 0, 1, 0, 0,
                    1, 0, 0, "Веган", "Болтун", "Громкий храп", 0, 0.0);
            myDb.insertPeopleData("Владислав", "Кошелев", 0, 0, 30, 1, 0, 1, 0, 0,
                    0, 0, 0, "Способный ученик", "Декоратор", "Пацифист", 0, 0.0);
            myDb.insertPeopleData("Святослав", "Гуляев", 0, 0, 25, 0, 1, 0, 0, 0,
                    0, 1, 0, "Анемия", "Гурман", "Неконструктивный", 0, 0.0);
            myDb.insertPeopleData("Артём", "Тетерин", 0, 0, 28, 0, 0, 0, 1, 0,
                    1, 0, 0, "Руки из лапши", "Бездонный желудок", "Жаворонок", 0, 0.0);
            myDb.insertPeopleData("Эдуард", "Лыткин", 0, 0, 18, 0, 0, 0, 0, 1,
                    0, 0, 1, "Буйвол", "Декоратор", "Нарколепсия", 0, 0.0);
            myDb.insertPeopleData("Иннокентий", "Харитонов", 0, 0, 34, 0, 1, 1, 0, 0,
                    0, 0, 0, "Медленная обучаемость", "Брезгливый", "Торопыжка", 0, 0.0);
            myDb.insertPeopleData("Илья", "Некрасов", 0, 0, 23, 1, 0, 0, 0, 0,
                    1, 0, 1, "Трипофобия", "Технарь", "Декоратор", 0, 0.0);

            //filling tecnologies table in db
            myDb.insertTecnologiesData("tec1", "desc1", 3, 300, 0);
            myDb.insertTecnologiesData("tec2", "desc2", 10, 1000, 1);
            myDb.insertTecnologiesData("tec3", "desc3", 5, 500, 0);
            myDb.insertTecnologiesData("tec4", "desc4", 8, 800, 0);
            myDb.insertTecnologiesData("tec5", "desc5", 12, 1200, 0);
            myDb.insertTecnologiesData("tec1.1", "desc1.1", 2, 200, 0);
            myDb.insertTecnologiesData("tec2.1", "desc2.1", 7, 700, 0);
            myDb.insertTecnologiesData("tec3.1", "desc3.1", 9, 900, 0);
            myDb.insertTecnologiesData("tec4.1", "desc4.1", 4, 400, 0);
            myDb.insertTecnologiesData("tec5.1", "desc5.1", 18, 1800, 0);

            myDb.insertStockData("Самосвал", "Транспорт", 2);
            myDb.insertStockData("Огурцы", "Еда", 10);
            myDb.insertStockData("Ресурс1", "Ресурсы", 2);
            myDb.insertStockData("Лук", "Еда", 30);
            myDb.insertStockData("Ресурс2", "Ресурсы", 10);
            myDb.insertStockData("Помидоры", "Еда", 10);
            myDb.insertStockData("Оборудование3", "Оборудование", 2);
            myDb.insertStockData("Ресурс3", "Ресурсы", 6);
            myDb.insertStockData("Оборудование2", "Оборудование", 7);
            myDb.insertStockData("Легковой автомобиль", "Транспорт", 5);
            myDb.insertStockData("Оборудование1", "Оборудование", 10);
            myDb.insertStockData("Трактор", "Транспорт", 3);

            myDb.insertFarmsData("Теплица 1", "Не выбрано", 0, 0);
            myDb.insertFarmsData("Теплица 2", "Не выбрано", 0, 0);
            myDb.insertFarmsData("Теплица 3", "Не выбрано", 0, 0);
            myDb.insertFarmsData("Теплица 4", "Не выбрано", 0, 0);
            myDb.insertFarmsData("Теплица 5", "Не выбрано", 0, 0);

            myDb.insertMarketData("Картофель", 10, 300, "руб", "Еда");
            myDb.insertMarketData("Оборудование42", 5, 50000, "руб", "Оборудование");
            myDb.insertMarketData("Бетономешалка", 1, 50000, "$", "Транспорт");
        }

        btnPopulation = findViewById(R.id.btnPopulation);
        btnFinances = findViewById(R.id.btnFinances);
        btnScience = findViewById(R.id.btnScience);
        btnSupply = findViewById(R.id.btnSupply);
        btnNextTurn = findViewById(R.id.btnNextTurn);
        tvNotEnoughtMoney = new TextView(getApplicationContext());
        tvNotEnoughtMoney.setText("У Вас недостаточно денег для выплаты зарплат. ЗП списываются с Вашего баланса в рублях. " +
                "Вы можете конвертировать доллары в рубли или уволить кого-то из сотрудников, чтобы исправить проблему. " +
                "Также можно просто начать игру заново. ");
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);
        date = findViewById(R.id.date);

        dialogNotEnoughtMoney = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogNotEnoughtMoney.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogNotEnoughtMoney.setView(tvNotEnoughtMoney);
        dialogNotEnoughtMoney.setButton(DialogInterface.BUTTON_POSITIVE, "Исправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogNotEnoughtMoney.dismiss();
            }
        });
        dialogNotEnoughtMoney.setButton(DialogInterface.BUTTON_NEUTRAL, "Начать заново", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
                dialogNotEnoughtMoney.dismiss();
            }
        });

        dialogNextTurn = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogNextTurn.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogNextTurn = new TextView(this);
        tvForDialogNextTurn.setText("ПОСЛЕ ПЕРЕХОДА НА СЛЕДУЮЩИЙ ХОД: " +
                "\n\n1. С Вашего баланса будут списаны зарплаты работающему населению. " +
                "\n2. Изучений текущей технологии продвинется еще на один месяц. " +
                "\n3. Коэффициенты улучшения могут быть обновлены " +
                "\n4. Используемые теплицы перейдут в следующий статус");
        dialogNextTurn.setView(tvForDialogNextTurn);
        dialogNextTurn.setButton(DialogInterface.BUTTON_POSITIVE, "Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nextTurn() == false) {
                    dialogNotEnoughtMoney.show();
                }
            }
        });

        dialogGameOver = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
//        tvForDialogGameOver = new TextView(this);
//        tvForDialogGameOver.setText("ИГРА ОКОНЧЕНА! \n\nПоздравляем! Вы успешно прошли демо-версию игры Гиперборея. " +
//                "Полная версия выйдет немного позже. " +
//                "Если Вы знаете, как помочь проекту или заметили ошибки в работе приложения, пожалуйста, свяжитесь с нами a.dronina@gmail.com. ");
        //dialogGameOver.setView(R.layout.game_over);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogGameOver.setView(inflater.inflate(R.layout.game_over, null));
        dialogGameOver.setCancelable(false);
        dialogGameOver.setCanceledOnTouchOutside(false);
        dialogGameOver.setButton(DialogInterface.BUTTON_POSITIVE, "Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
            }
        });

        btnPopulation.setOnClickListener(this);
        btnFinances.setOnClickListener(this);
        btnScience.setOnClickListener(this);
        btnSupply.setOnClickListener(this);
        btnNextTurn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (allSettings.getInt("TURN", 0) == 9) {
            dialogGameOver.show();
        }
        ifNewGame = allSettings.getBoolean("NEW_GAME", true);
        if (ifNewGame) {
            Intent popupIntent = new Intent(this, Popup.class);
            startActivity(popupIntent);
        }

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnPopulation:
                intent = new Intent(this, People.class);
                break;
            case R.id.btnFinances:
                intent = new Intent(this, Finances.class);
                break;
            case R.id.btnScience:
                intent = new Intent(this, Sciense.class);
                break;
            case R.id.btnSupply:
                intent = new Intent(this, Supply.class);
                break;
            case R.id.btnNextTurn:
                dialogNextTurn.show();
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

    public boolean nextTurn() {
        boolean result = true;

        //paying salaris and changing finances coef
        Cursor res = db.rawQuery("select * from " + "population", null);
        long salaries = 0;
        ArrayList<Integer> finIds = new ArrayList<>();
        ArrayList<Double> finCoefs = new ArrayList<>();
        ArrayList<Integer> finMonthsWorked = new ArrayList<>();
        while (res.moveToNext()) {
            int finId = res.getInt(0);
            int salary = Integer.parseInt(res.getString(4));
            int monthsWorked = res.getInt(17);
            double finCoef = res.getDouble(18);
            int job = Integer.parseInt(res.getString(3));
            salaries = salaries + salary;
            if (job == 11) {
                finIds.add(finId);
                finCoefs.add(finCoef);
                finMonthsWorked.add(monthsWorked);
            }
        }
        if (allSettings.getLong("MONEY_RUBLES", 0) < salaries) {
            result = false;
        } else {
            //changing month and year if it needs
            int currentMonth = allSettings.getInt("MONTH_ID", 0);
            if (currentMonth == 12) {
                allSettings.edit().putInt("MONTH_ID", 1).apply();
                allSettings.edit().putInt("YEAR", allSettings.getInt("YEAR", 0) + 1).apply();
            } else {
                allSettings.edit().putInt("MONTH_ID", currentMonth + 1).apply();
            }

            //changing money value and finanses coef
            allSettings.edit().putLong("MONEY_RUBLES", allSettings.getLong("MONEY_RUBLES", 0) - salaries).apply();
            for (int i = 0; i < finIds.size(); i++) {
                double newCoef = finCoefs.get(i) + 0.2;
                newCoef = Math.round(newCoef * 10.0) / 10.0;
                if (newCoef > 3.0) {
                    newCoef = 3.0;
                }
                db.execSQL("UPDATE " + "population" + " SET FIN_MONTHS_WORKED='" + finMonthsWorked.get(i) + 1 + "'WHERE ID='" + finIds.get(i) + "'");
                db.execSQL("UPDATE " + "population" + " SET FIN_COEF='" + newCoef + "'WHERE ID='" + finIds.get(i) + "'");
            }
            date.setText(dateAndMoney.getDate(allSettings));
            moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
            moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

            //learning current technology
            if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0) {
                int tecId = allSettings.getInt("TEC_IS_BEEING_LEARNED_ID", 0);
                int monthsLeft = allSettings.getInt("MONTHS_LEFT_TO_LEARN", 0) - 1;
                if (monthsLeft == 0) {
                    db.execSQL("UPDATE " + "tecnologies" + " SET TEC_IS_LEARNED='" + 1 + "'WHERE TEC_ID='" + tecId + "'");
                    allSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                    allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                    allSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", 0).apply();
                } else {
                    allSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", monthsLeft).apply();
                }
            }

            //changing farms statuses
            ArrayList<Farm> farms = new ArrayList<>();
            Cursor farmRes = db.rawQuery("select * from " + "farms", null);
            while (farmRes.moveToNext()) {
                int farmId = Integer.parseInt(farmRes.getString(0));
                String farmName = farmRes.getString(1);
                String farmCrop = farmRes.getString(2);
                int farmStatus = Integer.parseInt(farmRes.getString(3));
                int farmFarmerId = Integer.parseInt(farmRes.getString(4));

                farms.add(new Farm(farmId, farmName, farmCrop, farmStatus, farmFarmerId));
            }
            for (int i = 0; i < farms.size(); i++) {
                if ((farms.get(i).getFarmerId() != 0) && !(farms.get(i).getCrop().equals("Не выбрано"))) {
                    if (farms.get(i).getStatus() == 5) {
                        myDb = new DatabaseHelper(this);
                        myDb.insertStockData(farms.get(i).getCrop(), "Еда", 100);
                        db.execSQL("UPDATE " + "farms" + " SET FARM_STATUS='" + 0 + "'WHERE FARM_ID='" + farms.get(i).getId() + "'");
                    } else {
                        int statusChanged = farms.get(i).getStatus() + 1;
                        db.execSQL("UPDATE " + "farms" + " SET FARM_STATUS='" + statusChanged + "'WHERE FARM_ID='" + farms.get(i).getId() + "'");
                    }
                }
            }

            //changing turn value
            int turnValue = allSettings.getInt("TURN", 0);
            if (turnValue == 9) {
                dialogGameOver.show();
            } else {
                turnValue++;
                allSettings.edit().putInt("TURN", turnValue).apply();
            }
        }
        return result;
    }

    public void startNewGame(){
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
        allSettings.edit().putInt("SCIENTIST_IN_USE_ID", -1).apply();
        allSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
        allSettings.edit().putString("SOLD_TECHNOLOGIES", "").apply();

        getApplicationContext().deleteDatabase("hyperborea.db");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
