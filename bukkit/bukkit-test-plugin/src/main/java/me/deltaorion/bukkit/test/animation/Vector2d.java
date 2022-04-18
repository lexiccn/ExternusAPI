package me.deltaorion.bukkit.test.animation;

public class Vector2d {

    public final double x;
    public final double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ","+y+")";
    }
}
