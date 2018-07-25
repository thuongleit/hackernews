package me.thuongle.hknews.di

import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import me.thuongle.hknews.App
import me.thuongle.hknews.AppSchedulerProvider
import me.thuongle.hknews.SchedulerProvider
import me.thuongle.hknews.db.HackerNewsDb
import me.thuongle.hknews.db.ItemDao
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideScheduleProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @Singleton
    fun profileDatabase(app: App): HackerNewsDb {
        return Room
                .databaseBuilder(app, HackerNewsDb::class.java, "hknews.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    @Singleton
    fun provideItemDao(db: HackerNewsDb): ItemDao {
        return db.itemDao()
    }
}
