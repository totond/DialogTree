package com.yanzhikai.dialogtree

import android.app.AlertDialog
import android.app.Dialog
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.yanzhikai.dialogtree.tree.DTNodeCallBack
import com.yanzhikai.dialogtree.tree.DialogNode
import com.yanzhikai.dialogtree.tree.DialogTestUtil
import com.yanzhikai.dialogtree.tree.DialogTreeNode
import org.junit.Test

/**
 * author: jacketyan
 * date: 2020/2/16
 */
class DialogTreeTest{
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testDialog()
    {
        buildNodes()

    }

    private fun buildNodes() {
        val dialogA = buildDialog("A", "我是A")
        val data1 = Data1()
        val nodeA = object : DialogTreeNode<Data1>(dialogA, data1, "a") {
            override fun onPreShow(data: Data1?) {
                data?.a = 1
            }

            override fun showWhat(data: Data1?): Int? {
                data?.let {
                    return if (it.a > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        val dialogB = buildDialog("B", "我是B")
        val nodeB = object : DialogTreeNode<Data1>(dialogB, data1, "b") {
            override fun showWhat(data: Data1?): Int? {
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
            override fun showWhat(data: Data1?): Int? {
                data?.let {
                    return if (it.d > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        val dialogE = buildDialog("E", "我是E")
        val nodeE = object : DialogTreeNode<Data1>(dialogE, data1, "e") {
            override fun showWhat(data: Data1?): Int? {
                data?.let {
                    return if (it.e > 0) DTNodeCallBack.Type.THIS else null
                }
                return null
            }
        }

        val dialogF = buildDialog("F", "我是F")
        val nodeF = object : DialogTreeNode<Data1>(dialogF, data1, "f") {
            override fun showWhat(data: Data1?): Int? {
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

        nodeA.start()

        Log.i("jky", DialogTestUtil.getOutputTrees(nodeA).toString())

    }

    private fun buildDialog(title: String, content: String): DialogNode<Data1> {
        return object : DialogNode<Data1>(2) {
            override fun buildDialog(): Dialog {
                val builder = AlertDialog.Builder(context)

                return DialogNode.createDialog(builder.setTitle(title).setMessage(content).create(),"是", "否", this)
            }

        }
    }
}