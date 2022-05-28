package model;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    public static final int WIDTH = 490;
    public static final int HEIGHT = 550;
    public static final int MAIN_AREA_WIDTH = 700;

    private final Ball ball = new Ball();
    public final Platform platform = new Platform();

    private final int POINTS = 100;

    private List<Block> blocks;
    private Winner winner;
    private int score;
    private final int round = 1;


    GameField() {
        fillField();
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public void fillField() {
        blocks = new ArrayList<>();
        winner = Winner.NOBODY;

        blocks.add(new Block(50 + (Block.WIDTH), 100 + Block.HEIGHT * 4,     1));
        blocks.add(new Block(50 + (Block.WIDTH), 100 + Block.HEIGHT,         2));
        blocks.add(new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT,    1));
        blocks.add(new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT * 3,3));
        blocks.add(new Block(50 + (Block.WIDTH) * 4, 100 + Block.HEIGHT * 2, 2));
    }

    private boolean isAllBlocksDied() {
        return blocks.size() == 0;
    }

    public Integer getScore() {
        return score;
    }

    public boolean gameEnded() {
        return winner != Winner.NOBODY;
    }

    public boolean makeMove() {
        if (gameEnded()) return true;

        Position position = ball.move();
        Hit hit = getHit(position);

        if (hit != null) {
            boolean isAlive = hit.block.decreaseLives();
            if (!isAlive) {
                blocks.remove(hit.block);
                score += POINTS;
            }

            if (isAllBlocksDied()) {
                winner = Winner.PLAYER;
                ArkanoidFrame.condition = Condition.END_GAME;
//                ArkanoidFrame.update();
                return true;
            }
            ball.reflect(hit.hitType);
            return false;
        }

        if (wallCollision(position)) {
            ball.reflect();
            return false;
        }
        if (platformCollision(position)) {
            ball.platformReflect();
            return false;
        }
        if (outOfBounds(position)) {
            winner = Winner.COMPUTER;
            return true;
//            ArkanoidFrame.condition = Condition.END_GAME;
        }
        return false;
    }

    public List<GameObject> getGameObjects() {
        ArrayList<GameObject> list = new ArrayList<>();
        // CR: make int
        list.add(new GameObject((int) ball.getPositionX(), (int) ball.getPositionY(), ObjectType.BALL, ""));
        list.add(new GameObject(platform.getPositionX(), platform.getPositionY(), ObjectType.PLATFORM, ""));

        for(Block block : blocks) {
            list.add(new GameObject((int) block.getPosition().x, (int) block.getPosition().y, ObjectType.BLOCK, String.valueOf(block.numberOfLives)));
        }

        if(winner == Winner.COMPUTER) {
            list.add(new GameObject(400, 300, ObjectType.YOU_LOSE, ""));
        }

        if(winner == Winner.PLAYER) {
            list.add(new GameObject(400, 300, ObjectType.YOU_WIN, ""));
        }
        return list;
    }

    public int getWallWidth() {
        return 10;
    }

    record Hit(Block block, HitType hitType) {
        static Hit create(Block block, Position position) {
            HitType hit = getHitType(block, position);
            if(hit != null) return new Hit(block, hit);
            return null;
        }

        private static HitType getHitType(Block block, Position position) {
            return hit(block, position);
        }
    }

    private Hit getHit(Position position) {
        for (Block block : blocks) {
            Hit hit = Hit.create(block, position);
            if (hit != null) return hit;
        }
        return null;
    }

    private boolean outOfBounds(Position position) {
        return position.y > HEIGHT;
    }

    private boolean platformCollision(Position position) {
        return platform.getPositionY() - position.y < 2 * Ball.RADIUS + Block.EPS &&
                platform.getPositionY() - position.y >= 2 * Ball.RADIUS - Block.EPS &&

                position.x + 2 * Ball.RADIUS >= platform.getPositionX() &&
                position.x <= platform.getPositionX() + Platform.LEN_OF_PLATFORM;
    }

    private boolean wallCollision(Position position) {
        return position.x < 10 || position.x + (2 * Ball.RADIUS) >= WIDTH || position.y < 10;
    }

    private static HitType hit(Block block, Position position) {
        if(block.getPosition().x + Block.WIDTH + Block.EPS > position.x && block.getPosition().x - Block.EPS < position.x + 2 * Ball.RADIUS) {
            if(block.getPosition().y - Block.EPS < position.y + 2 * Ball.RADIUS && block.getPosition().y + Block.EPS > position.y + 2 * Ball.RADIUS) {
                return HitType.UP;
            }
            if(block.getPosition().y + Block.HEIGHT - Block.EPS < position.y && block.getPosition().y + Block.HEIGHT + Block.EPS >= position.y) {
                return HitType.DOWN;
            }
            return null;
        }
        if (block.getPosition().y < position.y + Ball.RADIUS && block.getPosition().y + Block.HEIGHT > position.y) {
           if(block.getPosition().x + Block.EPS > position.x + 2 * Ball.RADIUS && block.getPosition().x <= position.x + 2 * Ball.RADIUS) {
               return HitType.LEFT;
           }
           if(block.getPosition().x < position.x + 2 *  Ball.RADIUS && block.getPosition().x + Block.HEIGHT > position.x) {
               return HitType.RIGHT;
           }
        }
        return null;
    }

    public record Position(double x, double y) {}
}
