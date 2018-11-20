package xin.nbjzj.rehab.socket.distruptor;

import cn.hutool.core.util.StrUtil;
import xin.nbjzj.rehab.socket.distruptor.base.BaseEvent;
import xin.nbjzj.rehab.socket.distruptor.base.MessageConsumer;
import xin.nbjzj.rehab.socket.handler.client.FetchBlockResponseHandler;
import xin.nbjzj.rehab.socket.handler.client.NextBlockResponseHandler;
import xin.nbjzj.rehab.socket.handler.client.TotalBlockInfoResponseHandler;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.PacketType;
import xin.nbjzj.rehab.blockchain.common.AppId;
import xin.nbjzj.rehab.socket.base.AbstractBlockHandler;
import xin.nbjzj.rehab.socket.body.BaseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.utils.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有server发来的消息都在这里处理
 * @author wuweifeng wrote on 2018/4/20.
 */
@Component
public class DisruptorClientConsumer implements MessageConsumer {
    private static Map<Byte, AbstractBlockHandler<?>> handlerMap = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(getClass());

    static {
        handlerMap.put(PacketType.TOTAL_BLOCK_INFO_RESPONSE, new TotalBlockInfoResponseHandler());
        handlerMap.put(PacketType.NEXT_BLOCK_INFO_RESPONSE, new NextBlockResponseHandler());
        handlerMap.put(PacketType.FETCH_BLOCK_INFO_RESPONSE, new FetchBlockResponseHandler());
    }

    @Override
    public void receive(BaseEvent baseEvent) throws Exception {
        BlockPacket blockPacket = baseEvent.getBlockPacket();
        Byte type = blockPacket.getType();
        AbstractBlockHandler<?> blockHandler = handlerMap.get(type);
        if (blockHandler == null) {
            return;
        }

        //消费消息
        BaseBody baseBody = Json.toBean(new String(blockPacket.getBody()), BaseBody.class);
        //logger.info("收到来自于<" + baseBody.getAppId() + ">针对msg<" + baseBody.getResponseMsgId() + ">的回应");

        String appId = baseBody.getAppId();
        if (StrUtil.equals(AppId.value, appId)) {
            //是本机
            //return;
        }

        blockHandler.handler(blockPacket, baseEvent.getChannelContext());
    }
}
