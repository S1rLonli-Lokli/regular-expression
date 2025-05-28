import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Path inputFile = Paths.get("src/input.txt");
        Path outputFile = Paths.get("src/output.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputFile);
             BufferedWriter writer = Files.newBufferedWriter(outputFile)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                String name = parts.length > 0 ? cleanName(parts[0]) : "";
                String age = parts.length > 1 ? cleanAge(parts[1]) : "";
                String phone = parts.length > 2 ? cleanPhone(parts[2]) : "";
                String email = parts.length > 3 ? cleanEmail(parts[3]) : "";

                writer.write(String.join("|", name, age, phone, email));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Обработка завершена. Результат в output.txt");
    }

    // 1. Имя и фамилия
    private static String cleanName(String input) {
        input = input.replaceAll("[^А-Яа-яЁё]", ""); // Удаляем всё, кроме кириллицы
        input = input.replaceAll("(?<=[а-яё])(?=[А-ЯЁ])", " "); // Добавляем пробел перед второй заглавной буквой
        String nameRegex = "^[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$";
        return input.matches(nameRegex) ? input : "";
    }

    // 2. Возраст
    private static String cleanAge(String input) {
        String digitsOnly = input.replaceAll("[^0-9]", "");
        if (digitsOnly.matches("^\\d{1,3}$")) {
            int age = Integer.parseInt(digitsOnly);
            return (age >= 0 && age <= 120) ? String.valueOf(age) : "";
        }
        return "";
    }

    // 3. Телефон
    private static String cleanPhone(String input) {
        String digits = input.replaceAll("[^0-9]", "");
        if (digits.length() == 11 && digits.matches("^8\\d{10}$")) {
            digits = "7" + digits.substring(1);
        }
        if (digits.matches("^7\\d{10}$")) {
            return String.format("+7(%s)%s-%s-%s",
                    digits.substring(1, 4),
                    digits.substring(4, 7),
                    digits.substring(7, 9),
                    digits.substring(9));
        }
        return "";
    }

    // 4. Электронная почта
    private static String cleanEmail(String input) {
        input = input.trim().toLowerCase()
                .replaceAll("\\s+", "")
                .replaceAll("@{2,}", "@")
                .replaceAll("\\.{2,}", ".");

        // Корректный шаблон email-а
        Pattern emailPattern = Pattern.compile("^[a-z0-9_.+-]+@[a-z0-9-]+\\.[a-z]{2,6}$");
        Matcher matcher = emailPattern.matcher(input);
        return matcher.matches() ? input : "";
    }
}
