package queueServer;

import forTask.Task;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ObrOutQueue extends Thread  {
    HashMap<String, QueueOutForThread> result;
    LinkedBlockingQueue<Task> queueOut;

    public ObrOutQueue(HashMap<String, QueueOutForThread> result, LinkedBlockingQueue<Task> queueOut){
        this.result = result;
        this.queueOut = queueOut;
    }

    @Override
    public void run() {
        while (true){
            try {
                Task t = queueOut.take();
                result.get(t.getUid()).setResult(t.getRes());
                result.get(t.getUid()).cdlOut();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
