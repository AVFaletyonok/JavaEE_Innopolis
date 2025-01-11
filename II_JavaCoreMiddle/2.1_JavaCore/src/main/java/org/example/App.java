package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class App
{
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static FiguresMap figuresMap = new FiguresMap();

    public static void main( String[] args ) {
        JsonFileToPojo("II_JavaCoreMiddle/2.1_JavaCore/src/main/resources/figures_set.json");

        try(FileWriter writer = new FileWriter(
                "II_JavaCoreMiddle/2.1_JavaCore/src/main/resources/logs.txt", true)) {

            LocalDateTime currentDateTime = LocalDateTime.now();
            writer.write(currentDateTime.toString() + "\n");
            writer.write("Figures:\nEllipses:\n");
            writer.write(figuresMap.getEllipses().toString() + "\n");
            writer.write("Circles:\n");
            writer.write(figuresMap.getCircles().toString() + "\n");
            writer.write("Rectangles:\n");
            writer.write(figuresMap.getRectangles().toString() + "\n");
            writer.write("Squares:\n");
            writer.write(figuresMap.getSquares().toString() + "\n");

            for(Circle circle : figuresMap.getCircles()) {
                writer.write("Circle before moving : ");
                writer.write(circle.toString() + "\n");
                circle.ChangePosition(0.0, 0.0);
                writer.write("Circle after moving to the center of a grid : ");
                writer.write(circle.toString() + "\n");
            }
            for(Square square : figuresMap.getSquares()) {
                writer.write("Square before moving : ");
                writer.write(square.toString() + "\n");
                square.ChangePosition(0.0, 0.0);
                writer.write("Square after moving to the center of a grid : ");
                writer.write(square.toString() + "\n");
            }
            writer.write("--------------------\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void JsonFileToPojo(String filePath) {
        File file = new File(filePath);
        boolean isExistFile = false;
        if (!filePath.isBlank()) isExistFile = file.exists();
        if (!isExistFile) {
            System.out.println("The input file doesn't exist : " + filePath);
            System.exit(1);
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            figuresMap = objectMapper.readValue(file, FiguresMap.class);
        } catch (Exception e) {
            System.out.println("An error while reading file : " + e.getMessage());
            System.exit(2);
        }
    }

    private static void FillFiguresSet() {
        figuresMap.addEllipse(new Ellipse(10.0, 10.0, 10.0, 20.0));
        figuresMap.addEllipse(new Ellipse(11.0, 11.0, 15.0, 10.0));
        figuresMap.addCircle(new Circle(20.0, 20.0, 5.0));
        figuresMap.addCircle(new Circle(21.0, 21.0, 15.0));
        figuresMap.addRectangle(new Rectangle(30.0, 30.0, 3.0, 4.0));
        figuresMap.addRectangle(new Rectangle(31.0, 31.0, 5.0, 7.0));
        figuresMap.addSquare(new Square(40.0, 40.0, 6.0));
        figuresMap.addSquare(new Square(41.0, 41.0, 4.0));
        String json = null;
        try {
            json = objectMapper.writeValueAsString(figuresMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            objectMapper.writeValue(new File("II_JavaCoreMiddle/2.1_JavaCore/src/main/resources/figures_set.json"), figuresMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
