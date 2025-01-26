package org.example.figures;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class Ellipse extends Figure {

    protected double radiusX;
    protected double radiusY;

    public Ellipse(double centerX, double centerY,
                   double radiusX, double radiusY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * Math.sqrt(
                (Math.pow(radiusX, 2.0) + Math.pow(radiusY, 2.0)) / 2);
    }

    @Override
    public String toString() {
        return "Ellipse{" +
                "radiusX=" + radiusX +
                ", radiusY=" + radiusY +
                ", centerX=" + centerX +
                ", centerY=" + centerY + '}';
    }
}
