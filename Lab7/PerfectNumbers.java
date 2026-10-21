import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerfectNumbers {

    private static final IntUnaryOperator divisorSum =
            n -> IntStream.range(1, n).filter(d -> n % d == 0).sum();

    private static final IntPredicate isPerfect =
            n -> divisorSum.applyAsInt(n) == n;

    private static final Function<Integer, String> describeFunction =
            n -> n + " = " + IntStream.range(1, n)
                    .filter(d -> n % d == 0)
                    .boxed()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" + "));

    public static List<Integer> findPerfect(int limit) {
        return IntStream.rangeClosed(1, limit)
                .filter(isPerfect)
                .boxed()
                .collect(Collectors.toList());
    }

    public static String describe(int number) {
        return describeFunction.apply(number);
    }
}
