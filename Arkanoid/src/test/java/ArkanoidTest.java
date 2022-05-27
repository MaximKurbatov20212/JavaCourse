//import model.Ball;
//import model.Block;
//import model.GameField;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//public class ArkanoidTest {
//    GameField gameField = GameField.INSTANCE;
//    Ball ball = Ball.INSTANCE;
//
//
//    @Test
//    public void blockCollision() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(100 + (int) (Block.WIDTH / 2), 100 + Block.HEIGHT);
//        gameField.fillField(1, 1, blockPosition);
//        ball.setPosition(blockPosition);
//
//        // hit below
//        Assertions.assertNotNull(gameField.getHittedBlock(ballPosition));
//
//        // hit above
//        ballPosition = new GameField.Position(100 + (int) (Block.WIDTH / 2), 100 + 2 * Ball.RADIUS);
//        Assertions.assertNotNull(gameField.getHittedBlock(ballPosition));
//
//        // hit left
//        ballPosition = new GameField.Position(100 - 2 * Ball.RADIUS, 100 + (int) (Block.HEIGHT / 2));
//        Assertions.assertNotNull(gameField.getHittedBlock(ballPosition));
//
//        // hit right
//        ballPosition = new GameField.Position(100 + Block.WIDTH, 100 + (int) (Block.HEIGHT / 2));
//        Assertions.assertNotNull(gameField.getHittedBlock(ballPosition));
//    }
//
//    @Test
//    public void blockHittedBelow() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(100 + (int) (Block.WIDTH / 2), 100 + Block.HEIGHT + Ball.STEP);
//
//        for(int lives = 1; lives <= 3; lives++) {
//            gameField.fillField(1,lives, blockPosition);
//            ball.setDirectingVector(0, 1);
//            ball.setPosition(ballPosition);
//            gameField.makeMove();
//            Assertions.assertEquals(lives - 1, gameField.getBlocks()[0].getLives());
//        }
//    }
//
//    @Test
//    public void blockHittedAbove() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(100 + (int) (Block.WIDTH / 2), 100 - 2 * Ball.RADIUS - Ball.STEP);
//
//        for(int lives = 1; lives <= 3; lives++) {
//
//            gameField.fillField(1, lives, blockPosition);
//            ball.setDirectingVector(0, -1);
//            ball.setPosition(ballPosition);
//
//            gameField.makeMove();
//            Assertions.assertEquals(lives - 1, gameField.getBlocks()[0].getLives());
//        }
//    }
//
//    @Test
//    public void blockHittedRight() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(100 + Block.WIDTH + Ball.STEP, 100 + (Block.HEIGHT / 2));
//
//        for(int lives = 1; lives <= 3; lives++) {
//            gameField.fillField(1, lives, blockPosition);
//            ball.setDirectingVector(-1, 0);
//            ball.setPosition(ballPosition);
//
//            gameField.makeMove();
//            Assertions.assertEquals(lives - 1, gameField.getBlocks()[0].getLives());
//        }
//    }
//
//    @Test
//    public void blockHittedLeft() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(100 - Ball.STEP - 2 * Ball.RADIUS, 100 + (Block.HEIGHT / 2));
//
//        for(int lives = 1; lives <= 3; lives++) {
//            gameField.fillField(1, lives, blockPosition);
//            ball.setDirectingVector(1, 0);
//            ball.setPosition(ballPosition);
//
//            gameField.makeMove();
//            Assertions.assertEquals(lives - 1, gameField.getBlocks()[0].getLives());
//        }
//    }
//
//    @Test
//    public void playerLose() {
//        GameField.Position ballPosition = new GameField.Position(GameField.WIDTH / 2, GameField.HEIGHT);
//        ball.setPosition(ballPosition);
//        ball.setDirectingVector(0, -1);
//
//        gameField.makeMove();
//        Assertions.assertEquals(GameField.Winner.COMPUTER, gameField.getWinner());
//    }
//
//    @Test
//    public void playerWin() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(100 + (int) (Block.WIDTH / 2), 100 + Block.HEIGHT + Ball.STEP);
//
//        gameField.fillField(1, 1, blockPosition);
//        ball.setDirectingVector(0, 1);
//        ball.setPosition(ballPosition);
//        gameField.makeMove();
//
//        Assertions.assertEquals(GameField.Winner.PLAYER, gameField.getWinner());
//    }
//
//    @Test
//    public void moveAtLeftEdge() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(14, 80);
//        gameField.fillField(1, 1, blockPosition);
//
//        ball.setDirectingVector(1, 0);
//        ball.setPosition(ballPosition);
//
//        gameField.makeMove();
//        gameField.makeMove();
//        gameField.makeMove();
//        Assertions.assertTrue(ball.getPositionX() > 10);
//    }
//
//    @Test
//    public void moveAtRightEdge() {
//        GameField.Position blockPosition = new GameField.Position(100, 100);
//        GameField.Position ballPosition = new GameField.Position(495, 80);
//        gameField.fillField(1, 1, blockPosition);
//
//        ball.setDirectingVector(-1, 0);
//        ball.setPosition(ballPosition);
//
//        gameField.makeMove();
//        gameField.makeMove();
//        gameField.makeMove();
//        Assertions.assertTrue(ball.getPositionX() < 500);
//    }
//}
