package testClient;

import javax.print.DocFlavor;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SUM = "Сумма : 0 \n";
    private static final String DIF = "Разность : 1 \n";
    private static final String MUL = "Умножение : 2 \n";
    private static final String DEV = "Деление : 3 \n";
    private static final String ANT = "Не выполнять операцию: любое другое число\n";

    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8011);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));
        ) {
            while(true) {

                StringBuilder str = new StringBuilder();
                System.out.println("Введите код операции \n" + SUM + DIF + MUL + DEV + ANT);
                String operateCode = reader2.readLine();
                if (operateCode.equals("ВЫЙТИ")){
                    return;
                }
                while (!(Integer.parseInt(operateCode) >= 0 && Integer.parseInt(operateCode) <= 3)) {
                    System.out.println("Неверный код операции. Введите еще раз");
                    operateCode = reader2.readLine();
                }
                str.append(operateCode).append(" ");

                if (Integer.parseInt(operateCode) > 3 || Integer.parseInt(operateCode) < 0) {
                } else {

                    System.out.println("Введите первое число: ");
                    String firstOperand = reader2.readLine();
                    str.append(firstOperand).append(" ");
                    System.out.println("Введите второе число: ");
                    String secondOperand = reader2.readLine();
                    str.append(secondOperand);
                    writer.write(str.toString());
                    writer.newLine();
                    writer.flush();
                }
                String response = reader.readLine();
                System.out.println("Ответ сервера: " + response);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

