package xin.nbjzj.rehab.socket.pbft.listener;

import xin.nbjzj.rehab.socket.body.VoteBody;
import xin.nbjzj.rehab.socket.client.PacketSender;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.PacketBuilder;
import xin.nbjzj.rehab.socket.packet.PacketType;

import xin.nbjzj.rehab.socket.pbft.event.MsgCommitEvent;
import xin.nbjzj.rehab.socket.pbft.msg.VoteMsg;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 监听block可以commit消息
 * @author wuweifeng wrote on 2018/4/25.
 */
@Component
public class CommitEventListener {
    @Resource
    private PacketSender packetSender;

    /**
     * block已经开始进入commit状态，广播消息
     *
     * @param msgCommitEvent
     *         msgCommitEvent
     */
    @EventListener
    public void msgIsCommit(MsgCommitEvent msgCommitEvent) {
        VoteMsg voteMsg = (VoteMsg) msgCommitEvent.getSource();

        //群发消息，通知所有节点，我已对该Block Prepare
        BlockPacket blockPacket = new PacketBuilder<>().setType(PacketType.PBFT_VOTE).setBody(new
                VoteBody(voteMsg)).build();

        //广播给所有人我已commit
        packetSender.sendGroup(blockPacket);
    }
}
