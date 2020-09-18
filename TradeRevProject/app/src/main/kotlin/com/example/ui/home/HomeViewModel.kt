package com.example.ui.home

import com.example.ui.base.BaseViewModel
import io.reactivex.subjects.PublishSubject

class HomeViewModel : BaseViewModel() {
    enum class ContextEvent {
        SCROLL_NEXT_PAGE_EVENT,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()
}
