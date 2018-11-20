package xin.nbjzj.rehab.socket.handler.server;

import xin.nbjzj.rehab.socket.base.AbstractBlockHandler;
import xin.nbjzj.rehab.socket.body.RpcBlockBody;

import xin.nbjzj.rehab.socket.packet.BlockPacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * 获取全部区块信息的请求，全网广播
 * @author wuweifeng wrote on 2018/3/12.
 */
public class TotalBlockInfoRequestHandler extends AbstractBlockHandler<RpcBlockBody> {
    private Logger logger = LoggerFactory.getLogger(TotalBlockInfoRequestHandler.class);

    @Override
    public Class<RpcBlockBody> bodyClass() {
        return RpcBlockBody.class;
    }

    @Override
    public Object handler(BlockPacket packet, RpcBlockBody rpcBlockBody, ChannelContext channelContext) throws Exception {
        logger.info("收到<请求生成Block的回应>消息", Json.toJson(rpcBlockBody));

        //TODO check合法性
        //TODO response

        return null;
    }
}
