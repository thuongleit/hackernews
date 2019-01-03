package studio.vifi.hknews.di

import dagger.Module
import dagger.Provides
import studio.vifi.hknews.AppSchedulerProvider
import studio.vifi.hknews.SchedulerProvider

@Module
class AppModule {
    @Provides
    fun provideScheduleProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}
