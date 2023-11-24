package chiquetano.luiz.athletetrainingapp.model

import com.google.firebase.FirebaseApp;
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class TrainingDayRtDbFb: TrainingDayDao {
//    companion object {
//        private const val TRAINING_DAY_LIST_ROOT_NODE = "training"
//    }
//
//    private val trainingDayRtDbFbReference = Firebase.database
//        .getReference(TRAINING_DAY_LIST_ROOT_NODE)

    private var firebaseDatabase = FirebaseDatabase.getInstance("https://training-d6b90-default-rtdb.firebaseio.com/");
    private var databaseReference = firebaseDatabase.getReference("training");

    // Simula uma consulta ao Realtime Database
    private val trainingDayList: MutableList<TrainingDay> = mutableListOf()

    init {
        databaseReference.addChildEventListener(object: ChildEventListener {
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

        databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
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
        databaseReference.child(id.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateTrainingDay(trainingDay: TrainingDay) =
        databaseReference.child(trainingDay.id.toString()).setValue(trainingDay)
}