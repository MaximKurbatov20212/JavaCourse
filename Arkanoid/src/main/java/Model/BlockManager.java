package Model;

public class BlockManager {
    public static final BlockManager INSTANCE = new BlockManager();
    public static final int COUNT = 5;
    private int numberLiveBlocks;
    Block[] blocks = new Block[COUNT];

    private BlockManager() {
        numberLiveBlocks = COUNT;
        fillField();
    }

    public void fillField() {
//        for(int i = 0; i < 5; i++) {
//            for(int j = 0;  j < 1; j++) {
//                blocks[i * 5 + j] = new Block(50 + (Block.WIDTH + 1) * j, 100 + Block.HEIGHT  * i);
//            }
//        }
        blocks[0] = new Block(50 + (Block.WIDTH) , 100 + Block.HEIGHT  * 4 );
        blocks[1] = new Block(50 + (Block.WIDTH) , 100 + Block.HEIGHT  );
        blocks[2] = new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT  );
        blocks[3] = new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT * 3 );
        blocks[4] = new Block(50 + (Block.WIDTH) * 4 , 100 + Block.HEIGHT * 2 );
        numberLiveBlocks = COUNT;
    }

    public boolean isAllBlocksDied() {
        return numberLiveBlocks == 0;
    }

    public Block getBlock(int i) {
        if(i > COUNT) {
            throw new AssertionError("No such block");
        }
        return blocks[i];
    }

    public void decreaseLives(Block block) {
        block.decreaseLives();
        if(!block.isLife()) {
            numberLiveBlocks--;
        }
    }

    public Block[] getBlocks() {
        return blocks;
    }
}
