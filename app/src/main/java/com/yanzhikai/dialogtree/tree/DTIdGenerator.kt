package com.yanzhikai.dialogtree.tree

import java.util.concurrent.atomic.AtomicLong

/**
 *
 *
 * @author jacketyan
 * @date 2019/11/12
 */
class DTIdGenerator private constructor(){
    companion object {
        private const val TAG = "DTIdGenerator"

        val instance: DTIdGenerator by lazy { DTIdGenerator() }
    }

    /**
     * 原子操作器，里面存储下次要取的 id 的值
     */
    private val mNextIdAtomic = AtomicLong()

    /**
     * 生成 id。
     * 这是线程安全的。
     *
     * @return id 值
     */
    fun generate(): Long {
        return mNextIdAtomic.getAndAdd(1)
    }
}