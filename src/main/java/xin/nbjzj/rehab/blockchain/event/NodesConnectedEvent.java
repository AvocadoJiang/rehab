package xin.nbjzj.rehab.blockchain.event;

import org.springframework.context.ApplicationEvent;
import org.tio.core.ChannelContext;

/**
 * 节点连接完成时会触发该Event
 * @author andylo25 wrote on 2018/6/15.
 */
public class NodesConnectedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 526755692642414178L;

	public NodesConnectedEvent(ChannelContext channelContext) {
        super(channelContext);
    }
	
	public ChannelContext getSource() {
        return (ChannelContext) source;
    }
	
}
