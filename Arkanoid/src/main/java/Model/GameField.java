package Model;

import Controller.GameStateHandler;

import java.util.Arrays;

public class GameField {
    public static final GameField INSTANCE = new GameField();
    public static final int WIDTH = 490;
    public static final int HEIGHT = 550;
    public static int COUNT;

    private final Ball ball = Ball.INSTANCE;
    private final int POINTS = 100;
    Block[] blocks;
    private Winner winner;
    private int score;
    private final int round = 1;


    private GameField() {
        fillField();
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public void fillField() {
        COUNT = 5;
        blocks = new Block[COUNT];
        winner = Winner.NOBODY;

        blocks[0] = new Block(50 + (Block.WIDTH), 100 + Block.HEIGHT * 4);
        blocks[1] = new Block(50 + (Block.WIDTH), 100 + Block.HEIGHT);
        blocks[2] = new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT);
        blocks[3] = new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT * 3);
        blocks[4] = new Block(50 + (Block.WIDTH) * 4, 100 + Block.HEIGHT * 2);
    }

    public void fillField(int count, int lives, Position... positions) {
        winner = Winner.NOBODY;
        int i = 0;
        COUNT = count;
        blocks = new Block[COUNT];
        for (Position position : positions) {
            blocks[i++] = new Block((int) position.x, (int) position.y, lives);
            COUNT++;
        }
    }

    private boolean isAllBlocksDied() {
        // CR: use list: return blocks.size() == 0;
        return Arrays.stream(blocks).noneMatch(Block::isAlive);
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int i) {
        score = i;
    }

    public int getRound() {
        return round;
    }

    public boolean playerWin() {
        return winner == Winner.PLAYER;
    }

    public boolean gameEnded() {
        return winner != Winner.NOBODY;
    }

    public boolean computerWin() {
        return winner == Winner.COMPUTER;
    }

    public void makeMove() {
        if (gameEnded()) return;

        Position position = ball.move();
//        Hit hit = getHit(position);
        Block hit = getHittedBlock(position);

        if (hit != null) {
            hit.decreaseLives();
//            boolean isDead = hit.decreaseLives();
//            if (isDead) {
//                // remove from list
//                // add score
//            }

            if (!(hit.isAlive())) score += POINTS;

            if (isAllBlocksDied()) {
                winner = Winner.PLAYER;
                GameStateHandler.INSTANCE.condition = GameStateHandler.STOP_THE_WORLD;
                return;
            }
            ball.reflect(hit);
            return;
        }

        if (wallCollision(position)) {
            ball.reflect();
            return;
        }
        if (platformCollision(position)) {
            ball.platformReflect();
            return;
        }
        if (outOfBounds(position)) {
            winner = Winner.COMPUTER;
            GameStateHandler.INSTANCE.condition = GameStateHandler.STOP_THE_WORLD;
        }
    }

//    record Hit(Block block, HitType hitType) {
//        static Hit create(Block block) {
//            // check if hitted, if not - return null
//        }
//    }
//
//    private Hit getHit() {
//        for (Block block : blocks) {
//            Hit hit = Hit.create(block);
//            if (hit != null) return hit;
//        }
//        return null;
//    }


    public int getAreaWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    private boolean outOfBounds(Position position) {
        return position.y > HEIGHT;
    }

    private boolean platformCollision(Position position) {
        return Platform.INSTANCE.getPositionY() - position.y < 2 * Ball.RADIUS + Ball.EPS &&
                Platform.INSTANCE.getPositionY() - position.y >= 2 * Ball.RADIUS - Ball.EPS &&

                position.x + 2 * Ball.RADIUS >= Platform.INSTANCE.getPositionX() &&
                position.x <= Platform.INSTANCE.getPositionX() + Platform.LEN_OF_PLATFORM;
    }

    private boolean wallCollision(Position position) {
        return position.x < 10
                || position.x + (2 * Ball.RADIUS) >= BackField.INSTANCE.getAreaWidth()
                || position.y < 10;
    }

    public Block getHittedBlock(Position position) {
        for (Block block : blocks) {
            if (block.isAlive() && wasHit(block, position)) {
                return block;
            }
        }
        return null;
    }

    private boolean wasHit(Block block, Position position) {
        return ballHitsBlockBelow(position, block) || ballHitsBlockAbove(position, block) || ballHitsBlockRight(position, block) || ballHitsBlockLeft(position, block);
    }

    boolean ballHitsBlockAbove(Position position, Block block) {
        return block.getPositionX() + Block.WIDTH + Ball.EPS > position.x &&
                block.getPositionX() - Ball.EPS < position.x + 2 * Ball.RADIUS &&

                block.getPositionY() - Ball.EPS < position.y + 2 * Ball.RADIUS &&
                block.getPositionY() + Ball.EPS > position.y + 2 * Ball.RADIUS;
    }

    boolean ballHitsBlockBelow(Position position, Block block) {
        return block.getPositionX() + Block.WIDTH + Ball.EPS > position.x &&
                block.getPositionX() - Ball.EPS < position.x + 2 * Ball.RADIUS &&

                block.getPositionY() + Block.HEIGHT - Ball.EPS < position.y &&
                block.getPositionY() + Block.HEIGHT + Ball.EPS >= position.y;
    }

    boolean ballHitsBlockLeft(Position position, Block block) {

        return block.getPositionX() + Ball.EPS > position.x + 2 * Ball.RADIUS &&
                block.getPositionX() <= position.x + 2 * Ball.RADIUS &&

                block.getPositionY() < position.y + Ball.RADIUS &&
                block.getPositionY() + Block.HEIGHT > position.y;
    }

    boolean ballHitsBlockRight(Position position, Block block) {
        return block.getPositionX() + Block.WIDTH - Ball.EPS < position.x &&
                block.getPositionX() + Block.WIDTH >= position.x &&

                block.getPositionY() < position.y + Ball.RADIUS &&
                block.getPositionY() + Block.HEIGHT > position.y;
    }

    public enum Winner {
        PLAYER,
        COMPUTER,
        NOBODY
    }

    public record Position(double x, double y) {
    }
}
