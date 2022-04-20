package agentsServer;

import forTask.Task;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class MonoThreadAgent implements Runnable{

    private final Socket agentDialog;
    private final LinkedBlockingQueue<Task> inLinkedQueue; // Очередь невыполненных состояний
    private final LinkedBlockingQueue<Task> outLinkedQueue; // Очередь выполненных

    public MonoThreadAgent(Socket agentDialog, LinkedBlockingQueue<Task> q, LinkedBlockingQueue<Task> out) {
        this.agentDialog = agentDialog;
        this.inLinkedQueue = q;
        this.outLinkedQueue = out;

    }

    @Override
    public void run() {
        Task t = null;
        boolean flagRes = false; // Флаг, показывающий агент выполнил ли задание (случай, когда произошел обрыв связи с агентом)
        try {
            System.out.println("Agent accepted " + agentDialog.getInetAddress());
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(agentDialog.getOutputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(agentDialog.getInputStream()));
            while (!agentDialog.isClosed()) {

                flagRes = false;
                t = inLinkedQueue.take();
                String str = t.getUid() + " " + t.getOperation() + " " + t.getA() + " " + t.getB();

                bufferedWriter.write(str);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String answer = bufferedReader.readLine();
                System.out.println("answer uuid task: " + answer.substring(0, 7));
                int[] numArr = Arrays.stream(answer.substring(7).split(" "))
                                            .mapToInt(Integer::parseInt).toArray();

                if(numArr[numArr.length - 2] == 1)
                    t.setRes(numArr[numArr.length - 1]);
                else t.setValidRes(false);

                outLinkedQueue.put(t);

                flagRes = true;

            }

            agentDialog.close();
            bufferedWriter.close();
            bufferedReader.close();

        } catch (IOException | InterruptedException e) {
            if(t != null && !flagRes){
                try {
                    inLinkedQueue.put(t);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        }

    }
}
