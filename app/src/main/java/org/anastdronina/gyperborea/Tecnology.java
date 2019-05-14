package org.anastdronina.gyperborea;

public class Tecnology {
    private int id, monthsToLearn;
    long price;
    private String name, description;
    private boolean isLearned;

    public Tecnology(int id, String name, String description, int monthsToLearn, long price, boolean isLearned) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.monthsToLearn = monthsToLearn;
        this.price = price;
        this.isLearned = isLearned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonthsToLearn() {
        return monthsToLearn;
    }

    public void setMonthsToLearn(int monthsToLearn) {
        this.monthsToLearn = monthsToLearn;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }
}
