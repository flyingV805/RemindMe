package kz.flyingv.remindme.data.repository

import kz.flyingv.remindme.domain.entity.Reminder

interface FirebaseStoreRepository {

    suspend fun addToFirebaseStore(reminder: Reminder)
    suspend fun addToFirebaseStore(reminders: List<Reminder>)
    suspend fun getAllFromFirebaseStore(): List<Reminder>?

}