package server;
import forTask.Task;
import queueServer.QueueOutForThread;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MonoThreadClientHandler implements Runnable {

        private static Socket clientDialog;
        private LinkedBlockingQueue<Task> queue;
        private LinkedBlockingQueue<Task> out;
        private HashMap<String, QueueOutForThread> outWait;

        public MonoThreadClientHandler(Socket client, LinkedBlockingQueue<Task> q,LinkedBlockingQueue<Task> out,HashMap<String, QueueOutForThread> outWait) {
            MonoThreadClientHandler.clientDialog = client;
            this.queue = q;
            this.out = out;
            this.outWait = outWait;

        }

    @Override
        public void run() {

            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
                while (!clientDialog.isClosed()) {
                    String entry = in.readLine();
                    System.out.println(clientDialog.getLocalSocketAddress());
                    int[] numArr = Arrays.stream(entry.split(" ")).mapToInt(Integer::parseInt).toArray();
                    Task t = new Task(numArr[1], numArr[2], numArr[0]);
                    long start = System.nanoTime();
                    queue.put(t);
                    QueueOutForThread task = new QueueOutForThread();
                    outWait.put(t.getUid(), task);
                    task.waitQueue();
                    outWait.remove(t.getUid());
                    String operation = "";
                    switch (t.getOperation()){
                        case 0 -> operation = " + ";
                        case 1 -> operation = " - ";
                        case 2 -> operation = " * ";
                        case 3 -> operation = " / ";
                    }
                    long finish = System.nanoTime();
                    long elapsed = finish - start;
                    if(t.isFlagRes())
                        out.write(t.getA() + operation + t.getB() + " = " + task.getResult() + " - OK Time: " + (elapsed/ 1000000000) + " s");
                    else
                        out.write(t.getA() + operation + t.getB() + " = None - ERROR Time: " + (elapsed/ 1000000000) + " s");
                    out.newLine();
                    out.flush();

                }
                in.close();
                out.close();
                clientDialog.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

    }
    }

