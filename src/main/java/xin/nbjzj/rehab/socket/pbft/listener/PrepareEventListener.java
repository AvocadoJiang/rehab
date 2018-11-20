package xin.nbjzj.rehab.socket.pbft.listener;

import javax.annotation.Resource;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import xin.nbjzj.rehab.socket.body.VoteBody;
import xin.nbjzj.rehab.socket.client.PacketSender;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.PacketBuilder;
import xin.nbjzj.rehab.socket.packet.PacketType;

import xin.nbjzj.rehab.socket.pbft.event.MsgPrepareEvent;
import xin.nbjzj.rehab.socket.pbft.msg.VoteMsg;

/**
 * @author wuweifeng wrote on 2018/4/25.
 */
@Component
public class PrepareEventListener {
    @Resource
    private PacketSender packetSender;

    /**
     * block已经开始进入Prepare状态
     *
     * @param msgPrepareEvent
     *         msgIsPrepareEvent
     */
    @EventListener
    public void msgIsPrepare(MsgPrepareEvent msgPrepareEvent) {
        VoteMsg voteMsg = (VoteMsg) msgPrepareEvent.getSource();

        //群发消息，通知别的节点，我已对该Block Prepare
        BlockPacket blockPacket = new PacketBuilder<>().setType(PacketType.PBFT_VOTE).setBody(new
                VoteBody(voteMsg)).build();

        //广播给所有人我已Prepare
        packetSender.sendGroup(blockPacket);
    }
}
