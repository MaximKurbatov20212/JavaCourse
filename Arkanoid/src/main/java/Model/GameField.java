package Model;

import Controller.GameStateHandler;

import java.util.Arrays;

public class GameField {
    public static final GameField INSTANCE = new GameField();
    private final Ball ball = Ball.INSTANCE;
    private final BlockManager blockManager = BlockManager.INSTANCE;
    private final Platform platform = Platform.INSTANCE;
    private Winner winner = Winner.NOBODY;
    private int score = 0;
    private int round = 1;
    private final int POINTS = 100;

    private static final int WIDTH = 490;
    public static final int HEIGHT = 550;

    public Integer getScore() {
        return score;
    }

    public void setScore(int i) {
        score = i;
    }

    public int getRound() {
        return round;
    }

    public enum Winner {
        PLAYER,
        COMPUTER,
        NOBODY
    }

    public boolean PlayerWin() {
        return winner == Winner.PLAYER;
    }

    public boolean ComputerWin() {
        return winner == Winner.COMPUTER;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    record Position(double x, double y) {}

    public void makeMove() {
        if (winner != Winner.NOBODY) return;

        Position position = ball.move();
        Block hit = getHittedBlock(position);

        if (hit != null) {
            hit.decreaseLives();

            if(!(hit.isLife())) score += POINTS;

            if (blockManager.isAllBlocksDied()) {
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

    private boolean outOfBounds(Position position) {
        return position.y > HEIGHT;
    }

    private boolean platformCollision(Position position) {
        return Platform.INSTANCE.getPositionY() - position.y < 2 * Ball.RADIUS + Ball.EPS  &&
                Platform.INSTANCE.getPositionY() - position.y >= 2 * Ball.RADIUS - Ball.EPS   &&

                position.x + 2 * Ball.RADIUS >= Platform.INSTANCE.getPositionX() &&
                position.x <= Platform.INSTANCE.getPositionX() + Platform.INSTANCE.getLen();
    }

    public int getAreaWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    private boolean wallCollision(Position position) {
        return position.x < 10
        || position.x + (2 * Ball.RADIUS) >= BackField.INSTANCE.getAreaWidth()
        || position.y < 10;
    }


    private Block getHittedBlock(Position position) {
        return Arrays.stream(blockManager.getBlocks())
                .filter(block -> block.isLife() && wasHit(block, position))
                .findFirst()
                .orElse(null);
    }

    private boolean wasHit(Block block, Position position) {
        return ballHitsBlockBelow(position, block) || ballHitsBlockAbove(position, block) || ballHitsBlockRight(position, block) || ballHitsBlockLeft(position, block);
    }


    boolean ballHitsBlockAbove(Position position, Block block) {
        if(     block.getPositionX() < position.x + Block.HEIGHT &&
                block.getPositionX() + Block.WIDTH > position.x &&
                block.getPositionY() - Ball.EPS < position.y + 2 * Ball.RADIUS &&
                block.getPositionY() + Ball.EPS > position.y + 2 * Ball.RADIUS) {
            ball.setPosition(position.x, position.y );
            return true;
        }
        return false;
    }

    boolean ballHitsBlockBelow(Position position, Block block) {
        if(block.getPositionX() < position.x + block.getHeight() &&
                block.getPositionX() + Block.WIDTH > position.x &&
                block.getPositionY() + Block.HEIGHT - Ball.EPS < position.y &&
                block.getPositionY() + Block.HEIGHT + Ball.EPS >= position.y) {
            ball.setPosition(position.x, position.y );
            return true;
        }
        return false;
    }

    boolean ballHitsBlockLeft(Position position, Block block) {
        if(     block.getPositionX() + Ball.EPS > position.x + Ball.RADIUS &&
                block.getPositionX() <= position.x + Ball.RADIUS &&
                block.getPositionY() < position.y + Ball.RADIUS &&
                block.getPositionY() + Block.HEIGHT > position.y) {

            ball.setPosition(position.x , position.y);
            return true;
        }
        return false;
    }

    boolean ballHitsBlockRight(Position position, Block block) {
        if(     block.getPositionX() + Block.WIDTH - Ball.EPS < position.x &&
                block.getPositionX() + Block.WIDTH  >= position.x &&
                block.getPositionY() < position.y + Ball.RADIUS &&
                block.getPositionY() + Block.HEIGHT > position.y) {

            ball.setPosition(position.x , position.y);
            return true;
        }
        return false;
    }
}
