package com.dingshaoshuai.studyapt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dingshaoshuai.lib_annotation.TestAnnotation
import java.util.*

@TestAnnotation
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testAutoService()
        testAPT()
    }

    private fun testAutoService() {
        ServiceLoader.load(Person::class.java).forEach {
            it.run()
            println("hashCode: ${it.hashCode()}")
        }
        // 每次调用都是生成的新的对象
        ServiceLoader.load(Person::class.java).forEach {
            it.run()
            println("hashCode: ${it.hashCode()}")
        }
    }

    private fun testAPT() {
        val teacher = Teacher()
        teacher.lecture("思想政治")
        val student = Student()
        println("100 + 200 = ${student.add(100, 200)}")
    }
}