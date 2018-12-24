package xin.nbjzj.rehab.core.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.BlockBody;
import xin.nbjzj.rehab.blockchain.block.BlockHeader;
import xin.nbjzj.rehab.blockchain.block.Instruction;
import xin.nbjzj.rehab.blockchain.block.Operation;
import xin.nbjzj.rehab.blockchain.block.PairKey;
import xin.nbjzj.rehab.blockchain.block.merkle.MerkleTree;
import xin.nbjzj.rehab.blockchain.common.CommonUtil;
import xin.nbjzj.rehab.blockchain.common.Sha256;
import xin.nbjzj.rehab.blockchain.common.TrustSDK;
import xin.nbjzj.rehab.blockchain.common.exception.TrustSDKException;
import xin.nbjzj.rehab.blockchain.manager.DbBlockManager;
import xin.nbjzj.rehab.core.entity.ClinicalInfo;
import xin.nbjzj.rehab.core.entity.RehabApplication;
import xin.nbjzj.rehab.core.entity.User;
import xin.nbjzj.rehab.core.entity.WorkInjuryCertificate;
import xin.nbjzj.rehab.socket.body.RpcBlockBody;
import xin.nbjzj.rehab.socket.client.PacketSender;
import xin.nbjzj.rehab.socket.packet.BlockPacket;
import xin.nbjzj.rehab.socket.packet.PacketBuilder;
import xin.nbjzj.rehab.socket.packet.PacketType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tio.utils.json.Json;

import javax.annotation.Resource;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    private Block addBlock(Block block) {
      
    	
        BlockPacket blockPacket = new PacketBuilder<>().setType(PacketType.GENERATE_BLOCK_REQUEST).setBody(new
                RpcBlockBody(block)).build();

        //广播给其他人做验证
        packetSender.sendGroup(blockPacket);

        return block;
    }
    
    public Block constructBlock(byte operation,User entity,PairKey keys) {
    	
    	//构造instructions
		List<Instruction> instructions = new ArrayList<Instruction>();
		
		Instruction instruction = new Instruction();
		instruction.setJson(Json.toJson(entity));
		instruction.setOperation(operation);
		instruction.setTable("user");
		instruction.setPublicKey(keys.getPublicKey());
		try {
			instruction.setSign(TrustSDK.signString(keys.getPrivateKey(), instruction.toString()));
		} catch (UnsupportedEncodingException | TrustSDKException e) {
			e.printStackTrace();
		}
		instruction.setTimeStamp(CommonUtil.getNow());
		instruction.setHash(Sha256.sha256(instruction.toString()));
		instructions.add(instruction);
		
		List<String> hashList = instructions.stream().map(Instruction::getHash).collect(Collectors
                .toList());
		
		BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHashList(hashList);
        
      //计算所有指令的hashRoot
        blockHeader.setHashMerkleRoot(new MerkleTree(hashList).build().getRoot());
        blockHeader.setPublicKey(keys.getPublicKey());
        blockHeader.setTimeStamp(CommonUtil.getNow());
        blockHeader.setVersion(version);
        blockHeader.setNumber(dbBlockManager.getLastBlockNumber() + 1);
        blockHeader.setHashPreviousBlock(dbBlockManager.getLastBlockHash());
        Block block = new Block();
        BlockBody blockBody = new BlockBody();
        blockBody.setInstructions(instructions);
        block.setBlockBody(blockBody);
        block.setBlockHeader(blockHeader);
        block.setHash(Sha256.sha256(blockHeader.toString() + blockBody.toString()));
        addBlock(block);
    	return block;
    }
    
    
    public Block constructBlock(byte operation,ClinicalInfo entity,PairKey keys) {
    	
    	//构造instructions
		List<Instruction> instructions = new ArrayList<Instruction>();
		
		Instruction instruction = new Instruction();
		instruction.setJson(Json.toJson(entity));
		instruction.setOperation(operation);
		instruction.setTable("clinicalinfo");
		instruction.setPublicKey(keys.getPublicKey());
		try {
			instruction.setSign(TrustSDK.signString(keys.getPrivateKey(), instruction.toString()));
		} catch (UnsupportedEncodingException | TrustSDKException e) {
			e.printStackTrace();
		}
		instruction.setTimeStamp(CommonUtil.getNow());
		instruction.setHash(Sha256.sha256(instruction.toString()));
		instructions.add(instruction);
		
		List<String> hashList = instructions.stream().map(Instruction::getHash).collect(Collectors
                .toList());
		
		BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHashList(hashList);
        
      //计算所有指令的hashRoot
        blockHeader.setHashMerkleRoot(new MerkleTree(hashList).build().getRoot());
        blockHeader.setPublicKey(keys.getPublicKey());
        blockHeader.setTimeStamp(CommonUtil.getNow());
        blockHeader.setVersion(version);
        blockHeader.setNumber(dbBlockManager.getLastBlockNumber() + 1);
        blockHeader.setHashPreviousBlock(dbBlockManager.getLastBlockHash());
        Block block = new Block();
        BlockBody blockBody = new BlockBody();
        blockBody.setInstructions(instructions);
        block.setBlockBody(blockBody);
        block.setBlockHeader(blockHeader);
        block.setHash(Sha256.sha256(blockHeader.toString() + blockBody.toString()));
        addBlock(block);
    	return block;
    }
    
    public Block constructBlock(byte operation,WorkInjuryCertificate entity,PairKey keys) {
    	
    	//构造instructions
		List<Instruction> instructions = new ArrayList<Instruction>();
		
		Instruction instruction = new Instruction();
		instruction.setJson(Json.toJson(entity));
		instruction.setOperation(operation);
		instruction.setTable("workinjurycertificate");
		instruction.setPublicKey(keys.getPublicKey());
		try {
			instruction.setSign(TrustSDK.signString(keys.getPrivateKey(), instruction.toString()));
		} catch (UnsupportedEncodingException | TrustSDKException e) {
			e.printStackTrace();
		}
		instruction.setTimeStamp(CommonUtil.getNow());
		instruction.setHash(Sha256.sha256(instruction.toString()));
		instructions.add(instruction);
		
		List<String> hashList = instructions.stream().map(Instruction::getHash).collect(Collectors
                .toList());
		
		BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHashList(hashList);
        
      //计算所有指令的hashRoot
        blockHeader.setHashMerkleRoot(new MerkleTree(hashList).build().getRoot());
        blockHeader.setPublicKey(keys.getPublicKey());
        blockHeader.setTimeStamp(CommonUtil.getNow());
        blockHeader.setVersion(version);
        blockHeader.setNumber(dbBlockManager.getLastBlockNumber() + 1);
        blockHeader.setHashPreviousBlock(dbBlockManager.getLastBlockHash());
        Block block = new Block();
        BlockBody blockBody = new BlockBody();
        blockBody.setInstructions(instructions);
        block.setBlockBody(blockBody);
        block.setBlockHeader(blockHeader);
        block.setHash(Sha256.sha256(blockHeader.toString() + blockBody.toString()));
        addBlock(block);
    	return block;
    }
    
public Block constructBlock(byte operation,RehabApplication entity,PairKey keys) {
    	
    	//构造instructions
		List<Instruction> instructions = new ArrayList<Instruction>();
		
		Instruction instruction = new Instruction();
		instruction.setJson(Json.toJson(entity));
		instruction.setOperation(operation);
		instruction.setTable("rehabapplication");
		instruction.setPublicKey(keys.getPublicKey());
		try {
			instruction.setSign(TrustSDK.signString(keys.getPrivateKey(), instruction.toString()));
		} catch (UnsupportedEncodingException | TrustSDKException e) {
			e.printStackTrace();
		}
		instruction.setTimeStamp(CommonUtil.getNow());
		instruction.setHash(Sha256.sha256(instruction.toString()));
		instructions.add(instruction);
		
		List<String> hashList = instructions.stream().map(Instruction::getHash).collect(Collectors
                .toList());
		
		BlockHeader blockHeader = new BlockHeader();
        blockHeader.setHashList(hashList);
        
      //计算所有指令的hashRoot
        blockHeader.setHashMerkleRoot(new MerkleTree(hashList).build().getRoot());
        blockHeader.setPublicKey(keys.getPublicKey());
        blockHeader.setTimeStamp(CommonUtil.getNow());
        blockHeader.setVersion(version);
        blockHeader.setNumber(dbBlockManager.getLastBlockNumber() + 1);
        blockHeader.setHashPreviousBlock(dbBlockManager.getLastBlockHash());
        Block block = new Block();
        BlockBody blockBody = new BlockBody();
        blockBody.setInstructions(instructions);
        block.setBlockBody(blockBody);
        block.setBlockHeader(blockHeader);
        block.setHash(Sha256.sha256(blockHeader.toString() + blockBody.toString()));
        addBlock(block);
    	return block;
    }
}
