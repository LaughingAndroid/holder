package com.cf.base

import java.util.HashSet
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 *
 * @ClassName: BaseProcessor
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2019/9/17 10:24
 * @Version: 1.7.0
 */
open class BaseProcessor : AbstractProcessor() {
    lateinit var mFiler: Filer
    lateinit var mElements: Elements
    lateinit var mMessager: Messager
    lateinit var mLogger: Logger
    var packageName: String = ""
    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        try {
            mFiler = processingEnv.filer
        } catch (e: Exception) {
        }
        return true
    }

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        mLogger = Logger(processingEnvironment.messager)
        packageName = processingEnvironment.options[Constants.OPTION_PACKAGE_NAME] ?: ""
        mLogger.info("packageName:$packageName")
    }


    override fun getSupportedOptions(): Set<String> {
        return object : HashSet<String>() {
            init {
                this.add(Constants.OPTION_PACKAGE_NAME)
            }
        }
    }
}