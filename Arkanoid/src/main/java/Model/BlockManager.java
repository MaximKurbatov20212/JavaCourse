package Model;

import java.util.Arrays;

public class BlockManager {
    public static final BlockManager INSTANCE = new BlockManager();
    public static final int COUNT = 5;
    Block[] blocks = new Block[COUNT];

    private BlockManager() {
        fillField();
    }

    public void fillField() {
        blocks[0] = new Block(50 + (Block.WIDTH) , 100 + Block.HEIGHT  * 4);
        blocks[1] = new Block(50 + (Block.WIDTH) , 100 + Block.HEIGHT  );
        blocks[2] = new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT  );
        blocks[3] = new Block(50 + (Block.WIDTH) * 2, 100 + Block.HEIGHT * 3 );
        blocks[4] = new Block(50 + (Block.WIDTH) * 4 , 100 + Block.HEIGHT * 2 );
    }

    public boolean isAllBlocksDied() {
        return Arrays.stream(blocks).noneMatch(Block::isLife);
    }

    public Block[] getBlocks() {
        return blocks;
    }
}
