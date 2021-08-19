package com.dingshaoshuai.apt_processor

import com.dingshaoshuai.lib_annotation.TestAnnotation
import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import java.io.Writer
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import com.squareup.kotlinpoet.TypeSpec as KTypeSpec

@AutoService(Processor::class)
class TestProcessor : AbstractProcessor() {
    // 打印日志用的
    private lateinit var messager: Messager

    // 写文件用的
    private lateinit var filer: Filer

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        processingEnv ?: throw NullPointerException("这玩意儿都是空的吗？？嗯？？")
        messager = processingEnv.messager
        filer = processingEnv.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        // 处理编译器调用多次问题
        if (annotations?.size ?: 0 < 1) return false
        messager.printMessage(Diagnostic.Kind.NOTE, "TestProcessor#process ------------ start")

        generateTeacherFile()
        javapoetGenerateStudentFile()
        kotlinpoetGenerateStudentFile()

        messager.printMessage(Diagnostic.Kind.NOTE, "TestProcessor#process ------------ end")
        // 这个返回值我不知道啥意思
        return false
    }

    // 这个方法必须要重写
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(TestAnnotation::class.java.canonicalName)
    }

    // 这个方法必须要重写
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun generateTeacherFile() {
        // 创建一个 java 文件
        val sourceFile = filer.createSourceFile("com.dingshaoshuai.studyapt.Teacher")
        var writer: Writer? = null
        try {
            writer = sourceFile.openWriter()
            writer.write(
                """
package com.dingshaoshuai.studyapt;

public class Teacher {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void lecture(String subject) {
        System.out.println("我要开始讲" + subject + "课了");
    }
}
        """.trimIndent()
            )
        } finally {
            writer?.close()
        }
    }

    private fun javapoetGenerateStudentFile() {
        // 使用 javapoet 框架的 Api 生成 java 文件
        // github: https://github.com/square/javapoet

        // 创建属性
        val nameField = FieldSpec.builder(String::class.java, "name").build()
        val ageField = FieldSpec.builder(Int::class.java, "age").build()

        // 创建方法
        val addMethod = MethodSpec.methodBuilder("add")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(TypeName.INT, "i")
            .addParameter(TypeName.INT, "j")
            .returns(Int::class.java)
            .addCode(
                """
            return i + j;
            """.trimIndent()
            )
            .build()

        // 类名称
        val studentClass = TypeSpec.classBuilder("Student")
            // 修饰符
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            // 添加一个属性
            .addField(nameField)
            .addField(ageField)
            // 添加一个方法
            .addMethod(addMethod)
            .build()

        // 搞一个 java 文件对象出来
        val javaFile = JavaFile.builder("com.dingshaoshuai.studyapt", studentClass)
            .build()
        // 写入
        javaFile.writeTo(filer)
    }

    private fun kotlinpoetGenerateStudentFile() {
        // 创建一个方法
        val function = FunSpec.builder("meeting")
            // 方法修饰符
            .addModifiers(KModifier.OPEN)
            // 方法体
            .addCode(
                """
                println("所有老师跟我去开会")
            """.trimIndent()
            )
            .build()

        // 创建一个类，kotlin 与 java 的同名了，这里起了别名：KTypeSpec
        val schoolmasterClass = KTypeSpec.classBuilder("Schoolmaster")
            .addModifiers(KModifier.OPEN)
            // 添加初始代码块
//            .addInitializerBlock()
            .addFunction(function)
            .build()
        val kotlinFile = FileSpec.builder("com.dingshaoshuai.studyapt", "Schoolmaster")
            .addType(schoolmasterClass)
            .build()
        kotlinFile.writeTo(filer)
    }
}