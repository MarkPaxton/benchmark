package org.example;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.example.tests.CountLetters;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class CountLettersBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                        .include(CountLettersBenchmark.class.getSimpleName())
                        .forks(1)
                        .build();

        new Runner(opt).run();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void TestForLoop(ExecutionPlan plan, Blackhole blackhole) {
        for (var i = 0; i < plan.iterations; i++) {
            blackhole.consume(CountLetters.countWithForLoops(plan.getString()));
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void TestStream(ExecutionPlan plan, Blackhole blackhole) {
        for (var i = 0; i < plan.iterations; i++) {
            blackhole.consume(CountLetters.countWithStreams(plan.getString()));
        }
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan {

        @Param({ "1", "1000" })
        public int iterations;

        @Param({ "1", "10000" })
        public int stringLength;

        public String getString() {
            var sb = new StringBuilder();
            for (var i = 0; i < stringLength; i++) {
                sb.append(UUID.randomUUID().toString().replace("_", ""));
            }
            return sb.toString();
        }

    }
}
