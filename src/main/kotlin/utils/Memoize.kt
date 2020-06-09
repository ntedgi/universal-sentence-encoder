package utils

import java.util.concurrent.ConcurrentHashMap

private class Memoize1<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = mutableMapOf<T, R>()
    override fun invoke(x: T): R {
        return values.getOrPut(x, { f(x) })
    }
}

private class Memoize1Concurrent<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = ConcurrentHashMap<T, R>()
    override fun invoke(x: T): R {
        return values.getOrPut(x, { f(x) })
    }
}

private class Memoize2<in T, out R>(val f: (T, T) -> R) : (T, T) -> R {
    private val values = mutableMapOf<Pair<T, T>, R>()
    override fun invoke(x: T, y: T): R {
        return values.getOrPut(Pair(x, y), { f(x, y) })
    }
}

private class Memoize2Concurrent<in T, out R>(val f: (T, T) -> R) : (T, T) -> R {
    private val values = ConcurrentHashMap<Pair<T, T>, R>()
    override fun invoke(x: T, y: T): R {
        return values.getOrPut(Pair(x, y), { f(x, y) })
    }
}

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize1(this)

fun <T, R> ((T, T) -> R).memoize(): (T, T) -> R = Memoize2(this)

fun <T, R> ((T) -> R).memoizeThreadSafe(): (T) -> R = Memoize1Concurrent(this)

fun <T, R> ((T, T) -> R).memoizeThreadSafe(): (T, T) -> R = Memoize2Concurrent(this)