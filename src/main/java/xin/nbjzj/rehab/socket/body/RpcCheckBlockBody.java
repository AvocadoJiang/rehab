package xin.nbjzj.rehab.socket.body;

import lombok.Data;
import xin.nbjzj.rehab.blockchain.block.Block;

/**
 * 校验block是否合法，同意、拒绝区块生成请求
 * @author wuweifeng wrote on 2018/3/12.
 */
@Data
public class RpcCheckBlockBody extends RpcBlockBody {
    /**
     * 0是正常同意，-1区块number错误，-2没有权限，-3hash错误，-4时间错误，-10不合法的next block
     */
    private int code;
    /**
     * 附带的message
     */
    private String message;

    public RpcCheckBlockBody() {
    }

    public RpcCheckBlockBody(int code, String message) {
        this(code, message, null);
    }

    public RpcCheckBlockBody(int code, String message, Block block) {
        super(block);
        this.code = code;
        this.message = message;
    }

}
