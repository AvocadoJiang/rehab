package xin.nbjzj.rehab.blockchain.block;

import java.util.List;

import lombok.Data;

/**
 * 区块body，里面存放交易的数组
 * @author wuweifeng wrote on 2018/2/28.
 */
@Data
public class BlockBody {
    private List<Instruction> instructions;

}
