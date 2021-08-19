package com.dingshaoshuai.studyapt

import com.google.auto.service.AutoService

/**
 * @author: Xiao Bo
 * @date: 19/8/2021
 */
interface Person {
    fun run()
}

@AutoService(Person::class)
class Man : Person {
    override fun run() {
        println("男人在奔跑")
    }
}

@AutoService(Person::class)
class Woman : Person {
    override fun run() {
        println("女人在奔跑")
    }
}

@AutoService(Person::class)
class Mutants : Person {
    override fun run() {
        println("变种人在奔跑")
    }
}