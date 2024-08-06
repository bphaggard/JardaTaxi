package com.example.jardataxi.domain.model

import com.example.jardataxi.domain.Passenger
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject

class PassengerRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun addInput(dailyInput: Passenger) {
        try {
            db.collection("dailyInputs")
                .add(dailyInput)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    private suspend fun getTotalForWeek(field: String, startDate: Instant, endDate: Instant): Int {
//        return try {
//            val startTimestamp = Timestamp(startDate.epochSecond, startDate.nano)
//            val endTimestamp = Timestamp(endDate.epochSecond, endDate.nano)
//
//            val result: QuerySnapshot = db.collection("dailyInputs")
//                .whereGreaterThanOrEqualTo("date", startTimestamp)
//                .whereLessThanOrEqualTo("date", endTimestamp)
//                .orderBy("date", Query.Direction.ASCENDING)
//                .get()
//                .await()
//
//            result.documents.sumOf { it.getLong(field)?.toInt() ?: 0 }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            0
//        }
//    }

    suspend fun getPackaTotalForWeek(startDate: Instant, endDate: Instant): Int {
        return db.collection("dailyInputs")
            .whereGreaterThanOrEqualTo("date", Timestamp(startDate.epochSecond, startDate.nano))
            .whereLessThanOrEqualTo("date", Timestamp(endDate.epochSecond, endDate.nano))
            .get()
            .await()
            .documents
            .sumOf { it.getLong("packa")?.toInt() ?: 0 }
    }

    suspend fun getIgorTotalForWeek(startDate: Instant, endDate: Instant): Int {
        return db.collection("dailyInputs")
            .whereGreaterThanOrEqualTo("date", Timestamp(startDate.epochSecond, startDate.nano))
            .whereLessThanOrEqualTo("date", Timestamp(endDate.epochSecond, endDate.nano))
            .get()
            .await()
            .documents
            .sumOf { it.getLong("igor")?.toInt() ?: 0 }
    }

    suspend fun getPatrikTotalForWeek(startDate: Instant, endDate: Instant): Int {
        return db.collection("dailyInputs")
            .whereGreaterThanOrEqualTo("date", Timestamp(startDate.epochSecond, startDate.nano))
            .whereLessThanOrEqualTo("date", Timestamp(endDate.epochSecond, endDate.nano))
            .get()
            .await()
            .documents
            .sumOf { it.getLong("patrik")?.toInt() ?: 0 }
    }

    suspend fun deletePassengers() {
        try {
            val batch = db.batch()
            val collection = db.collection("dailyInputs")
            val snapshot = collection.get().await()
            for (document in snapshot.documents) {
                batch.delete(document.reference)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllPassengers(): Flow<List<Passenger>> = callbackFlow {
        val listenerRegistration = db.collection("dailyInputs")
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val dailyInputs = snapshot?.documents?.mapNotNull { it.toObject(Passenger::class.java) } ?: emptyList()
                trySend(dailyInputs)
            }
        awaitClose { listenerRegistration.remove() }
    }
}