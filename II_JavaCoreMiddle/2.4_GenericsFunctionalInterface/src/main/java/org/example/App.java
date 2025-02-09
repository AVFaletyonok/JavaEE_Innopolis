package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class App
{
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final static String INPUT_FILE_PATH =
            "II_JavaCoreMiddle/2.4_GenericsFunctionalInterface/src/main/resources/functional_interface_input.txt";
    private final static String LOGS_FILE_PATH =
            "II_JavaCoreMiddle/2.4_GenericsFunctionalInterface/src/main/resources/logs.txt";

    public static void main( String[] args )
    {
//        fillStringListInput(INPUT_FILE_PATH);
        List<String> stringList = jsonFileToStringList(INPUT_FILE_PATH);

        Predicate<Object> condition = Objects::isNull;
        Function<Object, Integer> ifTrue = obj -> 0;
        Function<CharSequence, Integer> ifFalse = CharSequence::length;
        Function<String, Integer> safeStringLength = ternaryOperator(condition, ifTrue, ifFalse);

        try(FileWriter writer = new FileWriter(LOGS_FILE_PATH, true)) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            writer.write(currentDateTime.toString() + "\n");

            writer.write("Length of the null String : " + safeStringLength.apply(null) + "\n");
            if(!Objects.isNull(stringList) && !stringList.isEmpty()) {
                stringList.stream()
                        .forEach(s -> {
                            try {
                                writer.write("Length of the \"" + s + "\" string : " + safeStringLength.apply(s) + "\n");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }

            writer.write("--------------------\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static <String, Integer> Function<String, Integer> ternaryOperator(Predicate<Object> predicate,
                                                                      Function<Object, Integer> ifTrue,
                                                                      Function<CharSequence, Integer> ifFalse) {
        return someString -> predicate.test(someString) ? ifTrue.apply(someString) : ifFalse.apply((CharSequence) someString);
    }

    private static List<String> jsonFileToStringList(String filePath) {
        File file = new File(filePath);
        boolean isExistFile = false;
        if (!filePath.isBlank()) isExistFile = file.exists();
        if (!isExistFile) {
            System.out.println("The input file doesn't exist : " + filePath);
            System.exit(1);
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            TypeReference<ArrayList<String>> typeRef
                    = new TypeReference<ArrayList<String>>() {};
            return objectMapper.readValue(file, typeRef);
        } catch (Exception e) {
            System.out.println("An error while reading file : " + e.getMessage());
            System.exit(2);
        }
        return null;
    }

    private static void fillStringListInput(String filePath) {
        List<String> stringList = List.of("Hi", "Piece", "World");
        try {
            objectMapper.writeValue(new File(filePath), stringList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
