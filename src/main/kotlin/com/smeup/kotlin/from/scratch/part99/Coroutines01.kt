package com.smeup.kotlin.from.scratch.part99

import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

fun findBigPrimeBlocking(): BigInteger {
    println("Running in ${Thread.currentThread().name}")
    return BigInteger.probablePrime(4096, Random())
}

suspend fun findBigPrime(): BigInteger =
    withContext(Dispatchers.Default) {
        findBigPrimeBlocking()
    }

object Coroutines01 {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        println("Starting...")
    }
}

