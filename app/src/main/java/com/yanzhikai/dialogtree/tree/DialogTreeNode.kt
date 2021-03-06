package com.yanzhikai.dialogtree.tree

import android.text.TextUtils
import android.util.Log
import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.set

/**
 * Dialog管理控件，把每个需要弹出的Dialog的逻辑（为什么弹，弹的内容，点击回调）
 * 使用者可以继承DialogTreeNode，传入data，按需实现各个回调
 * @author jacketyan
 * @date 2019/11/12
 */
open class DialogTreeNode<T>(
    private val rawDialogNode: DialogNode<T>,
    private var data: T,
    var alias: String = ""
) :
    DTNodeCallBack<T> {
    companion object {
        private const val TAG = "DialogTreeNode"
    }

    private val dialogNode by lazy {
        rawDialogNode.init(data)
        rawDialogNode.dismissCallback = {
            onDismissCall()
        }


        rawDialogNode.callBacks.forEach { (key, value) ->
            value.callBack = { callButtonClick(key) }
        }
        rawDialogNode
    }

    internal val id: Long = DTIdGenerator.instance.generate()

    var positiveNode: DialogTreeNode<out Any>? = null
        set(value) {
            childNodes[DTNodeCallBack.Type.POSITIVE] = value
            field = value
        }

    var negativeNode: DialogTreeNode<out Any>? = null
        set(value) {
            childNodes[DTNodeCallBack.Type.NEGATIVE] = value
            field = value
        }

    var childNodes: HashMap<Int, DialogTreeNode<out Any>?> = HashMap(2)

    private var showNode: Int? = DTNodeCallBack.Type.THIS

    private val compositeDisposable = CompositeDisposable()

    init {
        if (TextUtils.isEmpty(alias)) {
            alias = id.toString()
        }
    }

    fun setShowNode(node: Int?){
        showNode = node
    }

    override fun onShowCallback() {

    }

    override fun onPreShow(data: T) {
    }

    /**
     * 异步调用方式，可以通过重写此方法实现异步调用
     * 1.给super返回的Observable切一个线程subscribeOn(xx)，相当于onPreShow()切线程
     * 2.return一个由super返回的Observable和实际异步Observable的zip起来的Observable<Any>
     * 3.直接不使用super的结果，return一个自定义的observable<Any>
     * @param data T
     * @return Observable<Any>
     */
    @CallSuper
    override fun interceptPreShowObservable(data: T): Observable<Any> {
        return Observable.create<Any> {
            onPreShow(data)
            it.onNext(Any())
        }
    }

    @CallSuper
    final override fun onPositiveNodeCall() {
        onPositiveCall(data)
        onNodeCall(positiveNode)
    }

    @CallSuper
    final override fun onNegativeNodeCall() {
        onNegativeCall(data)
        onNodeCall(negativeNode)

    }

    open fun onPositiveCall(data: T) {}

    open fun onNegativeCall(data: T) {}

    @CallSuper
    override fun onCall(key: Int) {
        onNodeCall(childNodes.get(key))
    }

    override fun onDismissCall() {
        Log.i(TAG, "onDismissCall")
    }

    /**
     * 默认展示自己
     * @param data T
     * @return Int?
     */
    override fun showWhat(data: T): Int? {
        return showNode
    }

    open fun show() {
        dialogNode.show()
    }

    /**
     * 从这个节点开始
     * @return CompositeDisposable
     */
    fun start(): CompositeDisposable {

        val disposable = interceptPreShowObservable(data)
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

        dialogNode.dismiss()
    }

    /**
     * 根据key值调用对应回调
     * @param key Int
     */
    private fun callButtonClick(key: Int) {
        when (key) {
            DTNodeCallBack.Type.POSITIVE -> onPositiveNodeCall()
            DTNodeCallBack.Type.NEGATIVE -> onNegativeNodeCall()
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