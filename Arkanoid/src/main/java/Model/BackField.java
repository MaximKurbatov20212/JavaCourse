package Model;

public class BackField {
    public final static BackField INSTANCE = new BackField();
    private static final int WALL_WIDTH = 10;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 550;
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
