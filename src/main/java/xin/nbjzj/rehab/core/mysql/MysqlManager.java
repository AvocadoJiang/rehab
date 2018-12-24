package xin.nbjzj.rehab.core.mysql;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tio.utils.json.Json;


import xin.nbjzj.rehab.ApplicationContextProvider;
import xin.nbjzj.rehab.blockchain.block.Block;
import xin.nbjzj.rehab.blockchain.block.Instruction;
import xin.nbjzj.rehab.blockchain.block.Operation;
import xin.nbjzj.rehab.blockchain.event.DbSyncEvent;
import xin.nbjzj.rehab.blockchain.manager.DbBlockManager;
import xin.nbjzj.rehab.core.entity.WorkInjuryCertificate;
import xin.nbjzj.rehab.core.entity.ClinicalInfo;
import xin.nbjzj.rehab.core.entity.RehabApplication;
import xin.nbjzj.rehab.core.entity.User;
import xin.nbjzj.rehab.core.mysql.entity.Sync;
import xin.nbjzj.rehab.core.mysql.service.SyncRepository;
import xin.nbjzj.rehab.core.service.ClinicalInfoRepository;
import xin.nbjzj.rehab.core.service.RehabApplicationRepository;
import xin.nbjzj.rehab.core.service.UserRepository;
import xin.nbjzj.rehab.core.service.WorkInjuryCertificateRepository;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

/**
 * 对mysql数据库的操作（监听新增区块请求，执行对应的sql命令）
 *
 * @author wuweifeng wrote on 2018/3/15.
 */
@Component
public class MysqlManager {
    @Resource
    private UserRepository userRepository;
    @Resource
    private ClinicalInfoRepository clinicalInfoRepository;
    @Resource
    private WorkInjuryCertificateRepository workInjuryCertificateRepository;
    @Resource
    private RehabApplicationRepository rehabApplicationRepository;
    @Resource
    private SyncRepository syncRepository;
    @Resource
    private DbBlockManager dbBlockManager;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * sqlite同步，监听该事件后，去check当前已经同步到哪个区块了，然后继续执行之后的区块
     */
    @EventListener(DbSyncEvent.class)
    public void dbSync() {
        logger.info("开始执行导入区块到mysql操作");
        //查看同步到哪个区块了
        Sync syncEntity = syncRepository.findTopByOrderBySyncIDDesc();

        Block block;
        if (syncEntity == null) {
            //从第一个开始
            block = dbBlockManager.getFirstBlock();
            logger.info("正在导入第一个区块，hash为：" + block.getHash());
        } else {
            Block lastBlock = dbBlockManager.getLastBlock();
            //已经同步到最后一块了
            if (lastBlock.getHash().equals(syncEntity.getHash())) {
                logger.info("导入完毕");
                return;
            }
            logger.info("正在导入区块，hash为：" + lastBlock.getHash());
            String hash = syncEntity.getHash();
            block = dbBlockManager.getNextBlock(dbBlockManager.getBlockByHash(hash));
        }
       
        execute(block);
        ApplicationContextProvider.publishEvent(new DbSyncEvent(""));
    }

    /**
     * 根据一个block执行sql
     * 整个block一个事务
     * 
     * @param block
     *         block
     */
    @Transactional(rollbackFor = Exception.class)
    public void execute(Block block) {
        List<Instruction> instructions = block.getBlockBody().getInstructions();

        doSqlParse(instructions);

        //保存已同步的进度
        Sync sync = new Sync();
        sync.setHash(block.getHash());
        syncRepository.save(sync);
    }

    /**
     * 执行回滚一个block
     * @param <T>
     *
     * @param block
     *         block
     */
//    public void rollBack(Block block) {
//        List<Instruction> instructions = block.getBlockBody().getInstructions();
//        int size = instructions.size();
//        //需要对语句集合进行反转，然后执行和execute一样的操作
//        List<InstructionReverse> instructionReverses = new ArrayList<>(size);
//        for (int i = size - 1; i >= 0; i--) {
//            instructionReverses.add(instructionService.buildReverse(instructions.get(i)));
//        }
//        doSqlParse(instructionReverses);
//    }

    private void doSqlParse(List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            if("user".equals(instruction.getTable())) {
            	User entity = Json.toBean(instruction.getJson(),User.class);
        		
            	switch (instruction.getOperation()) {
				case Operation.ADD:
					
					userRepository.save(entity);
					break;
				case Operation.UPDATE:
					userRepository.save(entity);
					break;
				case Operation.DELETE:
					userRepository.delete(entity);
					break;
				default:
					break;
				}
            	
            }else if("clinicalinfo".equals(instruction.getTable())) {
            	ClinicalInfo entity = Json.toBean(instruction.getJson(),ClinicalInfo.class);
        		
            	switch (instruction.getOperation()) {
				case Operation.ADD:
					
					clinicalInfoRepository.save(entity);
					break;
				case Operation.UPDATE:
					clinicalInfoRepository.save(entity);
					break;
				case Operation.DELETE:
					clinicalInfoRepository.delete(entity);
					break;
				default:
					break;
				}
            }else if("workinjurycertificate".equals(instruction.getTable())) {
            	WorkInjuryCertificate entity = Json.toBean(instruction.getJson(),WorkInjuryCertificate.class);
        		
            	switch (instruction.getOperation()) {
				case Operation.ADD:
					entity.setClinicalInfo(clinicalInfoRepository.findById(entity.getClinicalInfoID()).get());
					workInjuryCertificateRepository.save(entity);
					break;
				case Operation.UPDATE:
					workInjuryCertificateRepository.save(entity);
					break;
				case Operation.DELETE:
					workInjuryCertificateRepository.delete(entity);
					break;
				default:
					break;
				}
            }else if("rehabapplication".equals(instruction.getTable())) {
            	RehabApplication entity = Json.toBean(instruction.getJson(),RehabApplication.class);
        		
            	switch (instruction.getOperation()) {
				case Operation.ADD:
					rehabApplicationRepository.save(entity);
					break;
				case Operation.UPDATE:
					rehabApplicationRepository.save(entity);
					break;
				case Operation.DELETE:
					rehabApplicationRepository.delete(entity);
					break;
				default:
					break;
				}
            }
        }
    }
    
    /**
     * 测试block的代码是否能正确执行
     * 
     * @param block block
     */
    @Transactional(rollbackFor = Exception.class)
    public void tryExecute(Block block) {
    	execute(block);
    }
    
}
