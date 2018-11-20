package xin.nbjzj.rehab.socket.distruptor.base;

/**
 * @author wuweifeng wrote on 2018/4/20.
 */
public interface MessageConsumer {
    void receive(BaseEvent baseEvent) throws Exception;
}
