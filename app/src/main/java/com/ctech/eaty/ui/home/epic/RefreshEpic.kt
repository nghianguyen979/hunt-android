package com.ctech.eaty.ui.home.epic

import com.ctech.eaty.base.redux.Action
import com.ctech.eaty.base.redux.Epic
import com.ctech.eaty.repository.ProductRepository
import com.ctech.eaty.repository.createHomeNextBarCode
import com.ctech.eaty.ui.home.action.HomeAction
import com.ctech.eaty.ui.home.result.RefreshResult
import com.ctech.eaty.ui.home.state.HomeState
import com.ctech.eaty.ui.home.viewmodel.ProductItemViewModel
import com.ctech.eaty.util.rx.ThreadScheduler
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class RefreshEpic(private val productRepository: ProductRepository,
                  private val threadScheduler: ThreadScheduler) : Epic<HomeState> {

    override fun apply(action: PublishSubject<Action>, state: BehaviorSubject<HomeState>): Observable<RefreshResult> {
        return action.filter {
            it == HomeAction.REFRESH
        }.flatMap {
            productRepository.getHomePosts(createHomeNextBarCode(0), true)
                    .map {
                        it.products
                    }
                    .map {
                        RefreshResult.success(it.map { ProductItemViewModel(it) })
                    }
                    .onErrorReturn {
                        RefreshResult.fail(it)
                    }
                    .subscribeOn(threadScheduler.workerThread())
                    .startWith(RefreshResult.inProgress())
        }
    }
}