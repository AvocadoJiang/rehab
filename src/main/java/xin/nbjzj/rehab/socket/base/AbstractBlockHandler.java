package xin.nbjzj.rehab.socket.base;

import xin.nbjzj.rehab.socket.body.BaseBody;
import xin.nbjzj.rehab.socket.common.Const;
import xin.nbjzj.rehab.socket.packet.BlockPacket;

import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * 基础handler
 * @author tanyaowu
 * 2017年3月27日 下午9:56:16
 */
public abstract class AbstractBlockHandler<T extends BaseBody> implements HandlerInterface {

	public AbstractBlockHandler() {
		
	}

	public abstract Class<T> bodyClass();

	@Override
	public Object handler(BlockPacket packet, ChannelContext channelContext) throws Exception {
		String jsonStr;
		T bsBody = null;
		if (packet.getBody() != null) {
			jsonStr = new String(packet.getBody(), Const.CHARSET);
			bsBody = Json.toBean(jsonStr, bodyClass());
		}

		return handler(packet, bsBody, channelContext);
	}

	/**
	 * 实际的handler处理
	 * @param packet packet
	 * @param bsBody 解析后的对象
	 * @param channelContext channelContext
	 * @return 用不上
	 * @throws Exception Exception
	 */
	public abstract Object handler(BlockPacket packet, T bsBody, ChannelContext channelContext) throws Exception;

}
