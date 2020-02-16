package com.yanzhikai.dialogtree.tree

import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import androidx.annotation.CallSuper
import androidx.core.util.forEach
import androidx.core.util.set
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Dialog管理控件，把每个需要弹出的Dialog的逻辑（为什么弹，弹的内容，点击回调）
 * 使用者可以继承DialogTreeNode，传入data，按需实现各个回调
 * @author jacketyan
 * @date 2019/9/9
 */
/**
 * todo
 * 注释优化
 * 测试方式实现
 * 测试用例实现
 */
open class DialogTreeNode<T>(
    private val dialogNode: DialogNode,
    private var data: T? = null,
    var alias: String = ""
) :
    DTNodeCallBack<T> {
    companion object {
        private const val TAG = "DialogTreeNode"
    }

    internal val id: Long = DTIdGenerator.instance.generate()

    var positiveNode: DialogTreeNode<out Any>? = null
        set(value) {
            childNodes[DTNodeCallBack.Type.NEGATIVE] = value
            field = value
        }

    var negativeNode: DialogTreeNode<out Any>? = null
        set(value) {
            childNodes[DTNodeCallBack.Type.NEGATIVE] = value
            field = value
        }

    var childNodes: SparseArray<DialogTreeNode<out Any>?> = SparseArray(2)

    private val compositeDisposable = CompositeDisposable()

    init {
        if (TextUtils.isEmpty(alias)) {
            alias = id.toString()
        }

        dialogNode.dismissCallback = {
            onDismissCall()
        }


        dialogNode.callBacks.forEach { key, value ->
            value.callBack = { callButtonClick(key) }
        }

    }


    override fun onShowCallback() {

    }

    override fun onPreShow(data: T?) {
    }

    @CallSuper
    override fun getPreShowObservable(data: T?): Observable<Any> {
        return Observable.create<Any> {
            onPreShow(data)
            it.onNext(Any())
        }
            .subscribeOn(Schedulers.io())
    }

    @CallSuper
    override fun onPositiveCall() {
        onNodeCall(positiveNode)
    }

    @CallSuper
    override fun onNegativeCall() {
        onNodeCall(negativeNode)

    }

    @CallSuper
    override fun onCall(key: Int) {
        onNodeCall(childNodes.get(key))
    }

    override fun onDismissCall() {
        Log.i(TAG, "onDismissCall")
    }

    override fun showWhat(data: T?): Int? {
        return DTNodeCallBack.Type.THIS
    }

    open fun show() {
        dialogNode.dialog.show()
    }

    /**
     * 从这个节点开始
     * @return CompositeDisposable
     */
    fun start(): CompositeDisposable {

        val disposable = getPreShowObservable(data)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val show = showWhat(data)
                if (show != null) {
                    if (show == DTNodeCallBack.Type.THIS) {
                        show()
                    } else {
                        onCall(show)
                    }
                }
            }
        compositeDisposable.add(disposable)
        return compositeDisposable
    }

    fun testShow(show: Int?) {
        if (show != null) {
            if (show == DTNodeCallBack.Type.THIS) {
                show()
            } else {
                onCall(show)
            }
        }
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun onNodeCall(node: DialogTreeNode<out Any>?) {
        node?.let {
            compositeDisposable.add(it.start())
        }

        dialogNode.dialog.dismiss()
    }

    private fun callButtonClick(key: Int) {
        when (key) {
            DTNodeCallBack.Type.POSITIVE -> onPositiveCall()
            DTNodeCallBack.Type.NEGATIVE -> onNegativeCall()
            else -> onCall(key)
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (other is DialogTreeNode<*>) {
            return id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}