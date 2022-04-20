package agentsServer;

import forTask.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadAgent extends Thread{
    static ExecutorService executeIt = Executors.newFixedThreadPool(5);
    static LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<>();
    static LinkedBlockingQueue<Task> queueOut = new LinkedBlockingQueue<>();

    public MultiThreadAgent(LinkedBlockingQueue<Task> in, LinkedBlockingQueue<Task> out){
        queue = in;
        queueOut = out;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(8010)) {
            while(!server.isClosed()){
                System.out.println(server.getLocalSocketAddress());
                Socket agent = server.accept();
                executeIt.execute(new MonoThreadAgent(agent, queue, queueOut));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executeIt.shutdown();
    }
}
