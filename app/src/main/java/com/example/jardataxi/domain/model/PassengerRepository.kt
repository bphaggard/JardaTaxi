package com.example.jardataxi.domain.model

import android.content.Context
import android.widget.Toast
import com.example.jardataxi.domain.Passenger
import com.example.jardataxi.domain.PassengersWeekValues
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class PassengerRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun addInput(dailyInput: Passenger, context: Context) {
        try {
            db.collection("dailyInputs")
                .add(dailyInput)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Nastala chyba při přidávání hodnoty. Zkuste to prosím znovu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun addWeekValue(weekValue: PassengersWeekValues, context: Context) {
        try {
            db.collection("weekValues")
                .add(weekValue)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Nastala chyba při přidávání hodnoty. Zkuste to prosím znovu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun checkIfValuesExistForWeek(week: Int): Boolean {
        val querySnapshot = db.collection("weekValues")
            .whereEqualTo("week", week)
            .get()
            .await()
        return !querySnapshot.isEmpty
    }

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

    suspend fun deleteWeekValues() {
        try {
            val batch = db.batch()
            val collection = db.collection("weekValues")
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

    fun getAllWeekValues(): Flow<List<PassengersWeekValues>> = callbackFlow {
        val listenerRegistration = db.collection("weekValues")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val weekValues = snapshot?.documents?.mapNotNull { it.toObject(PassengersWeekValues::class.java) } ?: emptyList()
                trySend(weekValues)
            }
        awaitClose { listenerRegistration.remove() }
    }
}