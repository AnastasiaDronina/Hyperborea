package org.anastdronina.gyperborea;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Stock extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerPruductType;
    private ArrayAdapter<CharSequence> stockArrayAdapter;
    private ArrayList<Product> products;
    private ListView stockListView;
    private SQLiteDatabase db;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private TextView date, moneyD, moneyR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();

        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);
        spinnerPruductType = findViewById(R.id.spinnerPruductType);
        stockListView = findViewById(R.id.stockListView);
        stockArrayAdapter = ArrayAdapter.createFromResource(this, R.array.product_types, R.layout.spinner_item);
        stockArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPruductType.setAdapter(stockArrayAdapter);
        spinnerPruductType.setOnItemSelectedListener(this);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.stock_header, stockListView, false);
        stockListView.addHeaderView(headerView, null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        products = new ArrayList<>();
        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        Cursor res = db.rawQuery("select * from " + "stock", null);
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String type = res.getString(2);
            int amount = res.getInt(3);

            products.add(new Product(id, name, type, amount));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Не выбрано")) {
            String[] productNames = new String[products.size()];
            String[] productAmounts = new String[products.size()];

            for (int i = 0; i < products.size(); i++) {
                productNames[i] = products.get(i).getName();
                if(products.get(i).getType().equals("Еда")){
                    productAmounts[i] = Integer.toString(products.get(i).getAmount()) + " кг ";
                } else {
                    productAmounts[i] = Integer.toString(products.get(i).getAmount()) + " шт ";
                }

            }
            StockAdapter adapter = new StockAdapter(this, productNames, productAmounts);
            stockListView.setAdapter(adapter);
        }
        if (text.equals("Еда")) {
            ArrayList<Product> foodList = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getType().equals("Еда")) {
                    foodList.add(products.get(i));
                }
            }
            String[] productNames = new String[foodList.size()];
            String[] productAmounts = new String[foodList.size()];

            for (int i = 0; i < foodList.size(); i++) {
                productNames[i] = foodList.get(i).getName();
                productAmounts[i] = Integer.toString(foodList.get(i).getAmount()) + " кг ";

            }
            StockAdapter adapter = new StockAdapter(this, productNames, productAmounts);
            stockListView.setAdapter(adapter);
        }
        if (text.equals("Ресурсы")) {
            ArrayList<Product> resoursesList = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getType().equals("Ресурсы")) {
                    resoursesList.add(products.get(i));
                }
            }
            String[] productNames = new String[resoursesList.size()];
            String[] productAmounts = new String[resoursesList.size()];

            for (int i = 0; i < resoursesList.size(); i++) {
                productNames[i] = resoursesList.get(i).getName();
                productAmounts[i] = Integer.toString(resoursesList.get(i).getAmount()) + " шт ";

            }
            StockAdapter adapter = new StockAdapter(this, productNames, productAmounts);
            stockListView.setAdapter(adapter);
        }
        if (text.equals("Оборудование")) {
            ArrayList<Product> equipmentList = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getType().equals("Оборудование")) {
                    equipmentList.add(products.get(i));
                }
            }
            String[] productNames = new String[equipmentList.size()];
            String[] productAmounts = new String[equipmentList.size()];

            for (int i = 0; i < equipmentList.size(); i++) {
                productNames[i] = equipmentList.get(i).getName();
                productAmounts[i] = Integer.toString(equipmentList.get(i).getAmount()) + " шт ";

            }
            StockAdapter adapter = new StockAdapter(this, productNames, productAmounts);
            stockListView.setAdapter(adapter);
        }
        if (text.equals("Транспорт")) {
            ArrayList<Product> vehiclesList = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getType().equals("Транспорт")) {
                    vehiclesList.add(products.get(i));
                }
            }
            String[] productNames = new String[vehiclesList.size()];
            String[] productAmounts = new String[vehiclesList.size()];

            for (int i = 0; i < vehiclesList.size(); i++) {
                productNames[i] = vehiclesList.get(i).getName();
                productAmounts[i] = Integer.toString(vehiclesList.get(i).getAmount()) + " шт ";

            }
            StockAdapter adapter = new StockAdapter(this, productNames, productAmounts);
            stockListView.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class StockAdapter extends ArrayAdapter<String> {
        Context context;
        String[] productNames;
        String[] productAmounts;

        StockAdapter(Context c, String[] names, String[] amounts) {
            super(c, R.layout.stock_row, R.id.scientistName, names);
            this.context = c;
            this.productNames = names;
            this.productAmounts = amounts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tecnologiesRow = layoutInflater.inflate(R.layout.stock_row, parent, false);
            TextView productName = tecnologiesRow.findViewById(R.id.scientistName);
            TextView productAmount = tecnologiesRow.findViewById(R.id.scientistLevel);

            productName.setText(productNames[position]);
            productAmount.setText(productAmounts[position]);
            return tecnologiesRow;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
