package queueServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс QueueOutForThread представляет собой структуру данных для wait-группы
 */
public class QueueOutForThread {
    /** Переменная CountDownLatch, показывающая выполнено ли задание*/
    private final CountDownLatch cdl;
    /** Результат выполнения задания */
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

    /**
     * Функция включает ожидание до тех пор cdl != 0
     * @throws InterruptedException
     */
    public void waitQueue() throws InterruptedException {
        cdl.await();
    }
}
