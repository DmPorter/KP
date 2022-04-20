package queueServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueOutForThread {
    private CountDownLatch cdl;
    private AtomicInteger result;
    public QueueOutForThread(){
        cdl = new CountDownLatch(1);
    }

    public AtomicInteger getResult() {
        return result;
    }

    public void cdlOut(){
        cdl.countDown();
    }
    public void setResult(int result) {
        this.result = new AtomicInteger(result);
    }

    public void waitQueue() throws InterruptedException {
        cdl.await();
    }
}
