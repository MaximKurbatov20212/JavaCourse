package model;

import static controller.GameStateHandler.gameDelay;

public class Ball {
    public static final int RADIUS = 10;

    private double positionX;
    private double positionY;

    public static final double STEP = 0.25 * gameDelay;

    public Ball() {
        positionX = 200;
        positionY = 350;
        dVector.x = 0;
        dVector.y = 1;
    }

    private final DirectingVector dVector = new DirectingVector();

    public void setDirectingVector(double x, double y) {
        dVector.x = x;
        dVector.y = y;
        dVector.norm();
    }

    public void reflect(HitType hitType) {
        switch (hitType) {
            case RIGHT, LEFT -> setDirectingVector(-dVector.x, dVector.y);
            case UP, DOWN -> setDirectingVector(dVector.x, -dVector.y);
        }
    }

    public void reflect() {
        if(hitCeiling()) {
            positionY++;
            dVector.y = -dVector.y;
        }

        else if(hitLeftWall()) {
            positionX++;
            dVector.x = -dVector.x;
        }
        else if(hitRightWall()) {
            positionX--;
            dVector.x = -dVector.x;
        }
    }

    private boolean hitLeftWall() {
        return positionX <= 10;
    }

    private boolean hitRightWall() {
        return positionX + (2 * RADIUS) >= GameField.WIDTH;
    }

    private boolean hitCeiling() {
        return positionY <= 10;
    }

    public void platformReflect() {

        double halfWidth = (double) Platform.LEN_OF_PLATFORM / 2;
        double x = -halfWidth + (getBallCenterX() - ArkanoidFrame.gameField.platform.getPositionX());
        double y = Math.sqrt(Math.abs(halfWidth * halfWidth - x * x));

        dVector.x = x;
        dVector.y = y;
        dVector.norm();
    }

    private double getBallCenterX() {
        return positionX + RADIUS;
    }

    public void setPosition(GameField.Position position) {
        positionX = position.x();
        positionY = position.y();
    }

    private static class DirectingVector {
        private double x;
        private double y = -1.0f;

        public void norm() {
            x = x / Math.sqrt(x*x + y*y);
            y = y / Math.sqrt(x*x + y*y);
        }
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public GameField.Position move() {
        dVector.norm();
        positionX += dVector.x * STEP;
        positionY -= dVector.y * STEP;
        return new GameField.Position(positionX, positionY);
    }
}
