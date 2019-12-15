package com.yanzhikai.dialogtree

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val nodeA = DialogTreeNode<Any>(dialogA)

        val dialogB = buildDialog("B", "我是B")
        val nodeB = DialogTreeNode<Any>(dialogB)

        val dialogC = buildDialog("C", "我是C")
        val nodeC = DialogTreeNode<Any>(dialogC)

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
