package studio.vifi.hknews.model.usecase

import studio.vifi.hknews.MediatorLiveResult

abstract class MediatorUseCase<in P, R> {
    protected val result = MediatorLiveResult<R>()

    open fun observe(): MediatorLiveResult<R> {
        return result
    }

    abstract fun execute(parameters: P)
}
