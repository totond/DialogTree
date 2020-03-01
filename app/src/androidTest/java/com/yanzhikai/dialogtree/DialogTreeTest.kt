package com.yanzhikai.dialogtree

import android.app.AlertDialog
import android.app.Dialog
import com.yanzhikai.dialogtree.tree.DialogNode
import com.yanzhikai.dialogtree.tree.DialogTestUtil
import com.yanzhikai.dialogtree.tree.DialogTreeNode
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DialogTreeTest {

    @Test
    fun test(){
        val d = object : DialogNode<Boolean>(2){
            override fun buildDialog(data: Boolean): Dialog {
                return DialogNode.createDialog( mock(AlertDialog::class.java)!!, "yes", "no", this)
            }

        }
        val nodeA = DialogTreeNode<Boolean>(d, false, "A")

        assert("A".equals(DialogTestUtil.getOutputTrees(nodeA)[0]))
    }
}