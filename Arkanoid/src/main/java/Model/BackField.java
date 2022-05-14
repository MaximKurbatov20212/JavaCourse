package Model;

public class BackField {
    public final static BackField INSTANCE = new BackField();
    // CR: Field, or even better remove
    private static final int WALL_WIDTH = 10;
    // CR: Field
    public static final int WIDTH = 700;
    // CR: Field
    public static final int HEIGHT = 550;
    // CR: Field, or even better remove
    private static final int BALL_AREA_WIDTH = 490;

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getAreaWidth() {
        return BALL_AREA_WIDTH;
    }

    public int getWallWidth() {
        return WALL_WIDTH;
    }

}
