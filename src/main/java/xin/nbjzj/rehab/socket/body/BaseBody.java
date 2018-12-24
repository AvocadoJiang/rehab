package xin.nbjzj.rehab.socket.body;

import lombok.Data;
import xin.nbjzj.rehab.blockchain.common.AppId;
import xin.nbjzj.rehab.blockchain.common.CommonUtil;

/**
 *
 * @author tanyaowu
 * 2017年3月27日 上午12:12:17
 */
@Data
public class BaseBody {

	/**
	 * 消息发送时间
	 */
	private Long time = System.currentTimeMillis();
    /**
     * 每条消息的唯一id
     */
	private String messageId = CommonUtil.generateUuid();
    /**
     * 回复的哪条消息
     */
	private String responseMsgId;
    /**
     * 自己是谁
     */
	private String appId = AppId.value;


}
