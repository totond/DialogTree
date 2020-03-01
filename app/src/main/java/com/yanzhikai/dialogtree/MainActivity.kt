package com.yanzhikai.dialogtree

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yanzhikai.dialogtree.tree.DTNodeCallBack
import com.yanzhikai.dialogtree.tree.DialogNode
import com.yanzhikai.dialogtree.tree.DialogTestUtil
import com.yanzhikai.dialogtree.tree.DialogTreeNode
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_test.setOnClickListener {
            buildNodes()
        }

    }

    private fun buildNodes() {
        val dialogA = buildDialog1("A", "我是A")
        val data1 = Data1()
        val nodeA = object : DialogTreeNode<Data1>(dialogA, data1, "a") {
            override fun onPreShow(data: Data1) {
                data.a = 1
            }

            override fun showWhat(data: Data1): Int? {
                data.let {
                    return if (it.a > 0) DTNodeCallBack.Type.THIS else null
                }
            }

            override fun onPositiveCall(data: Data1) {
                Log.i("jky", "onPositiveCall A")
            }

            override fun onCall(key: Int) {
                super.onCall(key)
                Log.i("jky", "onCall A $key")
            }
        }

        val dialogB = buildDialog("B", "我是B")
        val nodeB = object : DialogTreeNode<Data1>(dialogB, data1, "b") {
            override fun showWhat(data: Data1): Int? {
                data?.let {
                    return if (it.b > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        val dialogC = buildDialog("C", "我是C")
        val nodeC = DialogTreeNode<Data1>(dialogC, data1, "c")

        val dialogD = buildDialog("D", "我是D")
        val nodeD = object : DialogTreeNode<Data1>(dialogD, data1, "d") {
            override fun showWhat(data: Data1): Int? {
                data?.let {
                    return if (it.d > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        val dialogE = buildDialog("E", "我是E")
        val nodeE = object : DialogTreeNode<Data1>(dialogE, data1, "e") {
            override fun showWhat(data: Data1): Int? {
                data?.let {
                    return if (it.e > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        val dialogF = buildDialog("F", "我是F")
        val nodeF = object : DialogTreeNode<Data1>(dialogF, data1, "f") {
            override fun showWhat(data: Data1): Int? {
                data?.let {
                    return if (it.f > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        /*
                    a
                  c   b
                 f      d
                          e
                            f
         */

        nodeA.positiveNode = nodeB
        nodeA.negativeNode = nodeC
        nodeB.positiveNode = nodeD
        nodeC.negativeNode = nodeF
        nodeD.positiveNode = nodeE
        nodeE.negativeNode = nodeF

        nodeA.childNodes[3] = buildDialogTreeNode("异步方式测试","异步方式测试")

        nodeA.start()

        Log.i("jky", DialogTestUtil.getOutputTrees(nodeA).toString())

    }

    private fun buildDialog(title: String, content: String): DialogNode<Data1> {
        return object : DialogNode<Data1>(2) {
            override fun buildDialog(data: Data1): Dialog {
                val builder = AlertDialog.Builder(this@MainActivity)
                return DialogNode.createDialog(builder.setTitle(title).setMessage(content).create(),"是", "否", this)
            }

        }
    }

    private fun buildDialog1(title: String, content: String): DialogNode<Data1> {
        return object : DialogNode<Data1>(2) {
            override fun buildDialog(data: Data1): Dialog {
                val builder = AlertDialog.Builder(this@MainActivity)
                val positiveCallBack = DialogNode.DialogButtonCallback()
                val negativeCallback = DialogNode.DialogButtonCallback()
                val thirdCallback = DialogNode.DialogButtonCallback()
                builder.setTitle(title)
                    .setMessage(content)
                    .setPositiveButton("是") { _, _ ->
                        positiveCallBack.onCall()
                    }
                    .setNegativeButton("否") { _, _ ->
                        negativeCallback.onCall()
                    }
                    .setNeutralButton("第三个") { _, _ ->
                        thirdCallback.onCall()
                    }

                callBacks.put(DTNodeCallBack.Type.POSITIVE, positiveCallBack)
                callBacks.put(DTNodeCallBack.Type.NEGATIVE, negativeCallback)
                callBacks.put(3, thirdCallback)

                return builder.create()
            }

        }

    }

    private fun buildDialogTreeNode(title: String, content: String): DialogTreeNode<Data1> {
        val mockObservable = Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
        val dialogNode = buildDialog(title, content)
        val dialogTreeNode = object : DialogTreeNode<Data1>(dialogNode, Data1()) {
            override fun interceptPreShowObservable(data: Data1): Observable<Any> {
                return Observable.zip(mockObservable, super.interceptPreShowObservable(data), BiFunction<Boolean, Any, Any>{ _, _ ->
                    return@BiFunction Any()
                })

            }
        }

        return dialogTreeNode

    }
}
