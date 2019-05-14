package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Agriculture extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView agricultureLv;
    private ArrayList<Farm> farmsList;
    private SQLiteDatabase db;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private TextView date, moneyR, moneyD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);

        agricultureLv = findViewById(R.id.agricultureList);
        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.farms_header, agricultureLv, false);
        agricultureLv.addHeaderView(headerView, null, false);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        farmsList = new ArrayList<>();

        Cursor res = db.rawQuery("select * from " + "farms", null);
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            String name = res.getString(1);
            String crop = res.getString(2);
            int status = Integer.parseInt(res.getString(3));
            int farmerId = Integer.parseInt(res.getString(4));

            farmsList.add(new Farm(id, name, crop, status, farmerId));
        }

        String[] fNames = new String[farmsList.size()];
        String[] fCrops = new String[farmsList.size()];
        String[] fStatuses = new String[farmsList.size()];

        for (int i = 0; i < farmsList.size(); i++) {
            fNames[i] = farmsList.get(i).getName();
            fCrops[i] = farmsList.get(i).getCrop();
            fStatuses[i] = farmsList.get(i).statusString(farmsList.get(i).getStatus());
        }

        FarmAdapter adapter = new FarmAdapter(this, fNames, fCrops, fStatuses);
        agricultureLv.setAdapter(adapter);
        agricultureLv.setOnItemClickListener(this);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            allSettings.edit().putInt("CURRENT_FARM_ID", farmsList.get(position - 1).getId()).apply();
            allSettings.edit().putString("CURRENT_FARM_NAME", farmsList.get(position - 1).getName()).apply();
            allSettings.edit().putString("CURRENT_FARM_CROP", farmsList.get(position - 1).getCrop()).apply();
            allSettings.edit().putInt("CURRENT_FARM_STATUS", farmsList.get(position - 1).getStatus()).apply();
            allSettings.edit().putInt("CURRENT_FARM_FARMER_ID", farmsList.get(position - 1).getFarmerId()).apply();

            startActivity(new Intent(this, FarmCard.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    class FarmAdapter extends ArrayAdapter<String> {
        Context context;
        String[] names;
        String[] crops;
        String[] statuses;

        FarmAdapter(Context c, String[] names, String[] crops, String[] statuses) {
            super(c, R.layout.farms_row, R.id.scientistName, names);
            this.context = c;
            this.names = names;
            this.crops = crops;
            this.statuses = statuses;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View farmsRow = layoutInflater.inflate(R.layout.farms_row, parent, false);
            TextView rowName = farmsRow.findViewById(R.id.scientistName);
            TextView rowCrop = farmsRow.findViewById(R.id.farmsCrop);
            TextView rowStatus = farmsRow.findViewById(R.id.farmsStatus);

            rowName.setText(names[position]);
            rowCrop.setText(crops[position]);
            rowStatus.setText(statuses[position]);
            return farmsRow;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
