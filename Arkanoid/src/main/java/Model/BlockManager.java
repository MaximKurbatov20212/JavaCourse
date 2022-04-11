package Model;

public class BlockManager {
    public static final BlockManager INSTANCE = new BlockManager();
    public static final int COUNT = 25;
    private int numberLiveBlocks;
    Block[] blocks = new Block[COUNT];

    private BlockManager() {
        numberLiveBlocks = COUNT;
        fillField();
    }

    public void fillField() {
        for(int i = 0; i < 5; i++) {
            for(int j = 0;  j < 5; j++) {
                blocks[i * 5 + j] = new Block(50 + (Block.WIDTH + 1) * j, 100 + Block.HEIGHT  * i);
            }
        }
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
}
