package org.hyperskill.app;

public class Recipe {
    private String name;
    private int water;
    private int milk;
    private int coffeeBeans;
    private double price;

    public Recipe(String name, int water, int milk, int coffeeBeans, double price) {
        this.name = name;
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.price = price;
    }

    public Recipe(String name, int water, int coffeeBeans, double price) {
        this.name = name;
        this.water = water;
        this.coffeeBeans = coffeeBeans;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getMilk() {
        return milk;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    public int getCoffeeBeans() {
        return coffeeBeans;
    }

    public void setCoffeeBeans(int coffeeBeans) {
        this.coffeeBeans = coffeeBeans;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean checkRemainders(CoffeeMachine coffeeMachine) {

        if (this.water != 0) {
            if (coffeeMachine.getWater() < this.water) {
                System.out.println("Sorry, not enough water!");
                return false;
            }
        }

        if (this.milk != 0) {
            if (coffeeMachine.getMilk() < this.milk) {
                System.out.println("Sorry, not enough milk!");
                return false;
            }
        }

        if (this.coffeeBeans != 0) {
            if (coffeeMachine.getCoffeeBeans() < this.coffeeBeans) {
                System.out.println("Sorry, not enough coffee beans!");
                return false;
            }
        }
        return true;
    }
}
