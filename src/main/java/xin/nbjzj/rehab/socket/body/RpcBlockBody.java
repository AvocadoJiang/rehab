package xin.nbjzj.rehab.socket.body;

import lombok.Data;
import xin.nbjzj.rehab.blockchain.block.Block;

/**
 * body里是一个block信息
 * @author wuweifeng wrote on 2018/3/12.
 */
@Data
public class RpcBlockBody extends BaseBody {
    /**
     * blockJson
     */
    private Block block;

    public RpcBlockBody() {
        super();
    }

    public RpcBlockBody(Block block) {
        super();
        this.block = block;
    }

}
