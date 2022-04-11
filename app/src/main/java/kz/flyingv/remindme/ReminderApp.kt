package kz.flyingv.remindme

import android.app.Application
import androidx.room.Room
import kz.flyingv.remindme.notifications.Notificator
import kz.flyingv.remindme.repository.RemindRepository
import kz.flyingv.remindme.repository.RemindRepositoryImpl
import kz.flyingv.remindme.room.Database
import kz.flyingv.remindme.scheduler.RemindScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ReminderApp: Application() {

    private val appModule = module {

        single { Room.databaseBuilder(androidContext(), Database::class.java, "Reminder.db").build() }
        single { RemindScheduler(androidContext())}

        single<RemindRepository> { RemindRepositoryImpl() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@ReminderApp)
            modules(appModule)
        }

    }

}