package model;

/**
 * This class represents a point
 */
public class Point {

    private int x;
    private int y;

    /**
     * Constructs point
     * @param x X-position of the point
     * @param y Y-position of the point
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method returns X-position of this point
     * @return X-position of this point
     */
    public int getX() {
        return x;
    }

    /**
     * This method set X-position of this point
     * @param x X-position of this point
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * This method returns Y-position of this point
     * @return Y-position of this point
     */
    public int getY() {
        return y;
    }

    /**
     * This method set Y-position of this point
     * @param y Y-position of this point
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
