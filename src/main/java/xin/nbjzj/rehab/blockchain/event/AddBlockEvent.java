package xin.nbjzj.rehab.blockchain.event;

import org.springframework.context.ApplicationEvent;

import xin.nbjzj.rehab.blockchain.block.Block;

/**
 * 确定生成block的Event（添加到rocksDB，执行sqlite语句，发布给其他节点）
 * @author wuweifeng wrote on 2018/3/15.
 */
public class AddBlockEvent extends ApplicationEvent {
    public AddBlockEvent(Block block) {
        super(block);
    }
}
