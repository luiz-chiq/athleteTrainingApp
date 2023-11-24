package chiquetano.luiz.athletetrainingapp.controller

import android.os.Message
import chiquetano.luiz.athletetrainingapp.model.TrainingDay
import chiquetano.luiz.athletetrainingapp.model.TrainingDayDao
import chiquetano.luiz.athletetrainingapp.model.TrainingDayRtDbFb
import chiquetano.luiz.athletetrainingapp.view.MainActivity

class ContactRtDbFbController(private val mainActivity: MainActivity) {
    private val TrainingDayDaoImpl: TrainingDayDao = TrainingDayRtDbFb()

    fun insertContact(trainingDay: TrainingDay) {
        Thread {
            TrainingDayDaoImpl.createTrainingDay(trainingDay)
        }.start()
    }

    fun getContact(id: Int) = TrainingDayDaoImpl.getTrainingDay(id)

    fun getContacts() {
        Thread {
            val returnList = TrainingDayDaoImpl.getTrainingDays()

            mainActivity.updateContactListHandler.apply {
                sendMessage(Message().apply {
                    data.putParcelableArray(
                        "CONTACT_ARRAY",
                        returnList.toTypedArray()
                    )
                })
            }

        }.start()
    }

    fun editContact(trainingDay: TrainingDay){
        Thread {
            TrainingDayDaoImpl.updateTrainingDay(trainingDay)
        }.start()
    }

    fun removeContact(trainingDay: TrainingDay){
        Thread {
            trainingDay.id?.also {
                TrainingDayDaoImpl.deleteTrainingDay(it)
            }
        }.start()
    }
}