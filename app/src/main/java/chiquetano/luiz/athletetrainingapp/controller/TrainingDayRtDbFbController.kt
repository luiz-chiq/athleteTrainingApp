package chiquetano.luiz.athletetrainingapp.controller

import android.os.Message
import chiquetano.luiz.athletetrainingapp.model.TrainingDay
import chiquetano.luiz.athletetrainingapp.model.TrainingDayDao
import chiquetano.luiz.athletetrainingapp.model.TrainingDayDaoRtDbFb
import chiquetano.luiz.athletetrainingapp.model.TrainingDayDaoSqlite
import chiquetano.luiz.athletetrainingapp.view.MainActivity

class TrainingDayRtDbFbController(private val mainActivity: MainActivity) {
    private val TrainingDayDaoImpl: TrainingDayDao = TrainingDayDaoSqlite(mainActivity)
//    private val TrainingDayDaoImpl: TrainingDayDao = TrainingDayDaoRtDbFb()

    fun insertTrainingDay(trainingDay: TrainingDay) {
        Thread {
            TrainingDayDaoImpl.createTrainingDay(trainingDay)
        }.start()
    }

    fun getTrainingDay(id: Int) = TrainingDayDaoImpl.getTrainingDay(id)

    fun getTrainingDays() {
        Thread {
            val returnList = TrainingDayDaoImpl.getTrainingDays()

            mainActivity.updateTrainingDayListHandler.apply {
                sendMessage(Message().apply {
                    data.putParcelableArray(
                        "TRAINING_ARRAY",
                        returnList.toTypedArray()
                    )
                })
            }

        }.start()
    }

    fun editTrainingDay(trainingDay: TrainingDay){
        Thread {
            TrainingDayDaoImpl.updateTrainingDay(trainingDay)
        }.start()
    }

    fun removeTrainingDay(trainingDay: TrainingDay){
        Thread {
            trainingDay.id?.also {
                TrainingDayDaoImpl.deleteTrainingDay(it)
            }
        }.start()
    }
}