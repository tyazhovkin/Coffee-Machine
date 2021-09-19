package org.hyperskill.app;

import java.util.HashMap;
import java.util.Scanner;

public class CoffeeMachine {
    private int water;
    private int milk;
    private int coffeeBeans;
    private int disposableCups;
    private double money;
    private State state;
    private HashMap<Integer, Recipe> recipes;
    private String input;

    public CoffeeMachine(int water, int milk, int coffeeBeans, int disposableCups, double money) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.disposableCups = disposableCups;
        this.money = money;
        this.recipes = new HashMap<>();
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

    public int getDisposableCups() {
        return disposableCups;
    }

    public void setDisposableCups(int disposableCups) {
        this.disposableCups = disposableCups;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public HashMap<Integer, Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(HashMap<Integer, Recipe> recipes) {
        this.recipes = recipes;
    }

    public void nextInput(String input) {
        this.input = input;

        try {
            state = State.valueOf(this.input.toUpperCase());
        } catch (IllegalArgumentException ex) {
            if (!input.matches("\\d+")) {
                switch (state) {
                    case BUY_CHOICE:
                        if ("back".equals(input)) {
                            ready();
                        } else {
                            buyChoice();
                        }
                        return;
                    case WATER_INPUT:
                    case MILK_INPUT:
                    case BEANS_INPUT:
                    case CUPS_INPUT:
                        showAction();
                        return;
                }
            }
        }
        choiceAction();
    }

    public void start() {
        ready();
    }

    public void stop() {
        state = State.EXIT;
    }

    private void showAction() {
        switch (state) {
            case WATER_INPUT:
                System.out.println("\nWrite how many ml of water you want to add:");
                break;
            case MILK_INPUT:
                System.out.println("Write how many ml of milk you want to add:");
                break;
            case BEANS_INPUT:
                System.out.println("Write how many grams of coffee beans you want to add:");
                break;
            case CUPS_INPUT:
                System.out.println("Write how many disposable cups of coffee you want to add:");
                break;
        }
    }

    private void choiceAction() {
        switch (state) {
            case READY:
                ready();
            case BUY:
            case BUY_CHOICE:
                buyCoffee();
                break;
            case FILL:
            case WATER_INPUT:
            case MILK_INPUT:
            case BEANS_INPUT:
            case CUPS_INPUT:
                fill();
                break;
            case TAKE:
                take();
                break;
            case REMAINING:
                showRemaining();
                break;
            case EXIT:
                stop();
                break;
        }
    }

    private void ready() {
        state = State.READY;
        System.out.println();
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }

    private void buyChoice() {
        state = State.BUY_CHOICE;
        System.out.println();
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
    }

    private void showRemaining() {
        System.out.println();
        System.out.println("The coffee machine has:");
        System.out.printf("%d ml of water\n", water);
        System.out.printf("%d ml of milk\n", milk);
        System.out.printf("%d g of coffee beans\n", coffeeBeans);
        System.out.printf("%d disposable cups\n", disposableCups);
        System.out.printf("$%.0f of money\n", money);
        ready();
    }

    private void buyCoffee() {
        switch (state) {
            case BUY:
                buyChoice();
                break;
            case BUY_CHOICE:
                int choice = Integer.parseInt(input);

                Recipe recipe = recipes.get(choice);
                boolean isChecked = recipe.checkRemainders(this);

                if (isChecked) {
                    water -= recipe.getWater();
                    milk -= recipe.getMilk();
                    coffeeBeans -= recipe.getCoffeeBeans();
                    money += recipe.getPrice();
                    disposableCups--;
                    System.out.println("I have enough resources, making you a coffee!");
                }
                ready();
                break;
        }
    }

    private void fill() {
        switch (state) {
            case FILL:
                state = State.WATER_INPUT;
                showAction();
                break;
            case WATER_INPUT:
                water += Integer.parseInt(input);
                state = State.MILK_INPUT;
                showAction();
                break;
            case MILK_INPUT:
                milk += Integer.parseInt(input);
                state = State.BEANS_INPUT;
                showAction();
                break;
            case BEANS_INPUT:
                coffeeBeans += Integer.parseInt(input);
                state = State.CUPS_INPUT;
                showAction();
                break;
            case CUPS_INPUT:
                disposableCups += Integer.parseInt(input);
                ready();
                break;
        }
    }

    private void take() {
        System.out.printf("I gave you $%.0f\n", money);
        money = 0;
        ready();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);

        coffeeMachine.getRecipes().put(1, new Recipe("espresso", 250, 16, 4));
        coffeeMachine.getRecipes().put(2, new Recipe("latte", 350, 75, 20, 7));
        coffeeMachine.getRecipes().put(3, new Recipe("cappuccino", 200, 100, 12, 6));

        coffeeMachine.start();

        while (coffeeMachine.getState() != State.EXIT) {
            coffeeMachine.nextInput(scanner.next());
        }
    }
}