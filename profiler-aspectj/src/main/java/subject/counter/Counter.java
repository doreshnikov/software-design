package subject.counter;

public class Counter {

    private int a, b;

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void countPlus() {
        while (a < b) {
            setA(a + 1);
        }
    }

    public void countDiv() {
        setB(b * 2);
        if (b % a == 0) {
            throw new RuntimeException("Fail");
        }
    }

    public void count() {
        if (a < b) {
            countPlus();
        } else {
            while (a >= b) {
                countDiv();
            }
        }
    }

}
