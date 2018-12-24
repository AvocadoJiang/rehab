package xin.nbjzj.rehab.blockchain.block;

import java.util.List;

import lombok.Data;

/**
 * 区块头
 * @author wuweifeng wrote on 2018/2/27.
 */
@Data
public class BlockHeader {
    /**
     * 版本号
     */
    private int version;
    /**
     * 上一区块的hash
     */
    private String hashPreviousBlock;
    /**
     * merkle tree根节点hash
     */
    private String hashMerkleRoot;
    /**
     * 生成该区块的公钥
     */
    private String publicKey;
    /**
     * 区块的序号
     */
    private int number;
    /**
     * 时间戳
     */
    private long timeStamp;
    /**
     * 32位随机数
     */
    private long nonce;
    /**
     * 该区块里每条交易信息的hash集合，按顺序来的，通过该hash集合能算出根节点hash
     */
    private List<String> hashList;

}
