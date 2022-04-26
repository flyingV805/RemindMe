package kz.flyingv.remindme

import android.app.Application
import androidx.room.Room
import kz.flyingv.remindme.data.datastore.Database
import kz.flyingv.remindme.data.repository.*
import kz.flyingv.remindme.utils.scheduler.RemindScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ReminderApp: Application() {

    private val appModule = module {

        single { Room.databaseBuilder(androidContext(), Database::class.java, "Reminder.db").build() }
        single { RemindScheduler(androidContext())}

        single<ReminderRepository> { ReminderRepositoryImpl() }
        single<SchedulerRepository> { SchedulerRepositoryImpl() }
        single<SystemRepository> { SystemRepositoryImpl() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@ReminderApp)
            modules(appModule)
        }

    }

}