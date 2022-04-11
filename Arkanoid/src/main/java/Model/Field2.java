package Model;

//public class Field2 {
//
//    private final Ball ball;
//    private final Platform platform;
//    private final Block[] blocks;
//    private Winner winner;
//    int liveBlocks = N_BLOCKS;
//
//    enum Winner {
//        PLAYER,
//        COMPUTER
//    }
//
//    private static final int N_BLOCKS = 25;
//
//    record Position(double x, double y) {}
//
//    void makeMove() {
//        if (winner != null) return;
//        Position position = ball.move();
//        Block hit = getHittedBlock(position);
//        if (hit != null) {
//            hit.decreaseLives();
//            if (liveBlocks == 0) {
//                winner = Winner.PLAYER;
//                return;
//            }
//            ball.reflect();
//            return;
//        }
//        if (wallCollision(position)) {
//            ball.reflect();
//            return;
//        }
//        if (platformCollision(position)) {
//            ball.platformReflect();
//            return;
//        }
//        if (outOfBounds(position)) {
//            winner = Winner.COMPUTER;
//        }
//    }
//}
