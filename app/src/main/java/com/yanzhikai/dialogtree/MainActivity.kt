package com.yanzhikai.dialogtree

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yanzhikai.dialogtree.tree.DialogNode
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
        val nodeA = object: DialogTreeNode<Data1>(dialogA) {
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
        val nodeB = object: DialogTreeNode<Data1>(dialogB) {
            override fun shouldShow(data: Data1?): Boolean {
                data?.let {
                    return it.b > 0
                }
                return false
            }
        }

        val dialogC = buildDialog("C", "我是C")
        val nodeC = DialogTreeNode<Data1>(dialogC)

        nodeA.positiveNode = nodeB
        nodeA.negativeNode = nodeC

        nodeA.start()

    }

    private fun buildDialog(title: String, content: String): DialogNode {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(content)

        return DialogNode.create(builder, "是", "否")
    }
}
