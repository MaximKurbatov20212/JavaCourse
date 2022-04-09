package Model;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Ball extends JFrame {
    public final static Ball INSTANCE = new Ball();
    private Image ballImage;
    private final int radius = 10;
    private int positionX = 200;
    private int positionY = 450;

    public static final int EPS = 5;

    private final DirectingVector dVector = new DirectingVector();

    private Ball() {
        loadImage();
    }

    public double getDirectingVectorCordY() {
        return dVector.y;
    }
    public double getDirectingVectorCordX() {
        return dVector.x;
    }

    public void setDirectingVector(double x, double y) {
        dVector.x = x;
        dVector.y = y;
        dVector.norm();
    }

    public int getRadius() {
        return radius;
    }


    private static class DirectingVector {
        private double x;
        private double y = 1.0f;

        private void norm() {
            x = x / Math.sqrt(x*x + y*y);
            y = y / Math.sqrt(x*x + y*y);
        }
    }

    // CR: move
    private void loadImage() {
        ImageIcon iBall = new ImageIcon("src/Pictures/ball.png");
        ballImage = iBall.getImage().getScaledInstance(20,20,  Image.SCALE_DEFAULT);
    }

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public Image getImage() {
        return ballImage;
    }

    private boolean hitLeftWall() {
        return positionX <= 10;
    }

    private boolean hitRightWall() {
        return positionX + (2 * radius) >= GameField.INSTANCE.getAreaWidth();
    }

    private int getBallCenterX() {
        return positionX + 10;
    }

    private boolean hitFloor() {
        if(positionY + GameField.INSTANCE.getWallWidth() > GameField.INSTANCE.getHeight()) {
            System.out.println("HIT Floor");
            return true;
        }
        return false;
    }

    private boolean hitCeiling() {
        return positionY <= 10;
    }

    private boolean hitWithPlatform() {
        return Platform.INSTANCE.getPositionY() - positionY < 2 * radius + 5 &&
                Platform.INSTANCE.getPositionY() - positionY >= 2 * radius - 5 &&

                positionX + 2 * radius >= Platform.INSTANCE.getPositionX() &&
                positionX <= Platform.INSTANCE.getPositionX() + Platform.INSTANCE.getLen();
    }

    public void move() {
        positionX -= dVector.x * 8;
        positionY -= dVector.y * 8;
        if(hitFloor()) {
            GameField.INSTANCE.setLoseVisible(true);
            return;
        }

        else if(hitCeiling()) {
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

        if(hitWithPlatform()) {
            Platform platform = Platform.INSTANCE;
            double halfWidth = (double)platform.getLen() / 2;
            double x = halfWidth - (getBallCenterX() - platform.getPositionX());
            double y = Math.sqrt(Math.abs(halfWidth * halfWidth - x * x));
            dVector.x = x;
            dVector.y = y;
        }
    }
}
