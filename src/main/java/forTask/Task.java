package forTask;

import java.util.Random;

public class Task {
    private final int a;
    private final int b;
    private final int operation;
    private final String uid = Long.toUnsignedString(new Random().nextLong(), 36).substring(0,6);
    private int res = 0;
    private boolean validRes = true;

    public Task(int a, int b, int operation){
        this.a = a; this.b = b; this.operation = operation;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public boolean isValidRes() {
        return validRes;
    }

    public void setValidRes(boolean validRes) {
        this.validRes = validRes;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getRes() {
        return res;
    }

    public String getUid(){
        return uid;
    }

    public int getOperation() {
        return operation;
    }
}
