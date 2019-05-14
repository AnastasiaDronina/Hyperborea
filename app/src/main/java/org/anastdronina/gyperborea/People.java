package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class People extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Person> population;
//    private ArrayList<Finansist> finansists;
    private ListView peopleList;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private TextView date, moneyD, moneyR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        date = findViewById(R.id.date);
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);

        peopleList = findViewById(R.id.peopleList);
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.people_header, peopleList, false);
        peopleList.addHeaderView(headerView, null, false);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        population = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
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

            population.add(new Person(id, name, surname, job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art,
                    new ArrayList<>(Arrays.asList(trait1, trait2, trait3))));
        }
        String[] namesAndSurnames = new String[population.size()];
        String[] jobs = new String[population.size()];
        String[] salaries = new String[population.size()];
        String[] ages = new String[population.size()];
        String[] jobsList = getResources().getStringArray(R.array.jobs);

        for (int i = 0; i < population.size(); i++) {
            namesAndSurnames[i] = population.get(i).getName() + "\n" + population.get(i).getSurname();
            jobs[i] = jobsList[population.get(i).getJob()];
            salaries[i] = Integer.toString(population.get(i).getSalary());
            ages[i] = Integer.toString(population.get(i).getAge());
        }

        MyAdapter adapter = new MyAdapter(this, namesAndSurnames, jobs, salaries, ages);
        peopleList.setAdapter(adapter);
        peopleList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            allSettings.edit().putInt("CURRENT_PERS_ID", population.get(position - 1).getId()).apply();
            allSettings.edit().putString("CURRENT_PERS_NAME", population.get(position - 1).getName()).apply();
            allSettings.edit().putString("CURRENT_PERS_SURNAME", population.get(position - 1).getSurname()).apply();
            allSettings.edit().putInt("CURRENT_PERS_JOB", population.get(position - 1).getJob()).apply();
            allSettings.edit().putInt("CURRENT_PERS_SALARY", population.get(position - 1).getSalary()).apply();
            allSettings.edit().putInt("CURRENT_PERS_AGE", population.get(position - 1).getAge()).apply();
            allSettings.edit().putInt("CURRENT_PERS_BUILDING", population.get(position - 1).getBuilding()).apply();
            allSettings.edit().putInt("CURRENT_PERS_MANUFACTURE", population.get(position - 1).getManufacture()).apply();
            allSettings.edit().putInt("CURRENT_PERS_FARM", population.get(position - 1).getFarm()).apply();
            allSettings.edit().putInt("CURRENT_PERS_ATHLETIC", population.get(position - 1).getAthletic()).apply();
            allSettings.edit().putInt("CURRENT_PERS_LEARNING", population.get(position - 1).getLearning()).apply();
            allSettings.edit().putInt("CURRENT_PERS_TALKING", population.get(position - 1).getTalking()).apply();
            allSettings.edit().putInt("CURRENT_PERS_STRENGTH", population.get(position - 1).getStrength()).apply();
            allSettings.edit().putInt("CURRENT_PERS_ART", population.get(position - 1).getArt()).apply();
            allSettings.edit().putString("CURRENT_PERS_TRAIT_1", population.get(position - 1).getTraits().get(0)).apply();
            allSettings.edit().putString("CURRENT_PERS_TRAIT_2", population.get(position - 1).getTraits().get(1)).apply();
            allSettings.edit().putString("CURRENT_PERS_TRAIT_3", population.get(position - 1).getTraits().get(2)).apply();

            startActivity(new Intent(this, PersonCard.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String[] myNamesAndSurnames;
        String[] myJobs;
        String[] myAges;
        String[] mySalaries;

        MyAdapter(Context c, String[] namesAndSurnames, String[] jobs, String[] salaries, String[] ages) {
            super(c, R.layout.people_row, R.id.scientistName, namesAndSurnames);
            this.context = c;
            this.myNamesAndSurnames = namesAndSurnames;
            this.myJobs = jobs;
            this.mySalaries = salaries;
            this.myAges = ages;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View peopleRow = layoutInflater.inflate(R.layout.people_row, parent, false);
            TextView rowName = peopleRow.findViewById(R.id.scientistName);
            TextView rowJob = peopleRow.findViewById(R.id.farmsCrop);
            TextView rowSalary = peopleRow.findViewById(R.id.farmsStatus);
            TextView rowAge = peopleRow.findViewById(R.id.scientistLevel);

            rowName.setText(myNamesAndSurnames[position]);
            rowJob.setText(myJobs[position]);
            rowSalary.setText(mySalaries[position]);
            rowAge.setText(myAges[position]);
            return peopleRow;
        }
    }
}