package xin.nbjzj.rehab.blockchain.block;

import lombok.Data;

/**
 * blockBody内一条指令的基础属性
 * @author wuweifeng wrote on 2018/4/4.
 */
@Data
public class InstructionBase {
    /**
     * 指令的操作，增删改（1，-1，2）
     */
    private byte operation;
    /**
     * 操作的表名
     */
    private String table;
    /**
     * 最终要执行入库的json内容
     */
    private String oldJson;
    /**
     * 业务id，sql语句中where需要该Id
     */
    private String instructionId;

}
