package profiler;

import org.aspectj.lang.Signature;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class StatCollector {

    private StatCollector() {
    }

    private static final StatCollector INSTANCE = new StatCollector();
    private static final String PACKAGE = System.getProperty("profiledPackage");

    public static StatCollector getInstance() {
        return INSTANCE;
    }

    public static String getPackageName() {
        return PACKAGE;
    }

    private static String signatureToString(Signature signature) {
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    private static class Timestamp {
        String first;
        Instant second;

        Timestamp(String first, Instant second) {
            this.first = first;
            this.second = second;
        }
    }

    private final HashMap<String, Duration> callTimer = new HashMap<>();
    private final HashMap<String, Long> callCount = new HashMap<>();
    private final HashMap<String, Long> failCount = new HashMap<>();
    private final HashMap<String, HashMap<String, Long>> nextCallCount = new HashMap<>();
    private final Stack<Timestamp> callStack = new Stack<>();

    public void push(Signature signature) {
        String name = signatureToString(signature);
        if (!callStack.empty()) {
            String prev = callStack.peek().first;
            if (!nextCallCount.containsKey(prev)) {
                nextCallCount.put(prev, new HashMap<>());
            }
            nextCallCount.get(prev).compute(name, (s, c) -> c == null ? 1 : c + 1);
        }
        callStack.push(new Timestamp(name, Instant.now()));
        callCount.compute(name, (s, c) -> c == null ? 1 : c + 1);
    }

    public void pop(Signature signature) {
        String name = signatureToString(signature);
        Timestamp top = callStack.pop();
        assert name.equals(top.first);
        Duration thisCall = Duration.between(top.second, Instant.now());
        callTimer.compute(name, (s, d) -> d == null ? thisCall : d.plus(thisCall));
    }

    public void fail(Signature signature) {
        pop(signature);
        String name = signatureToString(signature);
        failCount.compute(name, (s, f) -> f == null ? 1 : f + 1);
    }

    public void printStatistics() {
        ArrayList<String> methodNames = new ArrayList<>(callTimer.keySet());
        methodNames.sort(Comparator.comparing(callTimer::get).reversed());

        StringBuilder sb = new StringBuilder();
        for (String name : methodNames) {
            Duration totalTime = callTimer.get(name);
            sb.append(name)
                    .append("\n")
                    .append("\t: called ").append(callCount.get(name))
                    .append("/failed ").append(failCount.getOrDefault(name, 0L))
                    .append("\n")
                    .append("\t: total ").append(totalTime.toNanos())
                    .append(" ns/average ").append(totalTime.dividedBy(callCount.get(name)).toNanos())
                    .append(" ns\n");
            HashMap<String, Long> nextCalls = nextCallCount.getOrDefault(name, new HashMap<>());
            for (Map.Entry<String, Long> entry : nextCalls.entrySet()) {
                sb.append("\t\t-> called ")
                        .append(entry.getKey()).append(" ")
                        .append(entry.getValue()).append(" times\n");
            }
        }

        System.out.println(sb.toString());
    }

}
