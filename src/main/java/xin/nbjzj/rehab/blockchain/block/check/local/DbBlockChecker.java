package xin.nbjzj.rehab.blockchain.block.check.local;

import cn.hutool.core.util.StrUtil;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.check.BlockChecker;
import xin.nbjzj.rehab.blockchain.common.Sha256;
import xin.nbjzj.rehab.blockchain.common.exception.TrustSDKException;
import xin.nbjzj.rehab.blockchain.manager.DbBlockManager;
import xin.nbjzj.rehab.core.service.BlockService;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 使用本地存储的权限、Block信息对新来的block进行校验
 * @author wuweifeng wrote on 2018/3/13.
 */
@Component
public class DbBlockChecker implements BlockChecker {
    @Resource
    private DbBlockManager dbBlockManager;
  
    @Resource
    private BlockService blockService;

    @Override
    public int checkNum(Block block) {
        Block localBlock = getLastBlock();
        int localNum = 0;
        if (localBlock != null) {
            localNum = localBlock.getBlockHeader().getNumber();
        }
        //本地区块+1等于新来的区块时才同意
        if (localNum + 1 == block.getBlockHeader().getNumber()) {
            //同意生成区块
            return 0;
        }

        //拒绝
        return -1;
    }

  

    @Override
    public int checkHash(Block block) {
        Block localLast = getLastBlock();
        //创世块可以，或者新块的prev等于本地的last hash也可以
        if (localLast == null && block.getBlockHeader().getHashPreviousBlock() == null) {
            return 0;
        }
        if (localLast != null && StrUtil.equals(localLast.getHash(), block.getBlockHeader().getHashPreviousBlock())) {
            return 0;
        }
        return -1;
    }

    @Override
    public int checkTime(Block block) {
        Block localBlock = getLastBlock();
        //新区块的生成时间比本地的还小
        if (localBlock != null && block.getBlockHeader().getTimeStamp() <= localBlock.getBlockHeader().getTimeStamp()) {
            //拒绝
            return -1;
        }
        return 0;
    }
    
    @Override
    public int checkSign(Block block) {
    	if(!checkBlockHashSign(block)) {
    		return -1;
    	}
    	return 0;
    }

    private Block getLastBlock() {
        return dbBlockManager.getLastBlock();
    }
    
    public String checkBlock(Block block) {
    	if(!checkBlockHashSign(block)) return block.getHash();
    	
    	String preHash = block.getBlockHeader().getHashPreviousBlock();
    	if(preHash == null) return null;
    	
    	Block preBlock = dbBlockManager.getBlockByHash(preHash);
    	if(preBlock == null) return block.getHash();
    	
		int localNum = preBlock.getBlockHeader().getNumber();
        //当前区块+1等于下一个区块时才同意
        if (localNum + 1 != block.getBlockHeader().getNumber()) {
            return block.getHash();
        }
        if(block.getBlockHeader().getTimeStamp() <= preBlock.getBlockHeader().getTimeStamp()) {
        	return block.getHash();
        }
    	
    		
    	return null;
    }


    /**
     * 检测区块签名及hash是否符合
     * @param block
     * @return
     */
	private boolean checkBlockHashSign(Block block) {
//		BlockRequestBody blockRequestBody = new BlockRequestBody();
//		blockRequestBody.setBlockBody(block.getBlockBody());
//		blockRequestBody.setPublicKey(block.getBlockHeader().getPublicKey());
//		try {
//			if(blockService.check(blockRequestBody) != null) return false;
//		} catch (TrustSDKException e) {
//			return false;
//		}
//		
//		String hash = Sha256.sha256(block.getBlockHeader().toString() + block.getBlockBody().toString());
//		if(!StrUtil.equals(block.getHash(),hash)) return false;
		
		return true;
	}

    
}
