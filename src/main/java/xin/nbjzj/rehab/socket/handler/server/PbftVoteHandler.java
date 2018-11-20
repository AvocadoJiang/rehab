package xin.nbjzj.rehab.socket.handler.server;

import xin.nbjzj.rehab.ApplicationContextProvider;
import xin.nbjzj.rehab.socket.base.AbstractBlockHandler;
import xin.nbjzj.rehab.socket.body.VoteBody;

import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.pbft.msg.VoteMsg;
import xin.nbjzj.rehab.socket.pbft.queue.MsgQueueManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;

/**
 * pbft投票处理
 *
 * @author wuweifeng wrote on 2018/3/12.
 */
public class PbftVoteHandler extends AbstractBlockHandler<VoteBody> {
    private Logger logger = LoggerFactory.getLogger(PbftVoteHandler.class);


    @Override
    public Class<VoteBody> bodyClass() {
        return VoteBody.class;
    }

    @Override
    public Object handler(BlockPacket packet, VoteBody voteBody, ChannelContext channelContext) {
        VoteMsg voteMsg = voteBody.getVoteMsg();
        logger.info("收到来自于<" + voteMsg.getAppId() + "><投票>消息，投票信息为[" + voteMsg + "]");

        ApplicationContextProvider.getBean(MsgQueueManager.class).pushMsg(voteMsg);
        return null;
    }
}
