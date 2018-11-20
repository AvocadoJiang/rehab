package xin.nbjzj.rehab.socket.pbft.queue;

import cn.hutool.core.bean.BeanUtil;
import xin.nbjzj.rehab.blockchain.event.AddBlockEvent;
import xin.nbjzj.rehab.socket.pbft.event.MsgPrepareEvent;
import xin.nbjzj.rehab.socket.pbft.msg.VoteMsg;
import xin.nbjzj.rehab.socket.pbft.msg.VotePreMsg;

import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.common.AppId;
import xin.nbjzj.rehab.blockchain.common.timer.TimerManager;

import xin.nbjzj.rehab.socket.pbft.VoteType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * preprepare消息的存储，但凡收到请求生成Block的信息，都在这里处理
 *
 * @author wuweifeng wrote on 2018/4/23.
 */
@Component
public class PreMsgQueue extends BaseMsgQueue {
    
    @Resource
    private PrepareMsgQueue prepareMsgQueue;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    private ConcurrentHashMap<String, VotePreMsg> blockConcurrentHashMap = new ConcurrentHashMap<>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void push(VoteMsg voteMsg) {
        //该队列里的是votePreMsg
        VotePreMsg votePreMsg = (VotePreMsg) voteMsg;
        String hash = votePreMsg.getHash();
        //避免收到重复消息
        if (blockConcurrentHashMap.get(hash) != null) {
            return;
        }
        //但凡是能进到该push方法的，都是通过基本校验的，但在并发情况下可能会相同number的block都进到投票队列中
        //需要对新进来的Vote信息的number进行校验，如果在比prepre阶段靠后的阶段中，已经出现了认证OK的同number的vote，则拒绝进入该队列
        if (prepareMsgQueue.otherConfirm(hash, voteMsg.getNumber())) {
            logger.info("拒绝进入Prepare阶段，hash为" + hash);
            return;
        }
        // 检测脚本是否正常
        try {
//            sqliteManager.tryExecute(votePreMsg.getBlock());
        } catch (Exception e) {
            // 执行异常
            logger.info("sql指令预执行失败");
            return;
        }

        //存入Pre集合中
        blockConcurrentHashMap.put(hash, votePreMsg);

        //加入Prepare行列，推送给所有人
        VoteMsg prepareMsg = new VoteMsg();
        BeanUtil.copyProperties(voteMsg, prepareMsg);
        prepareMsg.setVoteType(VoteType.PREPARE);
        prepareMsg.setAppId(AppId.value);
        eventPublisher.publishEvent(new MsgPrepareEvent(prepareMsg));
    }

    /**
     * 根据hash，得到内存中的Block信息
     *
     * @param hash
     *         hash
     * @return Block
     */
    public Block findByHash(String hash) {
        VotePreMsg votePreMsg = blockConcurrentHashMap.get(hash);
        if (votePreMsg != null) {
            return votePreMsg.getBlock();
        }
        return null;
    }

    /**
     * 新区块生成后，clear掉map中number比区块小的所有数据
     */
    @Order(3)
    @EventListener(AddBlockEvent.class)
    public void blockGenerated(AddBlockEvent addBlockEvent) {
        Block block = (Block) addBlockEvent.getSource();
        int number = block.getBlockHeader().getNumber();
        TimerManager.schedule(() -> {
            for (String key : blockConcurrentHashMap.keySet()) {
                if (blockConcurrentHashMap.get(key).getNumber() <= number) {
                    blockConcurrentHashMap.remove(key);
                }
            }
            return null;
        }, 2000);
    }
}
