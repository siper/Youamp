package ru.stersh.youamp.core.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.packInts
import androidx.compose.ui.util.unpackInt1
import androidx.compose.ui.util.unpackInt2

@Immutable
@JvmInline
value class MovePosition internal constructor(
    private val packedValue: Long,
) {
    val from: Int
        get() = unpackInt1(packedValue)

    val to: Int
        get() = unpackInt2(packedValue)
}

fun MovePosition(
    from: Int,
    to: Int,
) = MovePosition(
    packInts(
        from,
        to,
    ),
)
