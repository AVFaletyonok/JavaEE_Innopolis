package org.example;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class Square extends Rectangle implements IMovable {

    public Square (double centerX, double centerY, double side) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.sideA = this.sideB = side;
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
        return "Square{" +
                "sideA=" + sideA +
                ", sideB=" + sideB +
                ", centerX=" + centerX +
                ", centerY=" + centerY + '}';
    }
}
