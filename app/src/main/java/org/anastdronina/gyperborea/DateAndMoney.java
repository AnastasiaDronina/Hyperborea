package org.anastdronina.gyperborea;

import android.content.SharedPreferences;

import java.text.DecimalFormat;

public class DateAndMoney {
    public String getDate(SharedPreferences allSettings) {
        String month = "";
        int monthId = allSettings.getInt("MONTH_ID", 1);
        int yearId = allSettings.getInt("YEAR", 2019);
        switch (monthId) {
            case 1:
                month = "январь";
                break;
            case 2:
                month = "февраль";
                break;
            case 3:
                month = "март";
                break;
            case 4:
                month = "апрель";
                break;
            case 5:
                month = "май";
                break;
            case 6:
                month = "июнь";
                break;
            case 7:
                month = "июль";
                break;
            case 8:
                month = "август";
                break;
            case 9:
                month = "сентябрь";
                break;
            case 10:
                month = "октябрь";
                break;
            case 11:
                month = "ноябрь";
                break;
            case 12:
                month = "декабрь";
                break;
            default:
        }
        return month + " " + yearId;
    }

    public String getMoney(SharedPreferences allSettings, String currency) {
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

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,###,###,###");

        if (currency.equals("руб")) {
            return decimalFormat.format(moneyRubles) + "." + kop + " \u20BD";
        } else return decimalFormat.format(moneyDollars) + "." + cents + " $";
    }

}
