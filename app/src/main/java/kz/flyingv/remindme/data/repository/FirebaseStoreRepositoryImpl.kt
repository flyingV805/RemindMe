package kz.flyingv.remindme.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kz.flyingv.remindme.data.datastore.mapper.ReminderActionMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderIconMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderTypeMapper
import kz.flyingv.remindme.data.datastore.model.ReminderDTO
import kz.flyingv.remindme.data.firebase.FirebaseMapper
import kz.flyingv.remindme.data.firebase.FirebaseReminderDTO
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseStoreRepositoryImpl: FirebaseStoreRepository, KoinComponent {

    private val firebaseDatabase: FirebaseDatabase by inject()
    private val firebaseAuth: FirebaseAuth by inject()

    override suspend fun addToFirebaseStore(reminder: Reminder) {
        val userIdentity = firebaseAuth.currentUser?.uid ?: "321"

        firebaseDatabase.getReference("users")
            .child(userIdentity)
            .child("reminds")
            .child(reminder.name)
            .setValue( FirebaseMapper.mapToFirebaseReminder(reminder) )
            .await()

    }

    override suspend fun addToFirebaseStore(reminders: List<Reminder>) {
        val userIdentity = firebaseAuth.currentUser?.uid ?: "321"

        val tableReference = firebaseDatabase.getReference("users")
            .child(userIdentity)
            .child("reminds")

        reminders.forEach {
            tableReference
                .child(it.name)
                .setValue( FirebaseMapper.mapToFirebaseReminder(it) )
                .await()
        }

    }

    override suspend fun getAllFromFirebaseStore(): List<Reminder>? {
        val userIdentity = firebaseAuth.currentUser?.uid ?: "321"

        val userReminds = firebaseDatabase.getReference("users")
            .child(userIdentity)
            .child("reminds")
            .get()
            .await()

        @Suppress("UNCHECKED_CAST")
        val remindsMapped = try{
            userReminds.value as? Map<String, HashMap<String, Any>>
        }catch (e: Exception){
            null
        }

        return remindsMapped?.entries?.filter{
            (it.value["markAsDeleted"] as? Boolean) != true
        }?.map {
            Log.i("firebase Key", "Mapped ${it.key}")
            Log.i("firebase value", "Mapped ${it.value}")
            Log.i("firebase value", "Mapped ${it.value.javaClass.name}")
            FirebaseMapper.mapFromFirebaseReminder(it.value)
        }

    }

    override suspend fun markAsDeleted(reminder: Reminder) {
        val userIdentity = firebaseAuth.currentUser?.uid ?: "321"

        firebaseDatabase.getReference("users")
            .child(userIdentity)
            .child("reminds")
            .child(reminder.name)
            .child("markAsDeleted")
            .setValue( true )
            .await()
    }


}