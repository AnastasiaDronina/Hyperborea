package org.anastdronina.gyperborea;

public class Farm {
    private int id, status, farmerId;
    private String name, crop;

    public Farm(int id, String name, String crop, int status, int farmerId) {
        this.id = id;
        this.status = status;
        this.farmerId = farmerId;
        this.name = name;
        this.crop = crop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String statusString(int statusInt) {
        String statusStr = "";
        switch (statusInt) {
            case 0:
                statusStr = "Не используется";
                break;
            case 1:
                statusStr = "Посев завершен";
                break;
            case 2:
                statusStr = "Взошли ростки";
                break;
            case 3:
                statusStr = "Цветение";
                break;
            case 4:
                statusStr = "Созревание";
                break;
            case 5:
                statusStr = "Сбор урожая";
                break;
        }
        return statusStr;
    }
}
