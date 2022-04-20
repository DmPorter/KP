package agentsServer;

import forTask.Task;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class MonoThreadAgent implements Runnable{
    private Socket agentDialog;
    private LinkedBlockingQueue<Task> inQ;
    private LinkedBlockingQueue<Task> outQ;

    public MonoThreadAgent(Socket agentDialog, LinkedBlockingQueue<Task> q, LinkedBlockingQueue<Task> out) {
        this.agentDialog = agentDialog;
        this.inQ = q;
        this.outQ = out;

    }
    @Override
    public void run() {
        Task t = null;
        boolean flagRes = false;
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(agentDialog.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(agentDialog.getInputStream()));
            while (!agentDialog.isClosed()) {
                flagRes = false;
                t = inQ.take();
                String str = t.getUid() + " " + t.getOperation() + " " + t.getA() + " " + t.getB();
                out.write(str);
                out.newLine();
                out.flush();

                String answer = in.readLine();
                System.out.println("answer: " + answer);
                int[] numArr = Arrays.stream(answer.substring(7).split(" ")).mapToInt(Integer::parseInt).toArray();
                System.out.println(Arrays.toString(numArr));
                if(numArr[numArr.length - 2] == 1)
                    t.setRes(numArr[numArr.length - 1]);
                else t.setFlagRes(false);
                outQ.put(t);
                flagRes = true;
                System.out.println(answer);
            }
            System.out.println("AgentClose");
            agentDialog.close();
        } catch (IOException | InterruptedException e) {
            if(t != null && !flagRes){
                try {
                    inQ.put(t);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        }

    }
}
