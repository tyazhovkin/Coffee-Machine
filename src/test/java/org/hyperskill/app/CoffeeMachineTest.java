package org.hyperskill.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class TestClue {
    int water;
    int milk;
    int beans;
    int cups;
    int money;
    String feedback;

    TestClue(int w, int m, int b, int c, int mo, String feedback) {
        water = w;
        milk = m;
        beans = b;
        cups = c;
        money = mo;
        this.feedback = feedback;
    }
}

public class CoffeeMachineTest {
    private static ByteArrayOutputStream output;
    private static ByteArrayInputStream input;
    private static final PrintStream DEFAULT_STDOUT = System.out;
    private static final InputStream DEFAULT_STDIN = System.in;

    private void provideInput(String data) {
        input = new ByteArrayInputStream(data.getBytes());
        System.setIn(input);
    }

    private String getOutput() {
        return output.toString();
    }

    @BeforeEach
    public void setUpStreams() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void rollbackChangesToStdout() {
        System.setOut(DEFAULT_STDOUT);
    }

    @AfterEach
    public void rollbackChangesToStdin() {
        System.setIn(DEFAULT_STDIN);
    }

    @Test
    public void test1() {
        provideInput("remaining\n" +
                "buy\n" +
                "2\n" +
                "buy\n" +
                "2\n" +
                "fill\n" +
                "1000\n" +
                "0\n" +
                "0\n" +
                "0\n" +
                "buy\n" +
                "2\n" +
                "take\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                700 - 400,
                390 - 540,
                80 - 120,
                7 - 9,
                -550,
                "This test is exactly " +
                        "like in the example - try to run it by yourself"));
    }

    @Test
    public void test2() {
        provideInput("remaining\n" +
                "fill\n" +
                "3000\n" +
                "3000\n" +
                "3000\n" +
                "3000\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                3000,
                3000,
                3000,
                3000,
                0,
                "This test checks \"fill\" action"));
    }

    @Test
    public void test3() {
        provideInput("remaining\n" +
                "buy\n" +
                "1\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                -250,
                0,
                -16,
                -1,
                4, "This test checks \"buy\" " +
                "action with the first variant of coffee"));
    }

    @Test
    public void test4() {
        provideInput("remaining\n" +
                "buy\n" +
                "2\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                -350,
                -75,
                -20,
                -1,
                7, "This test checks \"buy\" " +
                "action with the second variant of coffee"));
    }

    @Test
    public void test5() {
        provideInput("remaining\n" +
                "buy\n" +
                "3\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                -200,
                -100,
                -12,
                -1,
                6, "This test checks \"buy\" " +
                "action with the third variant of coffee"));
    }

    @Test
    public void test6() {
        provideInput("remaining\n" +
                "take\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                0,
                0,
                0,
                0,
                -550, "This test checks \"take\" action"));
    }

    @Test
    public void test7() {
        provideInput("remaining\n" +
                "buy\n" +
                "back\n" +
                "remaining\n" +
                "exit\n");

        CoffeeMachine.main(new String[0]);

        check(getOutput(), new TestClue(
                0,
                0,
                0,
                0,
                0, "This test checks \"back\" " +
                "action right after \"buy\" action"));
    }

    private void check(String reply, TestClue clue) {
        String[] lines = reply.split("\\n");

        if (lines.length <= 1) {
            Assertions.fail("Looks like you didn't print anything!");
            return;
        }

        int water_ = clue.water;
        int milk_ = clue.milk;
        int beans_ = clue.beans;
        int cups_ = clue.cups;
        int money_ = clue.money;

        List<Integer> milk = new ArrayList<>();
        List<Integer> water = new ArrayList<>();
        List<Integer> beans = new ArrayList<>();
        List<Integer> cups = new ArrayList<>();
        List<Integer> money = new ArrayList<>();

        for (String line : lines) {
            line = line.replace("$", "").trim();
            String[] words = line.split("\\s+");
            if (words.length == 0) {
                continue;
            }
            String firstWord = words[0];
            int amount;
            try {
                amount = Integer.parseInt(firstWord);
            } catch (Exception e) {
                continue;
            }
            if (line.contains("milk")) {
                milk.add(amount);
            } else if (line.contains("water")) {
                water.add(amount);
            } else if (line.contains("beans")) {
                beans.add(amount);
            } else if (line.contains("cups")) {
                cups.add(amount);
            } else if (line.contains("money")) {
                money.add(amount);
            }
        }

        if (milk.size() != 2) {
            Assertions.fail("There should be two lines with \"milk\", " +
                    "found: " + milk.size());
            return;
        }

        if (water.size() != 2) {
            Assertions.fail("There should be two lines with \"water\", " +
                    "found: " + water.size());
            return;
        }

        if (beans.size() != 2) {
            Assertions.fail("There should be two lines with \"beans\", " +
                    "found: " + beans.size());
            return;
        }

        if (cups.size() != 2) {
            Assertions.fail("There should be two lines with \"cups\", " +
                    "found: " + cups.size());
            return;
        }

        if (money.size() != 2) {
            Assertions.fail("There should be two lines with \"money\", " +
                    "found: " + money.size());
            return;
        }

        int milk0 = milk.get(0);
        int milk1 = milk.get(milk.size() - 1);

        int water0 = water.get(0);
        int water1 = water.get(water.size() - 1);

        int beans0 = beans.get(0);
        int beans1 = beans.get(beans.size() - 1);

        int cups0 = cups.get(0);
        int cups1 = cups.get(cups.size() - 1);

        int money0 = money.get(0);
        int money1 = money.get(money.size() - 1);

        int diffWater = water1 - water0;
        int diffMilk = milk1 - milk0;
        int diffBeans = beans1 - beans0;
        int diffCups = cups1 - cups0;
        int diffMoney = money1 - money0;

        boolean isCorrect =
                diffWater == water_ &&
                        diffMilk == milk_ &&
                        diffBeans == beans_ &&
                        diffCups == cups_ &&
                        diffMoney == money_;

        Assertions.assertTrue(isCorrect, clue.feedback);
    }
}