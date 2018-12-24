package xin.nbjzj.rehab.socket.distruptor.base;

import xin.nbjzj.rehab.socket.packet.BlockPacket;
import org.tio.core.ChannelContext;

import lombok.Data;

import java.io.Serializable;

/**
 * 生产、消费者之间传递消息用的event
 *
 * @author wuweifeng wrote on 2018/4/20.
 */
@Data
public class BaseEvent implements Serializable {
    private BlockPacket blockPacket;
    private ChannelContext channelContext;

    public BaseEvent(BlockPacket blockPacket, ChannelContext channelContext) {
        this.blockPacket = blockPacket;
        this.channelContext = channelContext;
    }

    public BaseEvent(BlockPacket blockPacket) {
        this.blockPacket = blockPacket;
    }

    public BaseEvent() {
    }
}
