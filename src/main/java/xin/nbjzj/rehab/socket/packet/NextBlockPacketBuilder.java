package xin.nbjzj.rehab.socket.packet;

import xin.nbjzj.rehab.ApplicationContextProvider;
import xin.nbjzj.rehab.socket.body.RpcSimpleBlockBody;

import xin.nbjzj.rehab.blockchain.event.ClientRequestEvent;
import xin.nbjzj.rehab.blockchain.manager.DbBlockManager;

/**
 * 构建向别的节点请求next block的builder.带着自己最后一个block的hash
 * @author wuweifeng wrote on 2018/3/20.
 */
public class NextBlockPacketBuilder {
    public static BlockPacket build() {
        return build(null);
    }

    public static BlockPacket build(String responseId) {
        String hash = ApplicationContextProvider.getBean(DbBlockManager.class).getLastBlockHash();

        RpcSimpleBlockBody rpcBlockBody = new RpcSimpleBlockBody(hash);
        rpcBlockBody.setResponseMsgId(responseId);
        BlockPacket blockPacket = new PacketBuilder<>().setType(PacketType.NEXT_BLOCK_INFO_REQUEST).setBody
                (rpcBlockBody).build();
        //发布client请求事件
        ApplicationContextProvider.publishEvent(new ClientRequestEvent(blockPacket));
        return blockPacket;
    }

}
