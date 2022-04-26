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
        private final LinkedBlockingQueue<Task> inLinkedQueue;
        private final HashMap<String, QueueOutForThread> waitMap;

        public MonoThreadClientHandler(Socket client, LinkedBlockingQueue<Task> q,HashMap<String, QueueOutForThread> waitMap) {
            MonoThreadClientHandler.clientDialog = client;
            this.inLinkedQueue = q;
            this.waitMap = waitMap;

        }

    @Override
        public void run() {

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
                System.out.println("Client accepted " + clientDialog.getInetAddress());
                while (!clientDialog.isClosed()) {
                    String entry = bufferedReader.readLine();

                    int[] numArr = Arrays.stream(entry.split(" ")).mapToInt(Integer::parseInt).toArray();
                    Task t = new Task(numArr[1], numArr[2], numArr[0]);

                    long start = System.nanoTime();

                    inLinkedQueue.put(t);

                    QueueOutForThread task = new QueueOutForThread();
                    waitMap.put(t.getUid(), task);
                    task.waitQueue();

                    waitMap.remove(t.getUid());

                    long finish = System.nanoTime();
                    long elapsed = finish - start;

                    if(t.isValidRes())
                        bufferedWriter.write(t.getA() + getOperation(t.getOperation()) +
                                t.getB() + " = " + task.getResult() + " - OK Time: "
                                + (elapsed/ 1000000000) + " s");
                    else
                        bufferedWriter.write(t.getA() + getOperation(t.getOperation()) +
                                t.getB() + " = None - ERROR Time: "
                                + (elapsed/ 1000000000) + " s");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                }

                bufferedReader.close();
                bufferedWriter.close();
                clientDialog.close();
            } catch (IOException | InterruptedException | NumberFormatException e) {
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()));
                    bufferedWriter.write("Error");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    clientDialog.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }

    }

    private static String getOperation(int operation){
        switch (operation){
            case 0 -> {
                return " + ";
            }
            case 1 -> {
                return " - ";
            }
            case 2 -> {
                return " * ";
            }
            case 3 -> {
                return " / ";
            }
            default -> {
                return "";
            }
        }
    }
    }

