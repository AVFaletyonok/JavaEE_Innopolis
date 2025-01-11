package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class App
{
    private static List<Integer> integersList = new ArrayList<>();
    private static Set<String> stringSet = new HashSet<>();
    private final static String LIST_INPUT_FILE_PATH =
            "II_JavaCoreMiddle/2.2_CollectionsListSet/src/main/resources/integer_list_input.txt";
    private final static String HASHSET_INPUT_FILE_PATH =
            "II_JavaCoreMiddle/2.2_CollectionsListSet/src/main/resources/string_set_input.txt";
    private final static String LOGS_FILE_PATH =
            "II_JavaCoreMiddle/2.2_CollectionsListSet/src/main/resources/logs.txt";

    public static void main( String[] args )
    {
        FileToIntegersList(LIST_INPUT_FILE_PATH);
        ListPractise(LOGS_FILE_PATH);
        FileToStringHashSet(HASHSET_INPUT_FILE_PATH);
        SetPractise(LOGS_FILE_PATH);
    }

    private static void ListPractise(String logsFilePath) {
        try(FileWriter writer = new FileWriter(logsFilePath, true)) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            writer.write(currentDateTime.toString() + "\n");
            writer.write("List practise:\nRead Integer List from file:\n");
            writer.write(integersList.toString() + "\n");
            writer.write("Sorted List:\n");
            Collections.sort(integersList);
            writer.write(integersList.toString() + "\n");
            writer.write("Sorted List in reverse order:\n");
            Collections.sort(integersList, Collections.reverseOrder());
            writer.write(integersList.toString() + "\n");
            writer.write("Shuffled List:\n");
            Collections.shuffle(integersList, new Random(System.currentTimeMillis()));
            writer.write(integersList.toString() + "\n");
            writer.write("Rotated List by 1 element:\n");
            Collections.rotate(integersList, 1);
            writer.write(integersList.toString() + "\n");

            Set<Integer> tempSet = new HashSet<>();
            Set<Integer> duplicatesSet = new HashSet<>();
            for(Integer number : integersList) {
                if (!tempSet.add(number)) {
                    duplicatesSet.add(number);
                }
            }
            writer.write("Unique elements of the List:\n");
            List<Integer> uniqueList = new ArrayList<>(integersList);
            uniqueList.removeAll(duplicatesSet);
            writer.write(uniqueList.toString() + "\n");
            writer.write("Duplicated elements of the List:\n");
            integersList.removeAll(uniqueList);
            writer.write(integersList.toString() + "\n");

            writer.write("Array from the List:\n");
            Integer[] integersArray = integersList.toArray(new Integer[0]);
            writer.write(Arrays.toString(integersArray) + "\n");

            writer.write("--------------------\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void SetPractise(String logsFilePath) {
        try(FileWriter writer = new FileWriter(logsFilePath, true)) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            writer.write(currentDateTime.toString() + "\n");
            writer.write("Set practise:\nRead String set from file:\n");
            writer.write(stringSet.toString() + "\n");

            String[] newWords = {"Оазис", "Организатор", "Хоккеист", "Скепсис", "Асимметрия"};
            stringSet.addAll(Arrays.stream(newWords).toList());
            writer.write("String set after adding 5 new words:\n");
            for (String word : stringSet) {
                writer.write(word + ", ");
            }

            if (!stringSet.isEmpty()) {
                stringSet.add(stringSet.stream().findFirst().get());
                writer.write("\nString set after adding the first element \"" +
                                stringSet.stream().findFirst().get() + "\":\n");
                writer.write(stringSet.toString() + "\n");
            }
            writer.write("Does String set have the element \"Море\"? ");
            writer.write(stringSet.contains("Море") + "\n");

            if (!stringSet.isEmpty()) {
                writer.write("String set after removing the first element \"" +
                        stringSet.stream().findFirst().get() + "\":\n");
                stringSet.remove(stringSet.stream().findFirst().get());
                writer.write(stringSet.toString() + "\n");
            }
            writer.write("String set has " + stringSet.size() + " elements.\n");
            stringSet.clear();
            writer.write("Is String set empty after removing all elements? " +
                    stringSet.isEmpty() + "\n");

            writer.write("--------------------\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void FileToIntegersList(String filePath) {
        File file = new File(filePath);
        boolean isExistFile = false;
        if (!filePath.isBlank()) isExistFile = file.exists();
        if (!isExistFile) {
            System.out.println("The input file doesn't exist : " + filePath);
            System.exit(1);
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line = reader.readLine();
            while (line != null) {
                integersList.addAll(Arrays.stream(line.split(","))
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .toList());
                line = reader.readLine();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void FileToStringHashSet(String filePath) {
        File file = new File(filePath);
        boolean isExistFile = false;
        if (!filePath.isBlank()) isExistFile = file.exists();
        if (!isExistFile) {
            System.out.println("The input file doesn't exist : " + filePath);
            System.exit(1);
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line = reader.readLine();
            while (line != null) {
                stringSet.addAll(Arrays.stream(line.split("[ .,!-:;]")).toList());
                line = reader.readLine();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
