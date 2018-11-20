package xin.nbjzj.rehab.blockchain.service;

import xin.nbjzj.rehab.blockchain.block.PairKey;
import xin.nbjzj.rehab.blockchain.common.TrustSDK;
import xin.nbjzj.rehab.blockchain.common.exception.TrustSDKException;
import org.springframework.stereotype.Service;

/**
 * @author wuweifeng wrote on 2018/3/7.
 */
@Service
public class PairKeyService {

    /**
     * 生成公私钥对
     * @return PairKey
     * @throws TrustSDKException TrustSDKException
     */
    public PairKey generate() throws TrustSDKException {
        return TrustSDK.generatePairKey(true);
    }
}
