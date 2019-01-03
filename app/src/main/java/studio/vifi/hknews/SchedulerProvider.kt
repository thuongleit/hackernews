package studio.vifi.hknews

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ui(): Scheduler

    fun computation(): Scheduler

    fun newThread(): Scheduler

    fun io(): Scheduler
}
