package xin.nbjzj.rehab.socket.handler.client;

import xin.nbjzj.rehab.ApplicationContextProvider;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.check.CheckerManager;
import xin.nbjzj.rehab.socket.base.AbstractBlockHandler;
import xin.nbjzj.rehab.socket.body.RpcBlockBody;
import xin.nbjzj.rehab.socket.body.RpcCheckBlockBody;
import xin.nbjzj.rehab.socket.client.PacketSender;

import xin.nbjzj.rehab.blockchain.event.AddBlockEvent;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.NextBlockPacketBuilder;
import xin.nbjzj.rehab.socket.pbft.queue.NextBlockQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * 对方根据我们传的hash，给我们返回的block
 *
 * @author wuweifeng wrote on 2018/3/16.
 */
public class FetchBlockResponseHandler extends AbstractBlockHandler<RpcBlockBody> {
    private Logger logger = LoggerFactory.getLogger(TotalBlockInfoResponseHandler.class);

    @Override
    public Class<RpcBlockBody> bodyClass() {
        return RpcBlockBody.class;
    }

    @Override
    public Object handler(BlockPacket packet, RpcBlockBody rpcBlockBody, ChannelContext channelContext) {
        logger.info("收到来自于<" + rpcBlockBody.getAppId() + ">的回复，Block为：" + Json.toJson(rpcBlockBody));

        Block block = rpcBlockBody.getBlock();
        //如果为null，说明对方也没有该Block
        if (block == null) {
            logger.info("对方也没有该Block");
        } else {
            //此处校验传过来的block的合法性，如果合法，则更新到本地，作为next区块
        	if(ApplicationContextProvider.getBean(NextBlockQueue.class).pop(block.getHash()) == null) return null;
        	
            CheckerManager checkerManager = ApplicationContextProvider.getBean(CheckerManager.class);
            RpcCheckBlockBody rpcCheckBlockBody = checkerManager.check(block);
            //校验通过，则存入本地DB，保存新区块
            if (rpcCheckBlockBody.getCode() == 0) {
                ApplicationContextProvider.publishEvent(new AddBlockEvent(block));
                //继续请求下一块
                BlockPacket blockPacket = NextBlockPacketBuilder.build();
                ApplicationContextProvider.getBean(PacketSender.class).sendGroup(blockPacket);
            }
        }

        return null;
    }
}
