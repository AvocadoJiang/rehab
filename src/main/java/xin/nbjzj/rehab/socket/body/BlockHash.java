package xin.nbjzj.rehab.socket.body;

import lombok.Data;

/**
 * @author wuweifeng wrote on 2018/4/26.
 */
@Data
public class BlockHash {
    private String hash;
    private String prevHash;
    private String appId;

    public BlockHash() {
    }

    public BlockHash(String hash, String prevHash, String appId) {
        this.hash = hash;
        this.prevHash = prevHash;
        this.appId = appId;
    }
}
