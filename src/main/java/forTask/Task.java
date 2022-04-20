package forTask;

import java.util.Random;

public class Task {
    private int a;
    private int b;
    private String uid = Long.toUnsignedString(new Random().nextLong(), 36).substring(0,6);
    private int res = 0;
    private int operation;
    private boolean flagRes = true;

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public boolean isFlagRes() {
        return flagRes;
    }

    public void setFlagRes(boolean flagRes) {
        this.flagRes = flagRes;
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

    public Task(int a, int b, int operation){
        this.a = a; this.b = b; this.operation = operation;
    }
}
