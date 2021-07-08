package etc.cloud.commons.func;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import java.util.function.Supplier;

/**
 * @author lm_xu
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FuncWrappers {

    public static <T, R> Pair<T, R> wrapperPair(Supplier<T> t1, Supplier<R> t2) {
        return Pair.of(t1.get(), t2.get());
    }
}
