package xin.nbjzj.rehab.socket.handler.server;

import xin.nbjzj.rehab.ApplicationContextProvider;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.check.CheckerManager;
import xin.nbjzj.rehab.socket.base.AbstractBlockHandler;
import xin.nbjzj.rehab.socket.body.RpcBlockBody;
import xin.nbjzj.rehab.socket.body.RpcCheckBlockBody;

import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.pbft.VoteType;
import xin.nbjzj.rehab.socket.pbft.msg.VotePreMsg;
import xin.nbjzj.rehab.socket.pbft.queue.MsgQueueManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;

/**
 * 收到请求生成区块消息，进入PrePre队列
 *
 * @author wuweifeng wrote on 2018/3/12.
 */
public class GenerateBlockRequestHandler extends AbstractBlockHandler<RpcBlockBody> {
    private Logger logger = LoggerFactory.getLogger(GenerateBlockRequestHandler.class);


    @Override
    public Class<RpcBlockBody> bodyClass() {
        return RpcBlockBody.class;
    }

    @Override
    public Object handler(BlockPacket packet, RpcBlockBody rpcBlockBody, ChannelContext channelContext) {
        Block block = rpcBlockBody.getBlock();
        logger.info("收到来自于<" + rpcBlockBody.getAppId() + "><请求生成Block>消息，block信息为[" + block + "]");

        CheckerManager checkerManager = ApplicationContextProvider.getBean(CheckerManager.class);
        //对区块的基本信息进行校验，校验通过后进入pbft的Pre队列
        RpcCheckBlockBody rpcCheckBlockBody = checkerManager.check(block);
        logger.info("校验结果:" + rpcCheckBlockBody.toString());
        if (rpcCheckBlockBody.getCode() == 0) {
            VotePreMsg votePreMsg = new VotePreMsg();
            votePreMsg.setBlock(block);
            votePreMsg.setVoteType(VoteType.PREPREPARE);
            votePreMsg.setNumber(block.getBlockHeader().getNumber());
            votePreMsg.setAppId(rpcBlockBody.getAppId());
            votePreMsg.setHash(block.getHash());
            votePreMsg.setAgree(true);
            //将消息推入PrePrepare队列
            ApplicationContextProvider.getBean(MsgQueueManager.class).pushMsg(votePreMsg);
        }

        return null;
    }
}
