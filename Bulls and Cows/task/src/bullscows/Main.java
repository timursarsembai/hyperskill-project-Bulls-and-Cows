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
        int numberOfSymbols;
        int numberVariantOfSymbols;

        try {
            System.out.println("Input the length of the secret code:");
            String lengthInput = scanner.nextLine();
            numberOfSymbols = parseInput(lengthInput);

            System.out.println("Input the number of possible symbols in the code:");
            String rangeInput = scanner.nextLine();
            numberVariantOfSymbols = parseInput(rangeInput);

            validateInput(numberOfSymbols, numberVariantOfSymbols);
        } catch (GameException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        String secretCode = codeGenerator.generate(numberOfSymbols, numberVariantOfSymbols);
        System.out.println("The secret is prepared: " + "*".repeat(numberOfSymbols) +
                " (" + codeGenerator.getSymbolRange(numberVariantOfSymbols) + ").");
        System.out.println("Okay, let's start a game!");

        int step = 1;
        while (true) {
            System.out.println("Turn " + step + ":");
            String input = scanner.nextLine();

            try {
                validateGuess(input, numberOfSymbols, numberVariantOfSymbols);
                Grade grade = evaluateGuess(secretCode, input);

                if (grade.getBulls() == numberOfSymbols) {
                    System.out.println("Grade: " + grade + "\nCongratulations! You guessed the secret code.");
                    break;
                } else {
                    System.out.println("Grade: " + grade);
                }
                step++;
            } catch (GameException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }
    }

    private int parseInput(String input) throws GameException {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new GameException("\"" + input + "\" isn't a valid number.");
        }
    }

    private void validateInput(int numberOfSymbols, int numberVariantOfSymbols) throws GameException {
        if (numberOfSymbols <= 0) {
            throw new GameException("length must be positive.");
        }
        if (numberOfSymbols > MAX_SYMBOLS) {
            throw new GameException("maximum length of the code is " + MAX_SYMBOLS);
        }
        if (numberVariantOfSymbols > MAX_SYMBOLS) {
            throw new GameException("maximum number of possible symbols in the code is " + MAX_SYMBOLS + " (0-9, a-z).");
        }
        if (numberVariantOfSymbols < numberOfSymbols) {
            throw new GameException("it's not possible to generate a code with a length of " +
                    numberOfSymbols + " with " + numberVariantOfSymbols + " unique symbols.");
        }
    }

    private void validateGuess(String guess, int length, int range) throws GameException {
        if (guess.length() != length) {
            throw new GameException("guess length must be " + length);
        }
        Set<Character> validChars = new HashSet<>();
        for (int i = 0; i < range; i++) {
            if (i < 10) validChars.add((char)('0' + i));
            else validChars.add((char)('a' + (i - 10)));
        }
        for (char c : guess.toCharArray()) {
            if (!validChars.contains(c)) {
                throw new GameException("invalid symbols in guess");
            }
        }
    }

    private Grade evaluateGuess(String secretCode, String guess) {
        int bulls = 0;
        int cows = 0;
        boolean[] secretMatched = new boolean[secretCode.length()];
        boolean[] guessMatched = new boolean[guess.length()];

        for (int i = 0; i < secretCode.length(); i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                bulls++;
                secretMatched[i] = true;
                guessMatched[i] = true;
            }
        }

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

class GameException extends Exception {
    public GameException(String message) {
        super(message);
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
            result.append(bulls).append(" bull");
            if (bulls > 1) result.append("s");
        }
        if (cows > 0) {
            if (result.length() > 0) result.append(" and ");
            result.append(cows).append(" cow");
            if (cows > 1) result.append("s");
        }
        return result.length() > 0 ? result.toString() : "None";
    }
}