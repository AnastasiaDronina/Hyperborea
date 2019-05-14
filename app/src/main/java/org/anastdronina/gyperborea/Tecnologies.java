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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Tecnologies extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList<Tecnology> tecs, tecnologies;
    private ArrayList<Person> scientists;
    private ListView tecnologiesList, lvChangeScientist;
    private AlertDialog dialogLearnTec, dialogAboutTec, dialogChangeScientist, dialogStopLearning;
    private TextView learnTecView, aboutTecView, learningTecInfo, pinnedScientist, date, moneyR, moneyD, tvForDialodStopLearning;
    private Button changeScientist, btnStopLearning;
    private SharedPreferences allSettings;
    private SQLiteDatabase db;
    private DateAndMoney dateAndMoney;
    private ImageButton btnToPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnologies);

        dialogStopLearning = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogStopLearning.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialodStopLearning = new TextView(getApplicationContext());
        tvForDialodStopLearning.setText("Изучение текущей технологии будет остановлено, " +
                "закрепленный ученый будет сброшен, " +
                "но прогресс не будет сохранен. " +
                "То есть если Вы решите изучать эту технологию, нужно будет начать сначала. ");
        dateAndMoney = new DateAndMoney();
        dialogLearnTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogLearnTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogAboutTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogAboutTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogChangeScientist = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogChangeScientist.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        learnTecView = new TextView(this);
        aboutTecView = new TextView(this);
        lvChangeScientist = new ListView(this);
        pinnedScientist = findViewById(R.id.pinnedScientist);
        learningTecInfo = findViewById(R.id.sellTecInfo);
        changeScientist = findViewById(R.id.changeScientist);
        btnStopLearning = findViewById(R.id.btnStopLearning);
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);
        btnToPeople = findViewById(R.id.btnToPeople);
        btnToPeople.setOnClickListener(this);
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

        tecnologiesList = findViewById(R.id.tecnologies_list);
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.tecnologies_header, tecnologiesList, false);
        tecnologiesList.addHeaderView(headerView, null, false);
        ViewGroup headerView2 = (ViewGroup) getLayoutInflater().inflate(R.layout.scientists_header, lvChangeScientist, false);
        lvChangeScientist.addHeaderView(headerView2, null, false);

        dialogLearnTec.setTitle("Изучить технологию?");
        dialogLearnTec.setView(learnTecView);
        dialogAboutTec.setTitle("Информация о технологии");
        dialogAboutTec.setView(aboutTecView);
        dialogChangeScientist.setTitle("Выберите ученого из списка: ");
        dialogChangeScientist.setView(lvChangeScientist);
        dialogStopLearning.setTitle("Изучения технологии будет прервано ");
        dialogStopLearning.setView(tvForDialodStopLearning);


        dialogLearnTec.setButton(DialogInterface.BUTTON_POSITIVE, "Начать изучение", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int tecId = allSettings.getInt("CURRENT_TEC_ID", 0);
                String tecName = allSettings.getString("CURRENT_TEC_NAME", "");
                int tecMonths = allSettings.getInt("CURRENT_TEC_MONTHS", 0);
                long tecPrice = allSettings.getLong("CURRENT_TEC_PRICE", 0);
                allSettings.edit().putString("TEC_IS_BEEING_LEARNED", tecName).apply();
                allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", tecId).apply();
                allSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", tecMonths).apply();
                allSettings.edit().putLong("TEC_PRICE", tecPrice).apply();
                for (int i = 0; i < tecnologies.size(); i++) {
                    if (tecnologies.get(i).getName().equals(tecName)) {
                        tecnologies.remove(i);
                        break;
                    }
                }
                String[] names = new String[tecnologies.size()];
                String[] monthsToLearn = new String[tecnologies.size()];
                String[] prices = new String[tecnologies.size()];

                for (int i = 0; i < tecnologies.size(); i++) {
                    names[i] = tecnologies.get(i).getName();
                    monthsToLearn[i] = Integer.toString(tecnologies.get(i).getMonthsToLearn()) + " мес";
                    prices[i] = Long.toString(tecnologies.get(i).getPrice()) + " $";

                }

                TecAdapter adapter = new TecAdapter(getApplicationContext(), names, monthsToLearn, prices);
                tecnologiesList.setAdapter(adapter);
                learningTecInfo.setText("В процессе изучения технология: " + tecName
                        + " \nОсталось: " + tecMonths + " мес \nЦена продажи: " + tecPrice + " $");
            }
        });

        dialogAboutTec.setButton(DialogInterface.BUTTON_POSITIVE, "Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogStopLearning.setButton(DialogInterface.BUTTON_POSITIVE, "Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                allSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                allSettings.edit().putInt("SCIENTIST_IN_USE_ID", 0).apply();
                allSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
                printTechs();
                learningTecInfo.setText("");
            }
        });

        dialogStopLearning.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        changeScientist.setOnClickListener(this);
        btnStopLearning.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        printTechs();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            if (allSettings.getInt("SCIENTIST_IN_USE_ID", 0) == 0) {
                Toast.makeText(getApplicationContext(), "Для изучения необходимо сначала выбрать ученого! ", Toast.LENGTH_SHORT).show();
            } else {
                allSettings.edit().putInt("CURRENT_TEC_ID", tecnologies.get(position - 1).getId()).apply();
                allSettings.edit().putString("CURRENT_TEC_NAME", tecnologies.get(position - 1).getName()).apply();
                allSettings.edit().putString("CURRENT_TEC_DESCRIPTION", tecnologies.get(position - 1).getDescription()).apply();
                allSettings.edit().putInt("CURRENT_TEC_MONTHS", tecnologies.get(position - 1).getMonthsToLearn()).apply();
                allSettings.edit().putLong("CURRENT_TEC_PRICE", tecnologies.get(position - 1).getPrice()).apply();
                allSettings.edit().putBoolean("CURRENT_TEC_ISLEARNED", tecnologies.get(position - 1).isLearned()).apply();

                if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() == 0) {
                    learnTecView.setText("\n" + allSettings.getString("CURRENT_TEC_NAME", "").toUpperCase() + "\n\nОписание: \n"
                            + allSettings.getString("CURRENT_TEC_DESCRIPTION", "") + "\n\nДля изучения необходимо: "
                            + allSettings.getInt("CURRENT_TEC_MONTHS", 0) + " мес. \n\nЦена продажи: "
                            + allSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
                    dialogLearnTec.show();
                } else {
                    aboutTecView.setText("\n" + allSettings.getString("CURRENT_TEC_NAME", "").toUpperCase() + "\n\nОписание: \n"
                            + allSettings.getString("CURRENT_TEC_DESCRIPTION", "") + "\n\nДля изучения необходимо: "
                            + allSettings.getInt("CURRENT_TEC_MONTHS", 0) + " мес. \n\nЦена продажи: "
                            + allSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
                    dialogAboutTec.show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeScientist:
                if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() == 0) {
                    scientists = new ArrayList<>();
                    db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
                    Cursor res = db.rawQuery("select * from " + "population", null);
                    while (res.moveToNext()) {
                        int id = Integer.parseInt(res.getString(0));
                        String name = res.getString(1);
                        String surname = res.getString(2);
                        int job = Integer.parseInt(res.getString(3));
                        int salary = Integer.parseInt(res.getString(4));
                        int age = Integer.parseInt(res.getString(5));
                        int building = Integer.parseInt(res.getString(6));
                        int manufacture = Integer.parseInt(res.getString(7));
                        int farm = Integer.parseInt(res.getString(8));
                        int athletic = Integer.parseInt(res.getString(9));
                        int learning = Integer.parseInt(res.getString(10));
                        int talking = Integer.parseInt(res.getString(11));
                        int strength = Integer.parseInt(res.getString(12));
                        int art = Integer.parseInt(res.getString(13));
                        String trait1 = res.getString(14);
                        String trait2 = res.getString(15);
                        String trait3 = res.getString(16);

                        if (job == 6) {
                            scientists.add(new Person(id, name, surname, job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art,
                                    new ArrayList<>(Arrays.asList(trait1, trait2, trait3))));
                        }
                    }
                    String[] scientistNames = new String[scientists.size()];
                    String[] scientistLevels = new String[scientists.size()];

                    for (int i = 0; i < scientists.size(); i++) {
                        scientistNames[i] = scientists.get(i).getName() + " " + scientists.get(i).getSurname();
                        scientistLevels[i] = Integer.toString(scientists.get(i).getLearning());
                    }

                    ScientistAdapter adapter = new ScientistAdapter(this, scientistNames, scientistLevels);
                    lvChangeScientist.setAdapter(adapter);
                    lvChangeScientist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            allSettings.edit().putInt("SCIENTIST_IN_USE_ID", scientists.get(position - 1).getId()).apply();
                            allSettings.edit().putString("SCIENTIST_IN_USE_NAME", scientists.get(position - 1).getName() + " " + scientists.get(position - 1).getSurname()).apply();
                            pinnedScientist.setText("Для изучения закреплен ученый: "
                                    + allSettings.getString("SCIENTIST_IN_USE_NAME", ""));
                            dialogChangeScientist.dismiss();
                        }
                    });

                    dialogChangeScientist.show();
                } else
                    Toast.makeText(getApplicationContext(), "Изменение ученого в момент изучения технологии невозможно.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStopLearning:
                if(allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0
                        || allSettings.getString("SCIENTIST_IN_USE_NAME", "").length() > 0){
                    dialogStopLearning.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Пока сбрасывать нечего ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnToPeople:
                Intent intent = new Intent(this, People.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            default:
        }
    }

    class TecAdapter extends ArrayAdapter<String> {
        Context context;
        String[] tecNames;
        String[] tecMonths;
        String[] tecPrices;

        TecAdapter(Context c, String[] names, String[] monthsToLearn, String[] prices) {
            super(c, R.layout.tecnologies_row, R.id.tec_name, names);
            this.context = c;
            this.tecNames = names;
            this.tecMonths = monthsToLearn;
            this.tecPrices = prices;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tecnologiesRow = layoutInflater.inflate(R.layout.tecnologies_row, parent, false);
            TextView rowName = tecnologiesRow.findViewById(R.id.tec_name);
            TextView rowMonths = tecnologiesRow.findViewById(R.id.tec_months);
            TextView rowPrices = tecnologiesRow.findViewById(R.id.tec_price);

            rowName.setText(tecNames[position]);
            rowMonths.setText(tecMonths[position]);
            rowPrices.setText(tecPrices[position]);
            return tecnologiesRow;
        }
    }

    class ScientistAdapter extends ArrayAdapter<String> {
        Context context;
        String[] sciNames;
        String[] sciLevels;

        ScientistAdapter(Context c, String[] names, String[] levels) {
            super(c, R.layout.scientists_row, R.id.scientistName, names);
            this.context = c;
            this.sciNames = names;
            this.sciLevels = levels;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View scientistsRow = layoutInflater.inflate(R.layout.scientists_row, parent, false);
            TextView scientistName = scientistsRow.findViewById(R.id.scientistName);
            TextView scientistLevel = scientistsRow.findViewById(R.id.scientistLevel);

            scientistName.setText(sciNames[position]);
            scientistLevel.setText(sciLevels[position]);
            return scientistsRow;
        }
    }

    public ArrayList<Tecnology> changeTecnologiesList(ArrayList<Tecnology> tecnologies) {
        boolean tec1IsLearned = false, tec2IsLearned = false, tec3IsLearned = false, tec4IsLearned = false, tec5IsLearned = false;

        int i;

        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec1") && tecnologies.get(i).isLearned() == true) {
                tec1IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec2") && tecnologies.get(i).isLearned() == true) {
                tec2IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec3") && tecnologies.get(i).isLearned() == true) {
                tec3IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec4") && tecnologies.get(i).isLearned() == true) {
                tec4IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec5") && tecnologies.get(i).isLearned() == true) {
                tec5IsLearned = true;
                tecnologies.remove(i);
            }
        }

        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec1.1") && !tec1IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec2.1") && !tec2IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec3.1") && !tec3IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec4.1") && !tec4IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec5.1") && !tec5IsLearned) {
                tecnologies.remove(i);
            }
        }

        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals(allSettings.getString("TEC_IS_BEEING_LEARNED", ""))) {
                tecnologies.remove(i);
            }
        }
        return tecnologies;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void printTechs() {
        int sciId = allSettings.getInt("SCIENTIST_IN_USE_ID", -1);
        if (sciId != -1) {
            pinnedScientist.setText("Для изучения закреплен ученый: "
                    + allSettings.getString("SCIENTIST_IN_USE_NAME", ""));
        } else pinnedScientist.setText("Для изучения закреплен ученый: Не выбрано");
        tecs = new ArrayList<>();
        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        Cursor res = db.rawQuery("select * from " + "tecnologies", null);
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String description = res.getString(2);
            int monthsToLearn = res.getInt(3);
            int price = res.getInt(4);
            int isLearnedInt = res.getInt(5);
            boolean isLearned = false;
            if (isLearnedInt == 1) {
                isLearned = true;
            }
//            if(!isLearned){
            tecs.add(new Tecnology(id, name, description, monthsToLearn, price, isLearned));
//            }
        }
        //убираем из списка уже изученные технологии и заменяем их на следующие этого же типа
        tecnologies = changeTecnologiesList(tecs);

        if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0) {
            String tecInLearning = allSettings.getString("TEC_IS_BEEING_LEARNED", "");
            int monthsLeftToLearn = allSettings.getInt("MONTHS_LEFT_TO_LEARN", 0);
            long price = allSettings.getLong("TEC_PRICE", 0);
            learningTecInfo.setText("В процессе изучения технология: " + tecInLearning
                    + " \nОсталось: " + monthsLeftToLearn + " мес \nЦена продажи: " + price + " $");
        }

        String[] names = new String[tecnologies.size()];
        String[] monthsToLearn = new String[tecnologies.size()];
        String[] prices = new String[tecnologies.size()];

        for (int i = 0; i < tecnologies.size(); i++) {
            names[i] = tecnologies.get(i).getName();
            monthsToLearn[i] = Integer.toString(tecnologies.get(i).getMonthsToLearn()) + " мес";
            prices[i] = Long.toString(tecnologies.get(i).getPrice()) + " $";

        }

        TecAdapter adapter = new TecAdapter(this, names, monthsToLearn, prices);
        tecnologiesList.setAdapter(adapter);
        tecnologiesList.setOnItemClickListener(this);
    }
}
