package server;

import agentsServer.MultiThreadAgent;
import forTask.Task;
import queueServer.ObrOutQueue;
import queueServer.QueueOutForThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MultiThreadServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(10);
    static LinkedBlockingQueue<Task> inLinkedQueue = new LinkedBlockingQueue<>();
    static LinkedBlockingQueue<Task> outLinkedQueue = new LinkedBlockingQueue<>();
    static HashMap<String, QueueOutForThread> waitMap = new HashMap<>();

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(8011);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server started " + server.getInetAddress());
            ObrOutQueue obrOutQueue = new ObrOutQueue(waitMap, outLinkedQueue);
            obrOutQueue.start();

            MultiThreadAgent ag = new MultiThreadAgent(inLinkedQueue, outLinkedQueue);
            ag.start();

            while (!server.isClosed()) {
                if (bufferedReader.ready()) {
                    String serverCommand = bufferedReader.readLine();
                    if (serverCommand.equalsIgnoreCase("quit")) {

                        server.close();
                        break;
                    }
                }
                Socket client = server.accept();
                executeIt.execute(new MonoThreadClientHandler(client, inLinkedQueue, waitMap));
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
