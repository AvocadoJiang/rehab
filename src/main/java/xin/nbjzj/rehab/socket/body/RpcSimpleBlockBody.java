package xin.nbjzj.rehab.socket.body;

import lombok.Data;

/**
 * @author wuweifeng wrote on 2018/4/25.
 */
@Data
public class RpcSimpleBlockBody extends BaseBody {
    /**
     * blockHash
     */
    private String hash;

    public RpcSimpleBlockBody() {
        super();
    }

    public RpcSimpleBlockBody(String hash) {
        super();
        this.hash = hash;
    }
}
