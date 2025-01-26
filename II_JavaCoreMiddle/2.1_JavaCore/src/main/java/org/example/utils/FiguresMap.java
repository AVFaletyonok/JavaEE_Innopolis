package org.example.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.figures.Circle;
import org.example.figures.Ellipse;
import org.example.figures.Rectangle;
import org.example.figures.Square;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FiguresMap {
    private List<Ellipse> ellipses = new ArrayList<>();
    private List<Circle> circles = new ArrayList<>();
    private List<Rectangle> rectangles = new ArrayList<>();
    private List<Square> squares = new ArrayList<>();

    public void addEllipse(Ellipse ellipse) {
        ellipses.add(ellipse);
    }

    public void addCircle(Circle circle) {
        circles.add(circle);
    }

    public void addRectangle(Rectangle rectangle) {
        rectangles.add(rectangle);
    }

    public void addSquare (Square square) {
        squares.add(square);
    }
}
