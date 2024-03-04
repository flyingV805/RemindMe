package kz.flyingv.remindme

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kz.flyingv.remindme.data.datastore.Database
import kz.flyingv.remindme.data.repository.*
import kz.flyingv.remindme.data.scheduler.RemindScheduler
import kz.flyingv.remindme.domain.usecase.AddReminderUseCase
import kz.flyingv.remindme.domain.usecase.CheckSchedulerUseCase
import kz.flyingv.remindme.domain.usecase.GetInstalledAppsUseCase
import kz.flyingv.remindme.domain.usecase.GetRemindersUseCase
import kz.flyingv.remindme.data.scheduler.RemindSchedulerImplNew
import kz.flyingv.remindme.domain.usecase.DeleteReminderUseCase
import kz.flyingv.remindme.domain.usecase.GetCurrentUserUseCase
import kz.flyingv.remindme.domain.usecase.GetIsAlarmsPermittedUseCase
import kz.flyingv.remindme.domain.usecase.GetWorkerRemindersUseCase
import kz.flyingv.remindme.domain.usecase.GetReminderTimeUseCase
import kz.flyingv.remindme.domain.usecase.RescheduleUseCase
import kz.flyingv.remindme.domain.usecase.SearchRemindersUseCase
import kz.flyingv.remindme.domain.usecase.SignInUseCase
import kz.flyingv.remindme.domain.usecase.SignOutUseCase
import kz.flyingv.remindme.domain.usecase.SyncRemindsUseCase
import kz.flyingv.remindme.domain.usecase.UpdateLastShownUseCase
import kz.flyingv.remindme.domain.usecase.UpdateSchedulerUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ReminderApp: Application() {

    private val appModule = module {

        single<Context> { this@ReminderApp }

        single { Room.databaseBuilder(androidContext(), Database::class.java, "ReminderV2.rdb").build() }
        single <SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

        single<FirebaseAuth> { Firebase.auth }
        single<FirebaseDatabase>{ FirebaseDatabase.getInstance(BuildConfig.FB_DB_URL) }

        single<RemindScheduler> { RemindSchedulerImplNew(androidContext()) }

        single<ReminderRepository> { ReminderRepositoryImpl() }
        single<SchedulerRepository> { SchedulerRepositoryImpl() }
        single<SystemRepository> { SystemRepositoryImpl() }
        single<FirebaseAuthRepository> { FirebaseAuthRepositoryImpl() }
        single<FirebaseStoreRepository> { FirebaseStoreRepositoryImpl() }

        factory { GetIsAlarmsPermittedUseCase() }
        factory { AddReminderUseCase() }
        factory { GetRemindersUseCase() }
        factory { SearchRemindersUseCase() }
        factory { DeleteReminderUseCase() }
        factory { GetInstalledAppsUseCase() }
        factory { RescheduleUseCase() }
        factory { CheckSchedulerUseCase() }
        factory { GetReminderTimeUseCase() }
        factory { GetWorkerRemindersUseCase() }
        factory { UpdateSchedulerUseCase() }
        factory { UpdateLastShownUseCase() }

        factory { GetCurrentUserUseCase() }
        factory { SignInUseCase() }
        factory { SignOutUseCase() }
        factory { SyncRemindsUseCase() }


    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@ReminderApp)
            modules(appModule)
        }

    }

}