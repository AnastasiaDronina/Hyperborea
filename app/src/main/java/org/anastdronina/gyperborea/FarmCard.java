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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class FarmCard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences allSettings;
    private TextView tvStatus, tvFarmFarmer, tvFarmName;
    private Button btnChangeFarmer;
    private ImageButton btnToPeople;
    private EditText editFarmName;
    private Spinner spinnerCrops;
    private ArrayAdapter<CharSequence> adapter;
    private SQLiteDatabase db;
    private int farmId, farmFarmerId, farmStatus;
    private String farmName, farmCrop;
    private Person farmer;
    private ArrayList<Person> farmersAvailable;
    private ListView lvChangeFarmer;
    private AlertDialog dialogChangeFarmer, dialogChangeFarmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_card);

        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        tvStatus = findViewById(R.id.tvStatus);
        tvFarmFarmer = findViewById(R.id.tvFarmFarmer);
        tvFarmName = findViewById(R.id.tvFarmName);
        editFarmName = new EditText(this);
        btnChangeFarmer = findViewById(R.id.btnChangeFarmer);
        btnToPeople = findViewById(R.id.btnToPeople);
        spinnerCrops = findViewById(R.id.spinnerCrops);
        lvChangeFarmer = new ListView(this);
        dialogChangeFarmer = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogChangeFarmName = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();

        dialogChangeFarmer.setTitle("Выберите фермера из списка: ");
        dialogChangeFarmer.setView(lvChangeFarmer);
        dialogChangeFarmName.setTitle("Изменить название теплицы ");
        dialogChangeFarmName.setView(editFarmName);

        dialogChangeFarmName.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvFarmName.setText(editFarmName.getText());
                db.execSQL("UPDATE " + "farms" + " SET FARM_NAME='" + editFarmName.getText() + "'WHERE FARM_ID='" + farmId + "'");
            }
        });
        tvFarmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFarmName.setText(tvFarmName.getText());
                dialogChangeFarmName.show();
            }
        });

        adapter = ArrayAdapter.createFromResource(this, R.array.crops, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrops.setAdapter(adapter);
        spinnerCrops.setOnItemSelectedListener(this);
        btnToPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), People.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btnChangeFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allSettings.getInt("CURRENT_FARM_STATUS", 0) != 0) {
                    Toast.makeText(getApplicationContext(), "Переназначить фермера можно будет после сбора урожая ", Toast.LENGTH_SHORT).show();
                } else {
                    farmersAvailable = new ArrayList<>();
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

                        if (job == 4) {
                            farmersAvailable.add(new Person(id, name, surname, job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art,
                                    new ArrayList<>(Arrays.asList(trait1, trait2, trait3))));
                        }
                    }

                    String[] farmersNames = new String[farmersAvailable.size()];
                    String[] farmersLevels = new String[farmersAvailable.size()];

                    for (int i = 0; i < farmersAvailable.size(); i++) {
                        farmersNames[i] = farmersAvailable.get(i).getName() + " " + farmersAvailable.get(i).getSurname();
                        farmersLevels[i] = Integer.toString(farmersAvailable.get(i).getFarm());
                    }

                    FarmerAdapter adapterForFarmers = new FarmerAdapter(getApplicationContext(), farmersNames, farmersLevels);
                    lvChangeFarmer.setAdapter(adapterForFarmers);
                    lvChangeFarmer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int pinnedFarmerId = farmersAvailable.get(position - 1).getId();
                            allSettings.edit().putString("CURRENT_FARM_FARMER_NAME", farmersAvailable.get(position - 1).getName() + " " + farmersAvailable.get(position - 1).getSurname()).apply();
                            tvFarmFarmer.setText("Ответственный фермер: "
                                    + allSettings.getString("CURRENT_FARM_FARMER_NAME", ""));
                            db.execSQL("UPDATE " + "farms" + " SET FARM_FARMER_ID='" + pinnedFarmerId + "'WHERE FARM_ID='" + farmId + "'");
                            allSettings.edit().putInt("FARMER_IN_USE_ID", pinnedFarmerId).apply();

                            dialogChangeFarmer.dismiss();
                        }
                    });
                    dialogChangeFarmer.show();
                }
            }
        });

        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.scientists_header, lvChangeFarmer, false);
        lvChangeFarmer.addHeaderView(headerView, null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        farmId = allSettings.getInt("CURRENT_FARM_ID", 0);
        farmName = allSettings.getString("CURRENT_FARM_NAME", "");
        farmCrop = allSettings.getString("CURRENT_FARM_CROP", "");
        farmFarmerId = allSettings.getInt("CURRENT_FARM_FARMER_ID", 0);
        farmStatus = allSettings.getInt("CURRENT_FARM_STATUS", 0);

        Farm currentFarm = new Farm(farmId, farmName, farmCrop, farmStatus, farmFarmerId);

        tvFarmName.setText(farmName);
        tvStatus.setText("Статус: " + currentFarm.statusString(farmStatus));
        int crop = -1;
        switch (farmCrop) {
            case "Не выбрано":
                crop = 0;
                break;
            case "Огурцы":
                crop = 1;
                break;
            case "Картофель":
                crop = 2;
                break;
            case "Помидоры":
                crop = 3;
                break;
            case "Пшеница":
                crop = 4;
                break;
            case "Рожь":
                crop = 5;
                break;
            case "Лук":
                crop = 6;
                break;
            case "Морковь":
                crop = 7;
                break;
            case "Укроп":
                crop = 8;
                break;
            case "Свёкла":
                crop = 9;
                break;
        }
        spinnerCrops.setSelection(crop);

        if (farmFarmerId == 0) {
            tvFarmFarmer.setText("Ответственный фермер: Не назначен");
        } else {
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

                if (id == farmFarmerId) {
                    farmer = new Person(id, name, surname, job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art,
                            new ArrayList<String>(Arrays.asList(trait1, trait2, trait3)));
                }
            }
            tvFarmFarmer.setText("Ответственный фермер: " + farmer.getName() + " " + farmer.getSurname());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (allSettings.getInt("CURRENT_FARM_STATUS", 0) != 0) {
            if(!allSettings.getString("CURRENT_FARM_CROP", "").equals(text)){
                Toast.makeText(getApplicationContext(), "Выращиваемую культуру можно будет изменить только после сбора урожая", Toast.LENGTH_SHORT).show();
                int previousPosition = Arrays.asList((getResources().getStringArray(R.array.crops))).indexOf(allSettings.getString("CURRENT_FARM_CROP", ""));
                spinnerCrops.setSelection(previousPosition);
            }
        } else {
            if (text.equals("Не выбрано")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Не выбрано" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Огурцы")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Огурцы" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Картофель")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Картофель" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Помидоры")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Помидоры" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Пшеница")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Пшеница" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Рожь")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Рожь" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Лук")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Лук" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Морковь")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Морковь" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Укроп")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Укроп" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Свёкла")) {
                db.execSQL("UPDATE " + "farms" + " SET FARM_CROP='" + "Свёкла" + "'WHERE FARM_ID='" + farmId + "'");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class FarmerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] fNames;
        String[] fLevels;

        FarmerAdapter(Context c, String[] names, String[] levels) {
            super(c, R.layout.scientists_row, R.id.scientistName, names);
            this.context = c;
            this.fNames = names;
            this.fLevels = levels;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View scientistsRow = layoutInflater.inflate(R.layout.scientists_row, parent, false);
            TextView scientistName = scientistsRow.findViewById(R.id.scientistName);
            TextView scientistLevel = scientistsRow.findViewById(R.id.scientistLevel);

            scientistName.setText(fNames[position]);
            scientistLevel.setText(fLevels[position]);
            return scientistsRow;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
