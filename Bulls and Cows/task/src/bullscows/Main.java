package bullscows;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numberOfDigits = scanner.nextInt() ; // Диапазон числа, от 0 до n
        scanner.nextLine();

        if (numberOfDigits < 0 || numberOfDigits > 9) {
            System.out.println("Error: can't generate a secret number with a length of 11 because there aren't enough unique digits.");
        } else {
            String numStr = generateRandomNumber(numberOfDigits); // Получаем псевдослучайное число

            // Извлекаем из случайного числа нужный диапазон чисел от 0 до n
            String secretCode = numStr.substring(0, numberOfDigits);
            String[] arrCode = secretCode.split(""); // Преобразуем случайное число в массив

            boolean flag = true;

            int step = 1;
            System.out.println("Okay, let's start a game!");
            System.out.println("Turn " + step + ":");

            while (flag) {
                // Ввод пользователя (угадывание числа)
                String input = scanner.nextLine();
                String[] arrInput = input.split(""); // преобразуем число в массив

                // Считаем количество быков и коров
                int[] nums = counterBullsCows(arrCode, arrInput);
                int bulls = nums[0], cows = nums[1];

                // Определяем победителя
                flag = isGameOver(bulls, cows, numberOfDigits);

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

    public static String generateRandomNumber(int digits) {
        if (digits <= 0 || digits > 10) {
            throw new IllegalArgumentException("Количество цифр должно быть от 1 до 10.");
        }

        // Создаём список цифр от 0 до 9
        List<Integer> digitList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            digitList.add(i);
        }

        // Перемешиваем список случайным образом
        Collections.shuffle(digitList);

        // Собираем первые n цифр из перемешанного списка
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            number.append(digitList.get(i));
        }

        // Если первая цифра 0 (для чисел с несколькими цифрами), переставляем её
        if (number.charAt(0) == '0' && digits > 1) {
            number.setCharAt(0, number.charAt(1));
            number.setCharAt(1, '0');
        }

        return number.toString();
    }
}
