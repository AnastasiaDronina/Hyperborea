package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class SellTechnology extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView sellTecInfo, sellTecView, date, moneyD, moneyR;
    private SQLiteDatabase db;
    private ListView learnedTecList;
    private ArrayList<Tecnology> learnedTechs;
    private AlertDialog dialogSellTec;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private boolean isSold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_technology);

        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        sellTecInfo = findViewById(R.id.sellTecInfo);
        learnedTecList = findViewById(R.id.learnedTecList);
        dialogSellTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogSellTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        sellTecView = new TextView(this);
        date = findViewById(R.id.date);
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);
        dateAndMoney = new DateAndMoney();

        dialogSellTec.setTitle("Продать технологию?");
        dialogSellTec.setView(sellTecView);

        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.learned_tech_header, learnedTecList, false);
        learnedTecList.addHeaderView(headerView, null, false);

        dialogSellTec.setButton(DialogInterface.BUTTON_POSITIVE, "Продать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tecName = allSettings.getString("CURRENT_TEC_NAME", "");
                long tecPrice = allSettings.getLong("CURRENT_TEC_PRICE", 0);
                long res = allSettings.getLong("MONEY_DOLLARS", 0) + tecPrice;
                allSettings.edit().putLong("MONEY_DOLLARS", res).apply();
                for (int i = 0; i < learnedTechs.size(); i++) {
                    if (learnedTechs.get(i).getName().equals(tecName)) {
                        int id = learnedTechs.get(i).getId();
                        allSettings.edit().putString("SOLD_TECHNOLOGIES", allSettings.getString("SOLD_TECHNOLOGIES", "") + id + ",").apply();
                        learnedTechs.remove(i);
                        break;
                    }
                }
                String[] names = new String[learnedTechs.size()];
                String[] prices = new String[learnedTechs.size()];

                for (int i = 0; i < learnedTechs.size(); i++) {
                    names[i] = learnedTechs.get(i).getName();
                    prices[i] = Long.toString(learnedTechs.get(i).getPrice()) + " $";
                }

                LearnedTecAdapter adapter = new LearnedTecAdapter(getApplicationContext(), names, prices);
                learnedTecList.setAdapter(adapter);

                if (learnedTechs.size() == 0) {
                    sellTecInfo.setText("У вас пока нет изученных технологий. ");
                }

                date.setText(dateAndMoney.getDate(allSettings));
                moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
                moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] soldTechnologies = allSettings.getString("SOLD_TECHNOLOGIES", "").split(",");
        learnedTechs = new ArrayList<>();
        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        Cursor res = db.rawQuery("select * from " + "tecnologies", null);
        while (res.moveToNext()) {
            isSold = false;
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
            for (int i = 0; i < soldTechnologies.length; i++) {
                if (soldTechnologies[i].equals(Integer.toString(id))) {
                    isSold = true;
                }
            }
            if (isLearned && !isSold) {
                learnedTechs.add(new Tecnology(id, name, description, monthsToLearn, price, isLearned));
            }
        }
        if (learnedTechs.size() > 0) {
            sellTecInfo.setText("");
            String[] names = new String[learnedTechs.size()];
            String[] monthsToLearn = new String[learnedTechs.size()];
            String[] prices = new String[learnedTechs.size()];

            for (int i = 0; i < learnedTechs.size(); i++) {
                names[i] = learnedTechs.get(i).getName();
                monthsToLearn[i] = Integer.toString(learnedTechs.get(i).getMonthsToLearn()) + " мес";
                prices[i] = Long.toString(learnedTechs.get(i).getPrice()) + " $";

            }

            LearnedTecAdapter adapter = new LearnedTecAdapter(this, names, prices);
            learnedTecList.setAdapter(adapter);
            learnedTecList.setOnItemClickListener(this);
        } else sellTecInfo.setText("У вас пока нет технологий для продажи. ");

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            allSettings.edit().putInt("CURRENT_TEC_ID", learnedTechs.get(position - 1).getId()).apply();
            allSettings.edit().putString("CURRENT_TEC_NAME", learnedTechs.get(position - 1).getName()).apply();
            allSettings.edit().putString("CURRENT_TEC_DESCRIPTION", learnedTechs.get(position - 1).getDescription()).apply();
            allSettings.edit().putInt("CURRENT_TEC_MONTHS", learnedTechs.get(position - 1).getMonthsToLearn()).apply();
            allSettings.edit().putLong("CURRENT_TEC_PRICE", learnedTechs.get(position - 1).getPrice()).apply();
            allSettings.edit().putBoolean("CURRENT_TEC_ISLEARNED", learnedTechs.get(position - 1).isLearned()).apply();

            sellTecView.setText("\n" + allSettings.getString("CURRENT_TEC_NAME", "").toUpperCase() + "\n\nОписание: \n"
                    + allSettings.getString("CURRENT_TEC_DESCRIPTION", "") + "\n\nДля изучения необходимо: "
                    + allSettings.getInt("CURRENT_TEC_MONTHS", 0) + " мес. \n\nЦена продажи: "
                    + allSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
            dialogSellTec.show();
        }
    }

    class LearnedTecAdapter extends ArrayAdapter<String> {
        Context context;
        String[] tecNames;
        String[] tecPrices;

        LearnedTecAdapter(Context c, String[] names, String[] prices) {
            super(c, R.layout.tecnologies_row, R.id.tec_name, names);
            this.context = c;
            this.tecNames = names;
            this.tecPrices = prices;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tecnologiesRow = layoutInflater.inflate(R.layout.tecnologies_row, parent, false);
            TextView rowName = tecnologiesRow.findViewById(R.id.tec_name);
            TextView rowPrices = tecnologiesRow.findViewById(R.id.tec_price);

            rowName.setText(tecNames[position]);
            rowPrices.setText(tecPrices[position]);
            return tecnologiesRow;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
