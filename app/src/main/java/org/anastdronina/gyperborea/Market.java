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

public class Market extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvMarket;
    private DateAndMoney dateAndMoney;
    private ArrayList<MarketItem> marketItems;
    private SQLiteDatabase db;
    private int id, amount, price;
    private String name, currency, type;
    private SharedPreferences allSettings;
    private AlertDialog dialogBuyItem;
    private TextView tvForDialogBuyItem, date, moneyR, moneyD;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        myDb = new DatabaseHelper(getApplicationContext());
        lvMarket = findViewById(R.id.lvMarket);
        db = openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dialogBuyItem = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogBuyItem.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogBuyItem = new TextView(this);
        dateAndMoney = new DateAndMoney();
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);

        dialogBuyItem.setTitle("Купить товар? ");
        dialogBuyItem.setView(tvForDialogBuyItem);

        dialogBuyItem.setButton(DialogInterface.BUTTON_POSITIVE, "Купить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int price = allSettings.getInt("CURRENT_ITEM_PRICE", 0);
                String currency = allSettings.getString("CURRENT_ITEM_CURRENCY", "");
                if (currency.equals("руб")) {
                    long moneyRubbles = allSettings.getLong("MONEY_RUBLES", 0);
                    allSettings.edit().putLong("MONEY_RUBLES", moneyRubbles - price).apply();
                } else {
                    long moneyDollars = allSettings.getLong("MONEY_DOLLARS", 0);
                    allSettings.edit().putLong("MONEY_DOLLARS", moneyDollars - price).apply();
                }

                //add to stock
                myDb.insertStockData(
                        allSettings.getString("CURRENT_ITEM_NAME", ""),
                        allSettings.getString("CURRENT_ITEM_TYPE", ""),
                        allSettings.getInt("CURRENT_ITEM_AMOUNT", 0));

                //delete from market
                db.execSQL("delete from market where ITEM_ID='" + allSettings.getInt("CURRENT_ITEM_ID", 0) + "'");

                marketItems = new ArrayList<>();
                Cursor res = db.rawQuery("select * from " + "market", null);
                while (res.moveToNext()) {
                    id = res.getInt(0);
                    name = res.getString(1);
                    amount = res.getInt(2);
                    price = res.getInt(3);
                    currency = res.getString(4);
                    type = res.getString(5);


                    marketItems.add(new MarketItem(id, name, amount, price, currency, type));
                }

                String[] marketItemsNames = new String[marketItems.size()];
                String[] marketItemsAmounts = new String[marketItems.size()];
                String[] marketItemsPrices = new String[marketItems.size()];

                for (int i = 0; i < marketItems.size(); i++) {
                    marketItemsNames[i] = marketItems.get(i).getName();
                    if (marketItems.get(i).getType().equals("Еда")) {
                        marketItemsAmounts[i] = Integer.toString(marketItems.get(i).getAmount()) + " кг ";
                    } else {
                        marketItemsAmounts[i] = Integer.toString(marketItems.get(i).getAmount()) + " шт ";
                    }
                    marketItemsPrices[i] = marketItems.get(i).getPrice() + " " + marketItems.get(i).getCurrency();

                }
                MarketAdapter adapter = new MarketAdapter(getApplicationContext(), marketItemsNames, marketItemsAmounts, marketItemsPrices);
                lvMarket.setAdapter(adapter);
                date.setText(dateAndMoney.getDate(allSettings));
                moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
                moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
            }
        });


        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.market_header, lvMarket, false);
        lvMarket.addHeaderView(headerView, null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        marketItems = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + "market", null);
        while (res.moveToNext()) {
            id = res.getInt(0);
            name = res.getString(1);
            amount = res.getInt(2);
            price = res.getInt(3);
            currency = res.getString(4);
            type = res.getString(5);


            marketItems.add(new MarketItem(id, name, amount, price, currency, type));
        }

        String[] marketItemsNames = new String[marketItems.size()];
        String[] marketItemsAmounts = new String[marketItems.size()];
        String[] marketItemsPrices = new String[marketItems.size()];

        for (int i = 0; i < marketItems.size(); i++) {
            marketItemsNames[i] = marketItems.get(i).getName();
            if (marketItems.get(i).getType().equals("Еда")) {
                marketItemsAmounts[i] = Integer.toString(marketItems.get(i).getAmount()) + " кг ";
            } else {
                marketItemsAmounts[i] = Integer.toString(marketItems.get(i).getAmount()) + " шт ";
            }
            marketItemsPrices[i] = marketItems.get(i).getPrice() + " " + marketItems.get(i).getCurrency();

        }
        MarketAdapter adapter = new MarketAdapter(this, marketItemsNames, marketItemsAmounts, marketItemsPrices);
        lvMarket.setAdapter(adapter);
        lvMarket.setOnItemClickListener(this);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            boolean gotEnoughtMoney = false;
            if (marketItems.get(position - 1).getCurrency().equals("руб")) {
                if (allSettings.getLong("MONEY_RUBLES", 0) >= marketItems.get(position - 1).getPrice()) {
                    gotEnoughtMoney = true;
                }
            }
            if (marketItems.get(position - 1).getCurrency().equals("$")) {
                if (allSettings.getLong("MONEY_DOLLARS", 0) >= marketItems.get(position - 1).getPrice()) {
                    gotEnoughtMoney = true;
                }
            }
            if (!gotEnoughtMoney) {
                Toast.makeText(getApplicationContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
            } else {
                allSettings.edit().putInt("CURRENT_ITEM_ID", marketItems.get(position - 1).getId()).apply();
                allSettings.edit().putString("CURRENT_ITEM_NAME", marketItems.get(position - 1).getName()).apply();
                allSettings.edit().putInt("CURRENT_ITEM_AMOUNT", marketItems.get(position - 1).getAmount()).apply();
                allSettings.edit().putInt("CURRENT_ITEM_PRICE", marketItems.get(position - 1).getPrice()).apply();
                allSettings.edit().putString("CURRENT_ITEM_CURRENCY", marketItems.get(position - 1).getCurrency()).apply();
                allSettings.edit().putString("CURRENT_ITEM_TYPE", marketItems.get(position - 1).getType()).apply();

                String type;

                if (allSettings.getString("CURRENT_ITEM_TYPE", "").equals("Еда")) {
                    type = "кг";
                } else {
                    type = "шт";
                }

                tvForDialogBuyItem.setText("\n" + allSettings.getString("CURRENT_ITEM_NAME", "") + " "
                        + allSettings.getInt("CURRENT_ITEM_AMOUNT", 0)
                        + " " + type + "\n\nЦена: "
                        + allSettings.getInt("CURRENT_ITEM_PRICE", 0) + " "
                        + allSettings.getString("CURRENT_ITEM_CURRENCY", ""));
                dialogBuyItem.show();

            }
        }
    }

    class MarketAdapter extends ArrayAdapter<String> {
        Context context;
        String[] itemsNames;
        String[] itemsAmounts;
        String[] itemsPrices;

        MarketAdapter(Context c, String[] names, String[] amounts, String[] prices) {
            super(c, R.layout.market_row, R.id.marketItemName, names);
            this.context = c;
            this.itemsNames = names;
            this.itemsAmounts = amounts;
            this.itemsPrices = prices;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View marketRow = layoutInflater.inflate(R.layout.market_row, parent, false);
            TextView marketItemName = marketRow.findViewById(R.id.marketItemName);
            TextView marketItemAmount = marketRow.findViewById(R.id.marketItemAmount);
            TextView marketItemPrice = marketRow.findViewById(R.id.marketItemPrice);

            marketItemName.setText(itemsNames[position]);
            marketItemAmount.setText(itemsAmounts[position]);
            marketItemPrice.setText(itemsPrices[position]);
            return marketRow;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
