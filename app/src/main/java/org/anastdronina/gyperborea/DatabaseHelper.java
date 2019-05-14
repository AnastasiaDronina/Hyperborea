package org.anastdronina.gyperborea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "hyperborea.db";
    public static final String TABLE_PEOPLE = "population";
    public static final String TABLE_TECNOLOGIES = "tecnologies";
    public static final String TABLE_STOCK = "stock";
    public static final String TABLE_FARMS = "farms";
    public static final String TABLE_MARKET = "market";

    //for Population table
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_4 = "JOB";
    public static final String COL_5 = "SALARY";
    public static final String COL_6 = "AGE";
    public static final String COL_7 = "BUILDING";
    public static final String COL_8 = "MANUFACTURE";
    public static final String COL_9 = "FARM";
    public static final String COL_10 = "ATHLETIC";
    public static final String COL_11 = "LEARNING";
    public static final String COL_12 = "TALKING";
    public static final String COL_13 = "STRENGTH";
    public static final String COL_14 = "ART";
    public static final String COL_15 = "TRAIT1";
    public static final String COL_16 = "TRAIT2";
    public static final String COL_17 = "TRAIT3";
    public static final String COL_18 = "FIN_MONTHS_WORKED";
    public static final String COL_19 = "FIN_COEF";

    //for Tecnologies table
    public static final String COL_01 = "TEC_ID";
    public static final String COL_02 = "TEC_NAME";
    public static final String COL_03 = "TEC_DESCRIPTION";
    public static final String COL_04 = "TEC_MONTHS_TO_LEARN";
    public static final String COL_05 = "TEC_PRICE";
    public static final String COL_06 = "TEC_IS_LEARNED";

    //for Stock table
    public static final String COL_001 = "PRODUCT_ID";
    public static final String COL_002 = "PRODUCT_NAME";
    public static final String COL_003 = "PRODUCT_TYPE";
    public static final String COL_004 = "PRODUCT_AMOUNT";

    //for Farms table
    public static final String COL_0001 = "FARM_ID";
    public static final String COL_0002 = "FARM_NAME";
    public static final String COL_0003 = "FARM_CROP";
    public static final String COL_0004 = "FARM_STATUS";
    public static final String COL_0005 = "FARM_FARMER_ID";

    //for Market table
    public static final String COL_00001 = "ITEM_ID";
    public static final String COL_00002 = "ITEM_NAME";
    public static final String COL_00003 = "ITEM_AMOUNT";
    public static final String COL_00004 = "ITEM_PRICE";
    public static final String COL_00005 = "ITEM_CURRENCY";
    public static final String COL_00006 = "ITEM_TYPE";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PEOPLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, " +
                "JOB INTEGER, SALARY INTEGER, AGE INTEGER, BUILDING INTEGER, MANUFACTURE INTEGER, FARM INTEGER, " +
                "ATHLETIC INTEGER, LEARNING INTEGER, TALKING INTEGER, STRENGTH INTEGER, ART INTEGER, TRAIT1 TEXT, " +
                "TRAIT2 TEXT, TRAIT3 TEXT, FIN_MONTHS_WORKED INTEGER, FIN_COEF REAL)");
        db.execSQL("create table " + TABLE_TECNOLOGIES + " (TEC_ID INTEGER PRIMARY KEY AUTOINCREMENT, TEC_NAME TEXT, TEC_DESCRIPTION TEXT, " +
                "TEC_MONTHS_TO_LEARN INTEGER, TEC_PRICE INTEGER, TEC_IS_LEARNED INTEGER)");
        db.execSQL("create table " + TABLE_STOCK + " (PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, PRODUCT_NAME TEXT, " +
                "PRODUCT_TYPE TEXT, PRODUCT_AMOUNT INTEGER)");
        db.execSQL("create table " + TABLE_FARMS + " (FARM_ID INTEGER PRIMARY KEY AUTOINCREMENT, FARM_NAME TEXT, " +
                "FARM_CROP TEXT, FARM_STATUS INTEGER, FARM_FARMER_ID INTEGER)");
        db.execSQL("create table " + TABLE_MARKET + " (ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, " +
                "ITEM_AMOUNT INTEGER, ITEM_PRICE INTEGER, ITEM_CURRENCY TEXT, ITEM_TYPE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECNOLOGIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKET);
        onCreate(db);
    }

    public boolean insertPeopleData(String name, String surname, int job, int salary, int age, int building, int manufacture, int farm, int athletic,
                                    int learning, int talking, int strength, int art, String trait1, String trait2, String trait3, int finMonthsWorked, double finCoef) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, job);
        contentValues.put(COL_5, salary);
        contentValues.put(COL_6, age);
        contentValues.put(COL_7, building);
        contentValues.put(COL_8, manufacture);
        contentValues.put(COL_9, farm);
        contentValues.put(COL_10, athletic);
        contentValues.put(COL_11, learning);
        contentValues.put(COL_12, talking);
        contentValues.put(COL_13, strength);
        contentValues.put(COL_14, art);
        contentValues.put(COL_15, trait1);
        contentValues.put(COL_16, trait2);
        contentValues.put(COL_17, trait3);
        contentValues.put(COL_18, finMonthsWorked);
        contentValues.put(COL_19, finCoef);

        long result = db.insert(TABLE_PEOPLE, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }

    public boolean insertTecnologiesData(String tecName, String tecDescription, int tecMonthsToLearn, int tecPrice, int tecIsLearned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_02, tecName);
        contentValues.put(COL_03, tecDescription);
        contentValues.put(COL_04, tecMonthsToLearn);
        contentValues.put(COL_05, tecPrice);
        contentValues.put(COL_06, tecIsLearned);

        long result = db.insert(TABLE_TECNOLOGIES, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }

    public boolean insertStockData(String productName, String productType, int productAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_002, productName);
        contentValues.put(COL_003, productType);
        contentValues.put(COL_004, productAmount);

        long result = db.insert(TABLE_STOCK, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }

    public boolean insertFarmsData(String farmName, String farmCrop, int farmStatus, int farmFarmerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_0002, farmName);
        contentValues.put(COL_0003, farmCrop);
        contentValues.put(COL_0004, farmStatus);
        contentValues.put(COL_0005, farmFarmerId);

        long result = db.insert(TABLE_FARMS, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }

    public boolean insertMarketData(String itemName, int itemAmount, int itemPrice, String itemCurrency, String itemType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_00002, itemName);
        contentValues.put(COL_00003, itemAmount);
        contentValues.put(COL_00004, itemPrice);
        contentValues.put(COL_00005, itemCurrency);
        contentValues.put(COL_00006, itemType);

        long result = db.insert(TABLE_MARKET, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }
}
