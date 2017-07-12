package me.thuongle.daggersample.view.main

import me.thuongle.daggersample.api.model.Item
import me.thuongle.daggersample.view.base.BasePresenter
import me.thuongle.daggersample.view.base.NetworkView

internal interface MainContract {

    interface Presenter : BasePresenter {

        fun loadMore()
    }

    interface View : NetworkView {

        fun onReceiveData(item: Item)
    }
}