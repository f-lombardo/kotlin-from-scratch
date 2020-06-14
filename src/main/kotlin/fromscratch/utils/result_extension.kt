package fromscratch.utils

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen

inline fun <V, E, R> Result<V, E>.andThenRun(transform: V.() -> Result<R, E>): Result<R, E> =
    andThen(transform)

inline fun <V, E, R> Result<V, E>.andThenMap(transform: V.() -> R): Result<R, E> =
    when (this) {
        is Ok -> Ok(value.transform())
        is Err -> this
    }

