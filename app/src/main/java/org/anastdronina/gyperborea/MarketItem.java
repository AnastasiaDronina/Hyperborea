package org.anastdronina.gyperborea;

public class MarketItem {

    private int id, amount, price;
    String name, currency, type;

    public MarketItem(int id, String name, int amount, int price, String currency, String type) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.currency = currency;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
