package com.hope.igb.savvy.common.views


interface ObservableViewMvc<LISTENER_TYPE> {


    fun registerListener(listener: LISTENER_TYPE)

    fun unregisterListener(listener: LISTENER_TYPE)



}