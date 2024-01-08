package kz.flyingv.remindme

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import kz.flyingv.remindme.data.datastore.Database
import kz.flyingv.remindme.data.repository.*
import kz.flyingv.remindme.domain.usecase.AddReminderUseCase
import kz.flyingv.remindme.domain.usecase.CheckSchedulerUseCase
import kz.flyingv.remindme.domain.usecase.GetInstalledAppsUseCase
import kz.flyingv.remindme.domain.usecase.GetRemindersUseCase
import kz.flyingv.remindme.data.scheduler.RemindScheduler
import kz.flyingv.remindme.domain.usecase.DeleteReminderUseCase
import kz.flyingv.remindme.domain.usecase.GetReminderTimeUseCase
import kz.flyingv.remindme.domain.usecase.RescheduleUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ReminderApp: Application() {

    private val appModule = module {

        single { Room.databaseBuilder(androidContext(), Database::class.java, "Reminder.db").build() }
        single <SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

        single { RemindScheduler(androidContext()) }

        single<ReminderRepository> { ReminderRepositoryImpl() }
        single<SchedulerRepository> { SchedulerRepositoryImpl() }
        single<SystemRepository> { SystemRepositoryImpl() }

        factory { AddReminderUseCase() }
        factory { GetRemindersUseCase() }
        factory { DeleteReminderUseCase() }
        factory { GetInstalledAppsUseCase() }
        factory { RescheduleUseCase() }
        factory { CheckSchedulerUseCase() }
        factory { GetReminderTimeUseCase() }

    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@ReminderApp)
            modules(appModule)
        }

    }

}