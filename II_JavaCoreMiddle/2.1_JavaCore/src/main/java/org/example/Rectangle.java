package org.example;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class Rectangle extends Figure {

    protected double sideA;
    protected double sideB;

    public Rectangle (double centerX, double centerY,
                   double sideA, double sideB) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.sideA = sideA;
        this.sideB = sideB;
    }

    @Override
    public double getPerimeter() {
        return 2 * (sideA + sideB);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "sideA=" + sideA +
                ", sideB=" + sideB +
                ", centerX=" + centerX +
                ", centerY=" + centerY + '}';
    }
}
