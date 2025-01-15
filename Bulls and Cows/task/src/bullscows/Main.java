package bullscows;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] array = {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        };

        System.out.println("Input the length of the secret code:");
        int numberOfSymbols = scanner.nextInt() ;

        System.out.println("Input the number of possible symbols in the code:");
        int numberVariantOfSymbols = scanner.nextInt();

        while(true) {
            if (numberVariantOfSymbols < numberOfSymbols) {
                System.out.println("The number of possible symbols cannot fall short of the length of the secret code. Try again.");
                numberVariantOfSymbols = scanner.nextInt();
            } else {
                scanner.nextLine();
                break;
            }
        }

        if (numberOfSymbols < 0 || numberOfSymbols > 9) {
            System.out.println("Error: can't generate a secret number with a length of 11 because there aren't enough unique digits.");
        } else {
            String secretCode = generateRandomNumber(numberOfSymbols, numberVariantOfSymbols, array); // Получаем псевдослучайные символы

            // Извлекаем из случайного числа нужный диапазон чисел от 0 до n
            String[] arrCode = secretCode.split(""); // Преобразуем случайное число в массив

            String stars = "";
            for (int i = 1; i <= numberOfSymbols; i++) stars += "*";
            char lastLetter = 'a';
            for (int i = 1; i < numberVariantOfSymbols - 10; i++) {
                lastLetter++;
            }
            System.out.println("The secret is prepared: " + stars + " (0-9, a-" + lastLetter + ").");

            System.out.println("Okay, let's start a game!");

            int step = 1;
            System.out.println("Turn " + step + ":");

            boolean flag = true;

            while (flag) {
                // Ввод пользователя (угадывание числа)
                String input = scanner.nextLine();
                String[] arrInput = input.split(""); // преобразуем число в массив

                // Считаем количество быков и коров
                int[] nums = counterBullsCows(arrCode, arrInput);
                int bulls = nums[0], cows = nums[1];

                // Определяем победителя
                flag = isGameOver(bulls, cows, numberOfSymbols);

                step++;
                System.out.println("Turn " + step + ":");
            }
        }
    }

    static boolean isGameOver(int bulls, int cows, int numberOfDigits) {
        if (bulls == numberOfDigits) {
            System.out.println("Grade: " + bulls + " bull(s). Congratulations! You've guessed the secret code.");
            return false;
        }

        StringBuilder grade = new StringBuilder();
        if (bulls > 0) {
            grade.append(bulls).append(" bull(s)");
        }
        if (cows > 0) {
            if (grade.length() > 0) grade.append(" and ");
            grade.append(cows).append(" cow(s)");
        }

        System.out.println("Grade: " + (grade.length() > 0 ? grade : "None") + ".");
        return true;
    }

    static int[] counterBullsCows(String[] arrCode, String[] arrInput) {
        int bulls = 0, cows = 0;
        boolean[] matchedInput = new boolean[arrInput.length];
        boolean[] matchedCode = new boolean[arrCode.length];

        for (int i = 0; i < arrCode.length; i++) {
            if (arrCode[i].equals(arrInput[i])) {
                bulls++;
                matchedCode[i] = true;
                matchedInput[i] = true;
            }
        }

        for (int i = 0; i < arrCode.length; i++) {
            for (int j = 0; j < arrInput.length; j++) {
                if (!matchedCode[i] && !matchedInput[j] && arrCode[i].equals(arrInput[j])) {
                    cows++;
                    matchedCode[i] = true;
                    matchedInput[j] = true;
                    break;
                }
            }
        }
        return new int[] {bulls, cows};
    }

    public static String generateRandomNumber(int digits, int indexes, String[] array) {
        if (digits <= 0 || digits > 36) {
            throw new IllegalArgumentException("Количество символов должно быть от 0 до 36 включительно.");
        }
        List<String> symbolList = Arrays.asList(array).subList(0, indexes);
        Random random = new Random();

        Collections.shuffle(symbolList, random);

        StringBuilder number = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            number.append(symbolList.get(i));
        }

        return number.toString();
    }

}
