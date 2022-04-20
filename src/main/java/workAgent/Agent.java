package workAgent;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Agent {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8010);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            while(socket.isBound()) {
                String response = reader.readLine();
                StringBuilder str = new StringBuilder();
                str.append(response, 0, 6).append(" ");
                response = response.substring(7);

                int[] numArr = Arrays.stream(response.split(" ")).mapToInt(Integer::parseInt).toArray();
                int val = 0;
                boolean flag = true;
                switch (numArr[0]) {
                    case 0 -> {
                        try{
                            val = numArr[1] + numArr[2];
                        }catch (ArithmeticException e){
                            flag = false;
                        }
                        sleep(1000L);
                    }
                    case 1 -> {
                        try{
                            val = numArr[1] - numArr[2];
                        }catch (ArithmeticException e){
                            flag = false;
                        }
                        sleep(2000L);
                    }
                    case 2 -> {
                        try{
                            val = numArr[1] * numArr[2];
                        }catch (ArithmeticException e){
                            flag = false;
                        }
                        sleep(3000L);
                    }
                    case 3 -> {
                        try{
                            val = numArr[1] / numArr[2];
                        }catch (ArithmeticException e){
                            flag = false;
                        }
                        sleep(4000L);
                    }
                }
                str.append(numArr[0]).append(" ").append(numArr[1]).append(" ").append(numArr[2]).append(" ")
                        .append(flag ? 1:0).append(" ").append(flag ? val : 0);
                System.out.println(str.toString());
                writer.write(str.toString());
                writer.newLine();
                writer.flush();
            }
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
    }

}
