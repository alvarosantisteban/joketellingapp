package com.alvarosantisteban;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A small class that provides random jokes.
 */
public class JokeProvider {

    private static final String[] jokesArray = new String[]{"Can a kangaroo jump higher than a house? \n" +
            "-\n" + "Of course, a house doesn’t jump at all.",
            "Doctor: \"I'm sorry but you suffer from a terminal illness and have only 10 to live.\"\n" +
                    "\n" + "Patient: \"What do you mean, 10? 10 what? Months? Weeks?!\"\n" +
                    "\n" + "Doctor: \"Nine.",
            "Anton, do you think I’m a bad father?\n" +
                    "\n" +"My name is Paul.",
            "My dog used to chase people on a bike a lot. It got so bad, finally I had to take his bike away.",
            "What is the best season to jump on a trampoline? \n" +
                    "-\n" + "Spring time.",
            "Teacher to Paul: “Wake up, Paul! You can’t sleep in class!” \n" +
                    "-\n" + "Paul to teacher: “I could actually, it’s just that you’re a bit loud.",
            "What is orange and sounds like a parrot?\n" +
                    "-\n" + "A carrot.",
            "What should you put on the tomb stone of a mathematician?\n" +
                    "-\n" + "He didn't count with this..."
    };

    public static String getJoke() {
        // Get a random joke from the array
        int randomNum = ThreadLocalRandom.current().nextInt(0, jokesArray.length);
        return jokesArray[randomNum];
    }
}
