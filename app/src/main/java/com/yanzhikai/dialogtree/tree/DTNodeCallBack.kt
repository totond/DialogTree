package com.yanzhikai.dialogtree.tree

import androidx.annotation.IntDef
import com.yanzhikai.dialogtree.tree.DTNodeCallBack.Type.Companion.NEGATIVE
import com.yanzhikai.dialogtree.tree.DTNodeCallBack.Type.Companion.POSITIVE
import com.yanzhikai.dialogtree.tree.DTNodeCallBack.Type.Companion.THIS
import io.reactivex.Observable

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
interface DTNodeCallBack<T> {
    /**
     * 确认按钮回调
     */
    fun onPositiveCall()

    /**
     * 取消按钮回调
     */
    fun onNegativeCall()

    /**
     * 所有Dialog的key按钮回调，需要自定义key值
     * @param key Int参 自定义key值，注意考下面的CallBackType，不要覆盖里面的值，规则：大于0
     */
    fun onCall(key: Int)

    /**
     * Dialog取消回调
     */
    fun onDismissCall()

    /**
     * Dialog展示前数据处理回调
     * @param data T?
     */
    fun onPreShow(data: T?)

    /**
     * 异步调用方式
     * @param data T?
     * @return Observable<Any>
     */
    fun getPreShowObservable(data: T?): Observable<Any>

    /**
     * 应该展示哪种回调
     * @param data T?
     * @return Int 回调结果,返回空则是流程结束，可参考CallBackType，也可以自定义
     */
    fun showWhat(data: T?): Int?

    fun onShowCallback()

    class Type {
        companion object {
            //自身展示回调
            const val THIS = -1
            //确认按钮回调
            const val POSITIVE = -2
            //取消按钮回调
            const val NEGATIVE = -3
        }
    }


    @IntDef(value = [THIS, POSITIVE, NEGATIVE])
    annotation class CallBackType
}