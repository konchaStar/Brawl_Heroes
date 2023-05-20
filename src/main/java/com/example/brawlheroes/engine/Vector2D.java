package com.example.brawlheroes.engine;

import javafx.geometry.Point2D;

public class Vector2D {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2D normalize() {
        if((x != 0) || (y != 0)) {
            double magnitude = Math.sqrt(x * x + y * y);
            x = x / magnitude;
            y = y / magnitude;
        }
        return this;
    }
    public Vector2D add(Vector2D vector) {
        this.x = this.x + vector.getX();
        this.y = this.y + vector.getY();
        normalize();
        return this;
    }
    public Vector2D multiply(double value) {
        return new Vector2D(this.getX() * value, this.getY() * value);
    }
    public Vector2D subtract(Vector2D vector) {
        this.x = this.x - vector.getX();
        this.y = this.y - vector.getY();
        normalize();
        return this;
    }
    public Vector2D subtract(Point2D point) {
        this.x = this.x - point.getX();
        this.y = this.y - point.getY();
        normalize();
        return this;
    }
    public double dot(Vector2D vector) {
        return this.x * vector.x + this.y * vector.y;
    }
}