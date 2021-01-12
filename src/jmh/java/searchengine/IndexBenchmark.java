package searchengine;

// JMH Imports.
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

// Other Imports.
import java.util.concurrent.TimeUnit;
import java.util.List;

/**
 * The Indexing benchmark. This prototype benchmarks the efficiency of
 * the search engine's index by querying it multiple times and taking
 * time measurements. The benchmark is implemented with JMH, which
 * e.g. takes care of warming up the JVM. It is in part inspired by
 * {@code BenchmarkModes} and {@code States} in the JMH Samples. This
 * does not start a web server.
 *
 * @author Willard Rafnsson
 * @see <a href="https://openjdk.java.net/projects/code-tools/jmh/">JMH</a>
 * @see <a href="https://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/">JMH Samples</a>
 * @see <a href="https://appdoc.app/artifact/org.openjdk.jmh/jmh-core/1.10.2/org/openjdk/jmh/annotations/package-summary.html">JMH Annotations Documentation</a>
 */
public class IndexBenchmark {

    /**
     * The state used by all each run of the benchmark. This is JMH's
     * way to share state between benchmark runs; you do not need to
     * know how it works (this is an "inner class"). This is where we
     * create an instance of {@code SearchEngine}. We want multiple
     * queries to the same {@code SearchEngine} instance, since
     * otherwise, it would be hard to argue that the time measurements
     * we get are the index look-ups specifically, and not e.g. the
     * time it takes to read a file, obtain a {@code SearchEngine}
     * instance, and so on.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        public Index index;
        public BenchmarkState(){
            // Executed each time "# Fork: X of 5" appears in the output.
            List<Website> sites = FileHelper.parseFile("data/enwiki-medium.txt");
            index = new InvertedIndexHashMap();
            index.build(sites);
        }
    }

    /**
     * Measures the average execution time of this method.
     *
     * How it works: JMH continuously calls this method in a fixed
     * (rather long) window of time, measures the time each method
     * call took, and counts how many times the method was called. JMH
     * then uses this data to compute the average execution time.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void measureAvgTime(BenchmarkState state) throws InterruptedException {
        // Search for these words.
        state.index.lookup("denmark");
        state.index.lookup("archipelago");
        state.index.lookup("by");
        state.index.lookup("okhotsk");
        state.index.lookup("population");
        state.index.lookup("amager");
        state.index.lookup("the");
        state.index.lookup("uppsala");
        state.index.lookup("danish");
        state.index.lookup("nordic");
        state.index.lookup("sea");
        state.index.lookup("climate");
        state.index.lookup("immigration");
        state.index.lookup("million");
        state.index.lookup("copenhagen");
        state.index.lookup("selfgoverning");
        state.index.lookup("km");
        state.index.lookup("which");
        state.index.lookup("urban");
        state.index.lookup("zealand");
    }

    /**
     * JMH-magic. This needs to be here, but this {@code main} is
     * actually never run. JMH generates lots of other classes, and
     * then runs those instead.
     */
    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(IndexBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}