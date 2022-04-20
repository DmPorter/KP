package queueServer;

import forTask.Task;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ObrOutQueue extends Thread  {
    HashMap<String, QueueOutForThread> waitMap;
    LinkedBlockingQueue<Task> outLinkedQueue;

    public ObrOutQueue(HashMap<String, QueueOutForThread> waitMap, LinkedBlockingQueue<Task> outLinkedQueue){
        this.waitMap = waitMap;
        this.outLinkedQueue = outLinkedQueue;
    }

    /**
     * Обрабатывает заявки из выходной очереди. Если заявка выпонена, то параметр переменной CountDownLatch
     * становится = 0.
     */

    @Override
    public void run() {
        while (true){
            try {
                Task t = outLinkedQueue.take();
                waitMap.get(t.getUid()).setResult(t.getRes());
                waitMap.get(t.getUid()).cdlOut();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
