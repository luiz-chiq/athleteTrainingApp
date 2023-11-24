package chiquetano.luiz.athletetrainingapp.model

import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class TrainingDayDaoRtDbFb: TrainingDayDao {
    companion object {
        private const val TRAINING_DAY_LIST_ROOT_NODE = "training"
    }

    private val trainingDayRtDbFbReference = Firebase.database
        .getReference(TRAINING_DAY_LIST_ROOT_NODE)

    // Simula uma consulta ao Realtime Database
    private val trainingDayList: MutableList<TrainingDay> = mutableListOf()

    init {
        trainingDayRtDbFbReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val trainingDay: TrainingDay? = snapshot.getValue<TrainingDay>()

                trainingDay?.also { newTrainingDay ->
                    if (!trainingDayList.any{ it.id == newTrainingDay.id }){
                        trainingDayList.add(newTrainingDay)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val trainingDay: TrainingDay? = snapshot.getValue<TrainingDay>()

                trainingDay?.also {editedTrainingDay ->
                    trainingDayList.indexOfFirst { editedTrainingDay.id == it.id }.also {
                        trainingDayList[it] = editedTrainingDay
                    }

                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val trainingDay: TrainingDay? = snapshot.getValue<TrainingDay>()

                trainingDay?.also {
                    trainingDayList.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // NSA
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })

        trainingDayRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trainingDayMap = snapshot.getValue<Map<String, TrainingDay>>()

                trainingDayList.clear()
                trainingDayMap?.values?.also {
                    trainingDayList.addAll(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // NSA
            }
        })
    }

    override fun createTrainingDay(trainingDay: TrainingDay): Int {
        createOrUpdateTrainingDay(trainingDay)
        return 1
    }

    override fun getTrainingDay(id: Int): TrainingDay? {
        return trainingDayList[trainingDayList.indexOfFirst { it.id == id }]
    }

    override fun getTrainingDays(): MutableList<TrainingDay> = trainingDayList

    override fun updateTrainingDay(trainingDay: TrainingDay): Int {
        createOrUpdateTrainingDay(trainingDay)
        return 1
    }

    override fun deleteTrainingDay(id: Int): Int {
        trainingDayRtDbFbReference.child(id.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateTrainingDay(trainingDay: TrainingDay) =
        trainingDayRtDbFbReference.child(trainingDay.id.toString()).setValue(trainingDay)
}