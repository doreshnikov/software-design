import profiler.StatCollector;
import subject.randomizer.Randomizer;

public class Main {

    public static void main(String[] args) {

        if (args.length > 0 && !args[0].startsWith("-")) {
            System.setProperty("profiledPackage", args[0]);
        } else {
            System.setProperty("profiledPackage", "subject");
        }
        StatCollector collector = StatCollector.getInstance();
        new Randomizer(5000).run();
        collector.printStatistics();

    }

}
