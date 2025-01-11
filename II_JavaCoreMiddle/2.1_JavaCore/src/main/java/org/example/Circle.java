package org.example;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class Circle extends Ellipse implements IMovable {

    public Circle(double centerX, double centerY, double radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radiusX = this.radiusY = radius;
    }

    @Override
    public void Move(double deltaX, double deltaY) {
        centerX += deltaX;
        centerY += deltaY;
    }

    @Override
    public void ChangePosition(double X, double Y) {
        centerX = X;
        centerY = Y;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "radiusX=" + radiusX +
                ", radiusY=" + radiusY +
                ", centerX=" + centerX +
                ", centerY=" + centerY + '}';
    }
}
