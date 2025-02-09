package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class App
{
    private static Map<String, String> mapEnRuWords = new HashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final static String MAP_INPUT_FILE_PATH =
            "II_JavaCoreMiddle/2.3_CollectionsHashMap/src/main/resources/hash_map_input.txt";
    private final static String LOGS_FILE_PATH =
            "II_JavaCoreMiddle/2.3_CollectionsHashMap/src/main/resources/logs.txt";

    public static void main( String[] args )
    {
        fillHashMapInput(MAP_INPUT_FILE_PATH);
        jsonFileToHashMap(MAP_INPUT_FILE_PATH);
        mapPractise(LOGS_FILE_PATH);
    }

    private static void mapPractise(String logsFilePath) {
        try(FileWriter writer = new FileWriter(logsFilePath, true)) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            writer.write(currentDateTime.toString() + "\n");
            writer.write("HashMap practise:\na) Read String pairs from file:\n");
            writer.write(mapEnRuWords.toString() + "\n");
            mapEnRuWords.put("Eternity", "Вечность");
            mapEnRuWords.put("Comfort", "Комфорт");
            mapEnRuWords.put("Freedom", "Свобода");
            mapEnRuWords.put("Spring", "Родник");
            mapEnRuWords.put("Comfort", "Уют");
            writer.write("b) HashMap after adding 5 pairs:\n");
            writer.write(mapEnRuWords.toString() + "\n");
            writer.write("c) Simple enumeration of the hashMap by circle \"for\":\n");
            for (String key : mapEnRuWords.keySet()) {
                writer.write(key + " : " + mapEnRuWords.get(key) + "\n");
            }
            mapEnRuWords.put("Comfort", "Спокойствие");
            writer.write("d) HashMap after adding one pair with existing key:\n");
            writer.write(mapEnRuWords.toString() + "\n");
            Set<String> keySet = mapEnRuWords.keySet();
            writer.write("e) Key set from a separate collection:\n");
            writer.write(keySet.toString()  + "\n");

            Set<String> valueSet = new HashSet<String>(mapEnRuWords.values());
            writer.write("f) Count of the unique values in the map : " +
                            valueSet.size() + "\n");
            writer.write("g) Does the map contain \"Freedom\" key? " +
                        mapEnRuWords.containsKey("Freedom") + "\n");
            writer.write("h) Does the map contain \"Вечность\" value? " +
                        mapEnRuWords.containsValue("Вечность") + "\n");
            writer.write("i) Count of the map's pairs : " +
                        mapEnRuWords.size() + "\n");
            writer.write("j) After deleting pair from the map by the key \"Freedom\"\n");
            mapEnRuWords.remove("Freedom");
            writer.write(mapEnRuWords.toString() + "\n");
            writer.write("j) After deleting pair from the map by the value \"Вечность\"\n");
            mapEnRuWords.values().remove("Вечность");
            writer.write(mapEnRuWords.toString() + "\n");

            writer.write("--------------------\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void jsonFileToHashMap(String filePath) {
        File file = new File(filePath);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            TypeReference<HashMap<String, String>> typeRef
                    = new TypeReference<HashMap<String, String>>() {};
            mapEnRuWords = objectMapper.readValue(file, typeRef);
        } catch (Exception e) {
            System.out.println("An error while reading file : " + e.getMessage());
        }
    }

    private static void fillHashMapInput(String filePath) {
        Map<String, String> hashMapInput = new HashMap<>();
        hashMapInput.put("Spring", "Весна");
        hashMapInput.put("Summer", "Лето");
        hashMapInput.put("Autumn", "Осень");
        hashMapInput.put("Winter", "Зима");
        hashMapInput.put("Friendship", "Дружба");
        hashMapInput.put("Smile", "Улыбка");
        hashMapInput.put("Freedom", "Свобода");
        hashMapInput.put("Life", "Жизнь");
        hashMapInput.put("Destiny", "Судьба");
        hashMapInput.put("Melody", "Мелодия");
        String json = null;
        try {
            json = objectMapper.writeValueAsString(hashMapInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            objectMapper.writeValue(new File(filePath), hashMapInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
