package com.smeup.kotlin.from.scratch.part99

import kotlinx.coroutines.*

fun task1() {
    println("Start task1 | Thread ${Thread.currentThread()}")
    println("End task1 | Thread ${Thread.currentThread()}")
}

fun task2() {
    println("Start task2 | Thread ${Thread.currentThread()}")
    println("End task2 | Thread ${Thread.currentThread()}")
}

fun main() {
    println("Start main")

    runBlocking {
        task1()
        task2()
        println("Called task1 and task2 from ${Thread.currentThread()}")
    }

    println("End main")
}
