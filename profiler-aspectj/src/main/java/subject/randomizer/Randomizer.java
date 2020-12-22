package subject.randomizer;

import subject.counter.Counter;

import java.util.Random;

public class Randomizer {

    private final int limit;
    private final Random random;

    public Randomizer(int limit) {
        this.limit = limit;
        random = new Random(limit);
    }

    public void runOnce() {
        Counter counter = new Counter();
        counter.setA(random.nextInt(limit));
        counter.setB(random.nextInt(limit));
        try {
            counter.count();
        } catch (RuntimeException ignored) {
        }
    }

    public void run() {
        for (int i = 0; i < limit; i++) {
            runOnce();
        }
    }

}
