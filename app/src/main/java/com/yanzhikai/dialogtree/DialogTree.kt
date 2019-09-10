package com.yanzhikai.dialogtree

/**
 *
 *
 * @author jacketyan
 * @date 2019/9/9
 */
class DialogTree<T>(var root: DialogTreeNode<T>)
{
    companion object
    {
        private const val TAG = "DialogTree"
    }

    var currentNode = root

    init
    {

    }

}