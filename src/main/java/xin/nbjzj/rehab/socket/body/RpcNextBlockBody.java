package xin.nbjzj.rehab.socket.body;

import lombok.Data;

/**
 * 请求next block时用的包装类
 * @author wuweifeng wrote on 2018/4/25.
 */
@Data
public class RpcNextBlockBody extends BaseBody {
    /**
     * blockHash
     */
    private String hash;
    /**
     * 上一个hash
     */
    private String prevHash;

    public RpcNextBlockBody() {
        super();
    }

    public RpcNextBlockBody(String hash, String prevHash) {
        super();
        this.hash = hash;
        this.prevHash = prevHash;
    }
}
