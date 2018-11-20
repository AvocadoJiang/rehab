package xin.nbjzj.rehab.socket.distruptor;

import com.lmax.disruptor.EventHandler;
import xin.nbjzj.rehab.ApplicationContextProvider;

import xin.nbjzj.rehab.socket.distruptor.base.BaseEvent;

/**
 * @author wuweifeng wrote on 2018/4/20.
 */
public class DisruptorClientHandler implements EventHandler<BaseEvent> {

    @Override
    public void onEvent(BaseEvent baseEvent, long sequence, boolean endOfBatch) throws Exception {
        ApplicationContextProvider.getBean(DisruptorClientConsumer.class).receive(baseEvent);
    }
}
