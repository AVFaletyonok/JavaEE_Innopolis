package org.example.figures;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Figure {

    protected double centerX;
    protected double centerY;

    public double getPerimeter() {
        return 0.0;
    }
}
