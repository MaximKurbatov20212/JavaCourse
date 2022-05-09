package Model;

import static Controller.GameStateHandler.delay;

public class Ball {
    public final static Ball INSTANCE = new Ball();
    private final int radius = 10;
    private double positionX = 200;
    private double positionY = 450;
    public static final int EPS = 5;

    private Ball() {}

    private final DirectingVector dVector = new DirectingVector();

    public void setDirectingVector(double x, double y) {
        dVector.x = x;
        dVector.y = y;
        dVector.norm();
    }

    public int getRadius() {
        return radius;
    }

    public void reflect(Block block) {
        GameField gameField = GameField.INSTANCE;
        if (gameField.ballHitsBlockBelow(new GameField.Position(positionX, positionY), block)) {
            setDirectingVector(dVector.x, -dVector.y);
        } else if (gameField.ballHitsBlockAbove(new GameField.Position(positionX, positionY), block)) {
            setDirectingVector(dVector.x, -dVector.y);
        } else if (gameField.ballHitsBlockRight(new GameField.Position(positionX, positionY), block)) {
            setDirectingVector(-dVector.x, dVector.y);
        } else if (gameField.ballHitsBlockLeft(new GameField.Position(positionX, positionY), block)) {
            setDirectingVector(-dVector.x, dVector.y);
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
        return positionX + (2 * radius) >= GameField.INSTANCE.getAreaWidth();
    }

    private boolean hitCeiling() {
        return positionY <= 10;
    }


    public void platformReflect() {
        Platform platform = Platform.INSTANCE;
        double halfWidth = (double)platform.getLen() / 2;
        double x = halfWidth - (getBallCenterX() - platform.getPositionX());
        double y = Math.sqrt(Math.abs(halfWidth * halfWidth - x * x));
        dVector.x = x;
        dVector.y = y;
        dVector.norm();
    }

    private double getBallCenterX() {
        return positionX + radius;
    }

    private static class DirectingVector {
        private double x;
        private double y = 1.0f;

        public void norm() {
            x = x / Math.sqrt(x*x + y*y);
            y = y / Math.sqrt(x*x + y*y);
        }
    }

    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public GameField.Position move() {
        double s = 0.3 * delay;
        positionX -= dVector.x * s;
        positionY -= dVector.y * s;
        return new GameField.Position(positionX, positionY);
    }
}
