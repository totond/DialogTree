package com.yanzhikai.dialogtree

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yanzhikai.dialogtree.tree.DialogNode
import com.yanzhikai.dialogtree.tree.DialogTestUtil
import com.yanzhikai.dialogtree.tree.DialogTreeNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_test.setOnClickListener {
            buildNodes()
        }

    }

    private fun buildNodes() {
        val dialogA = buildDialog("A", "我是A")
        val data1 = Data1()
        val nodeA = object : DialogTreeNode<Data1>(dialogA, data1, "a") {
            override fun onPreShow(data: Data1?) {
                data?.a = 1
            }

            override fun shouldShow(data: Data1?): Boolean {
                data?.let {
                    return it.a > 0
                }
                return false
            }
        }

        val dialogB = buildDialog("B", "我是B")
        val nodeB = object : DialogTreeNode<Data1>(dialogB, data1, "b") {
            override fun shouldShow(data: Data1?): Boolean {
                data?.let {
                    return it.b > 0
                }
                return false
            }
        }

        val dialogC = buildDialog("C", "我是C")
        val nodeC = DialogTreeNode<Data1>(dialogC, data1, "c")

        val dialogD = buildDialog("D", "我是D")
        val nodeD = object : DialogTreeNode<Data1>(dialogD, data1, "d") {
            override fun shouldShow(data: Data1?): Boolean {
                data?.let {
                    return it.d > 0
                }
                return false
            }
        }

        val dialogE = buildDialog("E", "我是E")
        val nodeE = object : DialogTreeNode<Data1>(dialogE, data1, "e") {
            override fun shouldShow(data: Data1?): Boolean {
                data?.let {
                    return it.e > 0
                }
                return false
            }
        }

        val dialogF = buildDialog("F", "我是F")
        val nodeF = object : DialogTreeNode<Data1>(dialogF, data1, "f") {
            override fun shouldShow(data: Data1?): Boolean {
                data?.let {
                    return it.f > 0
                }
                return false
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

    private fun buildDialog(title: String, content: String): DialogNode {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(content)

        return DialogNode.create(builder, "是", "否")
    }
}
