import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        StringBuilder strB = new StringBuilder();

        // Убираем пробелы и приводим строку к нижнему регистру
        String normalizedInput = input.replaceAll("\\s+", "").toLowerCase();

        // Создаем StringBuilder для строки без пробелов
        strB.append(normalizedInput);

        // Проверяем, является ли строка палиндромом
        boolean isPalindrome = normalizedInput.equals(strB.reverse().toString());

        // Печатаем результат
        System.out.println(isPalindrome ? "Yes" : "No");
    }
}
