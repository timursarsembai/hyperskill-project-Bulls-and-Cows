package bullscows;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Создаем объект Game и запускаем игру
        Game game = new Game();
        game.start();
    }
}

/**
 * Класс Game отвечает за игровой процесс.
 * Он управляет вводом пользователя, проверкой данных, подсчетом результатов (быков и коров)
 * и определяет победителя.
 */
class Game {
    private final Scanner scanner = new Scanner(System.in); // Сканер для чтения ввода пользователя
    private final CodeGenerator codeGenerator = new CodeGenerator(); // Генератор секретного кода
    private final int MAX_SYMBOLS = 36; // Максимальное количество символов для генерации кода

    public void start() {
        // Просим пользователя ввести длину секретного кода
        System.out.println("Input the length of the secret code:");
        int numberOfSymbols = scanner.nextInt();

        // Просим пользователя ввести количество возможных символов
        System.out.println("Input the number of possible symbols in the code:");
        int numberVariantOfSymbols = scanner.nextInt();

        // Проверяем корректность ввода пользователя
        validateInput(numberOfSymbols, numberVariantOfSymbols);

        // Генерируем секретный код
        String secretCode = codeGenerator.generate(numberOfSymbols, numberVariantOfSymbols);

        // Подготавливаем сообщение о диапазоне символов и отображаем его
        System.out.println("The secret is prepared: " + "*".repeat(numberOfSymbols) +
                " (" + codeGenerator.getSymbolRange(numberVariantOfSymbols) + ").");

        System.out.println("Okay, let's start a game!"); // Начинаем игру

        boolean flag = true; // Флаг для отслеживания состояния игры
        int step = 1; // Номер текущего хода
        while (flag) {
            // Отображаем номер хода
            System.out.println("Turn " + step + ":");

            // Читаем ввод пользователя (его попытку угадать код)
            String input = scanner.next();

            // Оцениваем попытку пользователя (подсчитываем быков и коров)
            Grade grade = evaluateGuess(secretCode, input);

            // Если количество быков равно длине секретного кода, пользователь выиграл
            if (grade.getBulls() == numberOfSymbols) {
                System.out.println("Grade: " + grade + ". Congratulations! You've guessed the secret code.");
                flag = false; // Завершаем игру
            } else {
                // Если код не угадан, отображаем текущий результат (быки и коровы)
                System.out.println("Grade: " + grade);
            }
            step++; // Переходим к следующему ходу
        }
    }

    // Метод проверки корректности пользовательского ввода
    private void validateInput(int numberOfSymbols, int numberVariantOfSymbols) {
        if (numberOfSymbols <= 0 || numberOfSymbols > MAX_SYMBOLS) {
            throw new IllegalArgumentException("The length of the code must be between 1 and " + MAX_SYMBOLS);
        }
        if (numberVariantOfSymbols < numberOfSymbols || numberVariantOfSymbols > MAX_SYMBOLS) {
            throw new IllegalArgumentException("The number of possible symbols must be at least the length of the code and not exceed " + MAX_SYMBOLS);
        }
    }

    // Метод для подсчета быков и коров в попытке пользователя
    private Grade evaluateGuess(String secretCode, String guess) {
        int bulls = 0; // Количество быков
        int cows = 0; // Количество коров

        // Булевы массивы для отслеживания совпадений в коде и вводе пользователя
        boolean[] secretMatched = new boolean[secretCode.length()];
        boolean[] guessMatched = new boolean[guess.length()];

        // Подсчет быков (полные совпадения)
        for (int i = 0; i < secretCode.length(); i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                bulls++;
                secretMatched[i] = true;
                guessMatched[i] = true;
            }
        }

        // Подсчет коров (совпадения без учета позиции)
        for (int i = 0; i < secretCode.length(); i++) {
            if (secretMatched[i]) continue; // Пропускаем уже совпавшие символы
            for (int j = 0; j < guess.length(); j++) {
                if (!guessMatched[j] && secretCode.charAt(i) == guess.charAt(j)) {
                    cows++;
                    guessMatched[j] = true; // Помечаем совпадение
                    break;
                }
            }
        }

        // Возвращаем результат в виде объекта Grade
        return new Grade(bulls, cows);
    }
}

/**
 * Класс CodeGenerator отвечает за генерацию секретного кода и определение диапазона символов.
 * Он использует предопределенный массив символов (цифры и буквы).
 */
class CodeGenerator {
    // Все доступные символы для генерации секретного кода
    private final String[] SYMBOLS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z"
    };

    // Метод для генерации секретного кода
    public String generate(int length, int range) {
        // Берем подсписок символов в зависимости от указанного диапазона
        List<String> symbols = Arrays.asList(SYMBOLS).subList(0, range);
        Collections.shuffle(symbols); // Перемешиваем символы
        return String.join("", symbols.subList(0, length)); // Собираем строку из первых length символов
    }

    // Метод для определения диапазона символов в виде строки
    public String getSymbolRange(int range) {
        if (range <= 10) {
            return "0-" + (range - 1); // Если только цифры, отображаем их диапазон
        }
        // Если есть буквы, вычисляем последнюю букву
        char lastLetter = (char) ('a' + (range - 11));
        return "0-9, a-" + lastLetter;
    }
}

/**
 * Класс Grade используется для хранения и отображения результата попытки пользователя.
 * Хранит количество быков и коров, а также форматирует результат в читаемом виде.
 */
class Grade {
    private final int bulls; // Количество быков
    private final int cows; // Количество коров

    public Grade(int bulls, int cows) {
        this.bulls = bulls;
        this.cows = cows;
    }

    // Геттер для количества быков
    public int getBulls() {
        return bulls;
    }

    // Геттер для количества коров
    public int getCows() {
        return cows;
    }

    // Форматирование вывода результата (быки и коровы)
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
        return result.length() > 0 ? result.toString() : "None"; // Если ни быков, ни коров, выводим "None"
    }
}
