package chiquetano.luiz.athletetrainingapp.model

interface TrainingDayDao {
    fun createTrainingDay(trainingDay: TrainingDay): Int
    fun getTrainingDay(id: Int): TrainingDay?
    fun getTrainingDays(): MutableList<TrainingDay>
    fun updateTrainingDay(trainingDay: TrainingDay): Int
    fun deleteTrainingDay(id: Int): Int
}