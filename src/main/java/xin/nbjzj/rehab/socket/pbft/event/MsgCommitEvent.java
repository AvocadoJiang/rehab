package xin.nbjzj.rehab.socket.pbft.event;

import org.springframework.context.ApplicationEvent;

import xin.nbjzj.rehab.socket.pbft.msg.VoteMsg;

/**
 * 消息已被验证，进入到Commit集合中
 * @author wuweifeng wrote on 2018/4/25.
 */
public class MsgCommitEvent extends ApplicationEvent {
    public MsgCommitEvent(VoteMsg source) {
        super(source);
    }
}
