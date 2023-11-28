package com.cf.annotation

/**
 * @ClassName: LayoutRes
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2019/9/2 16:58
 * @Version: 1.7.0
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Holder(val layoutName: String = "", val itemType: Int = -1,val binding:Boolean = false)
