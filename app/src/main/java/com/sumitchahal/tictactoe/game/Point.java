package com.sumitchahal.tictactoe.game;

public class Point {

    public final int x, y;

    public Point(int x, int y) {
        if (0 <= x && x <= 2 && 0 <= y && y <= 2) {
            this.x = x;
            this.y = y;
        } else {
            throw new UnsupportedOperationException("x and y must lie between 0 and 2 (inclusive)");
        }
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
