package bullscows;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

class Game {
    private final Scanner scanner = new Scanner(System.in);
    private final CodeGenerator codeGenerator = new CodeGenerator();
    private final int MAX_SYMBOLS = 36;

    public void start() {
        System.out.println("Input the length of the secret code:");
        int numberOfSymbols = scanner.nextInt();

        System.out.println("Input the number of possible symbols in the code:");
        int numberVariantOfSymbols = scanner.nextInt();

        validateInput(numberOfSymbols, numberVariantOfSymbols);

        String secretCode = codeGenerator.generate(numberOfSymbols, numberVariantOfSymbols);

        System.out.println("The secret is prepared: " + "*".repeat(numberOfSymbols) +
                " (" + codeGenerator.getSymbolRange(numberVariantOfSymbols) + ").");

        System.out.println("Okay, let's start a game!");

        boolean flag = true;
        int step = 1;
        while (flag) {
            System.out.println("Turn " + step + ":");
            String input = scanner.next();
            Grade grade = evaluateGuess(secretCode, input);

            if (grade.getBulls() == numberOfSymbols) {
                System.out.println("Grade: " + grade + ". Congratulations! You've guessed the secret code.");
                flag = false;
            } else {
                System.out.println("Grade: " + grade);
            }
            step++;
        }
    }

    private void validateInput(int numberOfSymbols, int numberVariantOfSymbols) {
        if (numberOfSymbols <= 0 || numberOfSymbols > MAX_SYMBOLS) {
            throw new IllegalArgumentException("The length of the code must be between 1 and " + MAX_SYMBOLS);
        }
        if (numberVariantOfSymbols < numberOfSymbols || numberVariantOfSymbols > MAX_SYMBOLS) {
            throw new IllegalArgumentException("The number of possible symbols must be at least the length of the code and not exceed " + MAX_SYMBOLS);
        }
    }

    private Grade evaluateGuess(String secretCode, String guess) {
        int bulls = 0;
        int cows = 0;
        boolean[] secretMatched = new boolean[secretCode.length()];
        boolean[] guessMatched = new boolean[guess.length()];

        // Count bulls
        for (int i = 0; i < secretCode.length(); i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                bulls++;
                secretMatched[i] = true;
                guessMatched[i] = true;
            }
        }

        // Count cows
        for (int i = 0; i < secretCode.length(); i++) {
            if (secretMatched[i]) continue;
            for (int j = 0; j < guess.length(); j++) {
                if (!guessMatched[j] && secretCode.charAt(i) == guess.charAt(j)) {
                    cows++;
                    guessMatched[j] = true;
                    break;
                }
            }
        }

        return new Grade(bulls, cows);
    }
}

class CodeGenerator {
    private final String[] SYMBOLS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z"
    };

    public String generate(int length, int range) {
        List<String> symbols = Arrays.asList(SYMBOLS).subList(0, range);
        Collections.shuffle(symbols);
        return String.join("", symbols.subList(0, length));
    }

    public String getSymbolRange(int range) {
        if (range <= 10) {
            return "0-" + (range - 1);
        }
        char lastLetter = (char) ('a' + (range - 11));
        return "0-9, a-" + lastLetter;
    }
}

class Grade {
    private final int bulls;
    private final int cows;

    public Grade(int bulls, int cows) {
        this.bulls = bulls;
        this.cows = cows;
    }

    public int getBulls() {
        return bulls;
    }

    public int getCows() {
        return cows;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (bulls > 0) {
            result.append(bulls).append(" bull(s)");
        }
        if (cows > 0) {
            if (result.length() > 0) result.append(" and ");
            result.append(cows).append(" cow(s)");
        }
        return result.length() > 0 ? result.toString() : "None";
    }
}
