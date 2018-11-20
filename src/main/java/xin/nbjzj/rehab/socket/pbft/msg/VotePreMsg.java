package xin.nbjzj.rehab.socket.pbft.msg;

import xin.nbjzj.rehab.blockchain.block.Block;

/**
 * @author wuweifeng wrote on 2018/4/25.
 */
public class VotePreMsg extends VoteMsg {
    private Block block;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
