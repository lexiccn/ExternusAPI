package me.deltaorion.extapi.test.animation;

import net.jcip.annotations.Immutable;

@Immutable
public class PolarVector {

    private final int length;
    private final double angle;

    public PolarVector(int length, double angle) {
        this.length = length;
        this.angle = angle;
    }

    public int getLength() {
        return length;
    }

    public double getAngle() {
        return angle;
    }

    public double getCartesianX() {
        return length * Math.cos(angle);
    }

    public double getCartesianY() {
        return length * Math.sin(angle);
    }
}
