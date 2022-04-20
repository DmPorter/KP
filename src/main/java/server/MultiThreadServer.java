package server;

import agentsServer.MultiThreadAgent;
import forTask.Task;
import queueServer.ObrOutQueue;
import queueServer.QueueOutForThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);
    static LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<>();
    static LinkedBlockingQueue<Task> queueOut = new LinkedBlockingQueue<>();
    static HashMap<String, QueueOutForThread> result = new HashMap<>();

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(8011);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created, command console reader for listen to server commands");
            ObrOutQueue obrOutQueue = new ObrOutQueue(result, queueOut);
            System.out.println(server.getLocalSocketAddress());
            obrOutQueue.start();
//            Worker w1 = new Worker(queue,queueOut, 4);
//            Worker w2 = new Worker(queue,queueOut, 1);
//            w1.start();
//            w2.start();
            MultiThreadAgent ag = new MultiThreadAgent(queue, queueOut);
            ag.start();
            while (!server.isClosed()) {
                if (br.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        break;
                    }
                }
                Socket client = server.accept();
                executeIt.execute(new MonoThreadClientHandler(client, queue, queueOut, result));
                System.out.print("Connection accepted.");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
