package org.anastdronina.gyperborea;

import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Person {
    private int id;
    private String name, surname;
    private int age, salary, job;
    private int building, manufacture, farm, athletic, learning, talking, strength, art;
    private ArrayList<String> traits;

    public Person(int id, String name, String surname, int job, int salary, int age, int building, int manufacture, int farm,
                  int athletic, int learning, int talking, int strength, int art, ArrayList<String> traits) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.job = job;
        this.salary = salary;
        this.age = age;
        this.building = building;
        this.manufacture = manufacture;
        this.farm = farm;
        this.athletic = athletic;
        this.learning = learning;
        this.talking = talking;
        this.strength = strength;
        this.art = art;
        this.traits = traits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getManufacture() {
        return manufacture;
    }

    public void setManufacture(int manufacture) {
        this.manufacture = manufacture;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public int getAthletic() {
        return athletic;
    }

    public void setAthletic(int athletic) {
        this.athletic = athletic;
    }

    public int getLearning() {
        return learning;
    }

    public void setLearning(int learning) {
        this.learning = learning;
    }

    public int getTalking() {
        return talking;
    }

    public void setTalking(int talking) {
        this.talking = talking;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public ArrayList<String> getTraits() {
        return traits;
    }

    public void setTraits(ArrayList<String> traits) {
        this.traits = traits;
    }

}
