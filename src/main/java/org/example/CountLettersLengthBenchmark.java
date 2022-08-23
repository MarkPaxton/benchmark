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

public class CountLettersLengthBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                        .include(CountLettersLengthBenchmark.class.getSimpleName())
                        .forks(1)
                        .build();

        new Runner(opt).run();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void forLoop(ExecutionPlan plan, Blackhole blackhole) {
        blackhole.consume(CountLetters.countWithForLoops(plan.getString()));
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void stream(ExecutionPlan plan, Blackhole blackhole) {
        blackhole.consume(CountLetters.countWithStreams(plan.getString()));
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void parallelStream(ExecutionPlan plan, Blackhole blackhole) {
        blackhole.consume(CountLetters.countWithParallelStreams(plan.getString()));
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({ "1", "50", "500", "10000", "50000", "100000" })
        public int stringLength;

        public String getString() {
            var sb = new StringBuilder();
            for (var i = 0; i < stringLength; i++) {
                sb.append(UUID.randomUUID().toString().replace("-", ""));

            }
            return sb.toString();
        }

    }
}
