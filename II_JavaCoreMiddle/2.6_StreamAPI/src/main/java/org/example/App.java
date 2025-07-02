package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App
{
    private static Scanner scanner = new Scanner(System.in);
    private static final Pattern WORD_PATTERN =
            Pattern.compile("[a-zA-Z0-9]+");

    public static void main( String[] args )
    {
        System.out.println("Введите номер задачи:");
        System.out.println("1. Сформировать код Грея;");
        System.out.println("2. Посчитать наиболее встречающиеся слова.");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice == 1) {
            runGrayCode();
        } else if (choice == 2) {
            try {
                runWordFrequency();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Такого варианта нет)");
        }
    }

    private static void runGrayCode() {
        System.out.print("Введите битность кода (1-16): ");
        int n = scanner.nextInt();

        Stream<Integer> grayCodes = cycleGrayCode(n);
        grayCodes.forEach(code ->
                System.out.println(code + " (binary: " + Integer.toBinaryString(code) + ")"));
    }

    private static Stream<Integer> cycleGrayCode(int n) {
        if (n < 1 || n > 16) {
            System.out.println("Битность должна быть в пределах 1 и 16");
            return null;
        }
        int size = 1 << n; // 2^n
        return IntStream.iterate(0, i -> (i + 1) % size)
                .mapToObj(i -> i ^ (i >> 1));
    }

    private static void runWordFrequency() throws IOException {
        System.out.print("Введите путь с названием входного файла : ");
        String inputFile = scanner.nextLine().trim();
        Path inputFilePath = Paths.get(inputFile);
        if (inputFile.isEmpty() || !Files.exists(inputFilePath)) {
            System.out.print("Файл не существует: " + inputFile);
            return;
        };
        System.out.print("Введите путь с названием выходного файла : ");
        String outputFile = scanner.nextLine().trim();
        if (outputFile.isEmpty()) {
            return;
        };
        System.out.print("Введите желаемое количество наиболее встречающихся слов: ");
        int topN = scanner.nextInt();
        wordFrequencyAnalyze(inputFile, outputFile, topN);
    }

    private static void wordFrequencyAnalyze(String inputPath, String outputPath, int topN) throws IOException {
        String content = Files.readString(Paths.get(inputPath));

        Map<String, Long> wordCounts = Arrays.stream((content.toLowerCase() + " ").split("[^\\p{L}\\p{N}]+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String result = wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(" "));

        Files.writeString(Path.of(outputPath), result);

        System.out.println("Результат : " + result);
    }
}
