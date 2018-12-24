package xin.nbjzj.rehab.blockchain.block;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.Data;

/**
 * 区块
 * @author wuweifeng wrote on 2018/2/27.
 */
@Data
public class Block {
    /**
     * 区块头
     */
    private BlockHeader blockHeader;
    /**
     * 区块body
     */
    private BlockBody blockBody;
    /**
     * 该区块的hash
     */
    private String hash;

    /**
     * 根据该区块所有属性计算sha256
     * @return
     * sha256hex
     */
    private String calculateHash() {
        return DigestUtil.sha256Hex(
                        blockHeader.toString() + blockBody.toString()
        );
    }
}
