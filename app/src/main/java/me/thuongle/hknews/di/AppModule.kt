package me.thuongle.hknews.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import me.thuongle.hknews.AppSchedulerProvider
import me.thuongle.hknews.SchedulerProvider
import me.thuongle.hknews.data.db.HackerNewsDb
import me.thuongle.hknews.data.db.ItemDao
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideScheduleProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @Singleton
    fun profileDatabase(application: Application): HackerNewsDb {
        return Room
                .databaseBuilder(application, HackerNewsDb::class.java, "hknews.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    fun provideItemDao(db: HackerNewsDb): ItemDao {
        return db.itemDao()
    }
}
