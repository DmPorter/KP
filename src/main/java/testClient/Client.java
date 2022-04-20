package testClient;

import java.io.*;
import java.net.Socket;
import forTask.Task;

public class Client {

    public static void main(String[] args) {
        Task[] tasks = new Task[15];
        for(int i = 0; i < 15; i++){
            tasks[i] = new Task(i, i, 2);
        }
        try (
                Socket socket = new Socket("0.0.0.0", 8011);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            long start = System.nanoTime();
            for (int i = 0; i < 15; i++) {
                writer.write(tasks[i].getOperation() + " " + tasks[i].getA() + " " + tasks[i].getB());
                writer.newLine();
                writer.flush();
                String response = reader.readLine();
                System.out.println("Ответ сервера: " + response);
            }
            long finish = System.nanoTime();
            long elapsed = finish - start;
            System.out.println(elapsed / 1000000000);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
