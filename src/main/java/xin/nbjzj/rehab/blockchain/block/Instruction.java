package xin.nbjzj.rehab.blockchain.block;

import lombok.Data;

/**
 * 区块body内一条指令
 * @author Jason wrote on 2018/11/27.
 */
@Data
public class Instruction{
	
	 /** 指令的操作，增删改（1，-1，2）**/
    private byte operation;
    /** 操作的表名 **/
    private String table;
    /** 新的内容  实体类 **/
    private String json;
    /** 时间戳 **/
    private Long timeStamp;
    /** 操作人的公钥 **/
    private String publicKey;
    /** 签名 **/
    private String sign;
    /** 该操作的hash **/
    private String hash;
	
    @Override
	public String toString() {
		return this.getOperation() + this.getTable() + this.getJson();
	}
}
