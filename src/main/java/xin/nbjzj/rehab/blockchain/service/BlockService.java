package xin.nbjzj.rehab.blockchain.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.BlockHeader;
import xin.nbjzj.rehab.blockchain.block.Instruction;
import xin.nbjzj.rehab.blockchain.block.merkle.MerkleTree;
import xin.nbjzj.rehab.blockchain.common.CommonUtil;
import xin.nbjzj.rehab.blockchain.common.Sha256;
import xin.nbjzj.rehab.blockchain.common.exception.TrustSDKException;
import xin.nbjzj.rehab.blockchain.manager.DbBlockManager;
import xin.nbjzj.rehab.socket.body.RpcBlockBody;
import xin.nbjzj.rehab.socket.client.PacketSender;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.PacketBuilder;
import xin.nbjzj.rehab.socket.packet.PacketType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuweifeng wrote on 2018/3/8.
 */
@Service
public class BlockService {
    
    @Value("${version}")
    private int version;
    @Resource
    private PacketSender packetSender;
    @Resource
    private DbBlockManager dbBlockManager;
    
   

    /**
     * 添加新的区块
     * @param blockRequestBody blockRequestBody
     * @return Block
     */
    public Block addBlock(Block block) {
      

        BlockPacket blockPacket = new PacketBuilder<>().setType(PacketType.GENERATE_BLOCK_REQUEST).setBody(new
                RpcBlockBody(block)).build();

        //广播给其他人做验证
        packetSender.sendGroup(blockPacket);

        return block;
    }

}
