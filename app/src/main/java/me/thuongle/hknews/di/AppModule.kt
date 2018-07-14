package me.thuongle.hknews.di

import dagger.Module
import dagger.Provides
import me.thuongle.hknews.AppSchedulerProvider
import me.thuongle.hknews.SchedulerProvider

@Module
class AppModule {
    @Provides
    fun provideScheduleProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}
