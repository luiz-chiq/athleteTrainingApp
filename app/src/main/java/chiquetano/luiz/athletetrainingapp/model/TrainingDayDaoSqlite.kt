package chiquetano.luiz.athletetrainingapp.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import chiquetano.luiz.athletetrainingapp.R

class TrainingDayDaoSqlite(context: Context) : TrainingDayDao {

    companion object Constant {
        private const val TRAINING_DAY_DATABASE_FILE = "training_days"
        private const val TRAINING_DAY_TABLE = "training_day"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val WEIGHT_COLUMN = "weight"
        private const val AGE_COLUMN = "age"
        private const val DESCRIPTION_COLUMN = "description"
        private const val GENDER_COLUMN = "gender"
        private const val WEEK_DAY_COLUMN = "weekday"
        private const val CREATE_TRAINING_DAY_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TRAINING_DAY_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$WEIGHT_COLUMN REAL NOT NULL, " +
                    "$AGE_COLUMN INTEGER NOT NULL, " +
                    "$DESCRIPTION_COLUMN TEXT NOT NULL, " +
                    "$GENDER_COLUMN INTEGER NOT NULL, " +
                    "$WEEK_DAY_COLUMN INTEGER NOT NULL " +
                    ");"
    }

    private val trainingDaySqliteDatabase: SQLiteDatabase
    init {
        trainingDaySqliteDatabase =
            context.openOrCreateDatabase(TRAINING_DAY_DATABASE_FILE, MODE_PRIVATE, null)
        try {
            trainingDaySqliteDatabase.execSQL(CREATE_TRAINING_DAY_TABLE_STATEMENT)
        }catch (se: SQLException){
            Log.e(context.getString(R.string.app_name), se.message.toString())
        }
    }


    override fun createTrainingDay(trainingDay: TrainingDay) = trainingDaySqliteDatabase.insert(
        TRAINING_DAY_TABLE,
        null,
        trainingDay.toContentValues()
    ).toInt()


    override fun getTrainingDay(id: Int): TrainingDay? {
        val cursor = trainingDaySqliteDatabase.rawQuery(
            "SELECT * FROM $TRAINING_DAY_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )

        val trainingDay = if(cursor.moveToFirst()) cursor.rowToTrainingDay() else null
        cursor.close()
        return trainingDay
    }
    override fun getTrainingDays(): MutableList<TrainingDay> {
        val trainingDayList = mutableListOf<TrainingDay>()

        val cursor = trainingDaySqliteDatabase.rawQuery(
            "SELECT * FROM $TRAINING_DAY_TABLE ORDER BY $NAME_COLUMN",
            null
        )

        while (cursor.moveToNext()) {
            trainingDayList.add(cursor.rowToTrainingDay())
        }
        cursor.close()

        return trainingDayList
    }

    override fun updateTrainingDay(trainingDay: TrainingDay): Int = trainingDaySqliteDatabase.update(
        TRAINING_DAY_TABLE,
        trainingDay.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(trainingDay.id.toString())
    )

    override fun deleteTrainingDay(id: Int): Int = trainingDaySqliteDatabase.delete(
        TRAINING_DAY_TABLE,
        "$ID_COLUMN = ?",
        arrayOf(id.toString())
    )

    private fun Cursor.rowToTrainingDay(): TrainingDay = TrainingDay(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getDouble(getColumnIndexOrThrow(WEIGHT_COLUMN)),
        getInt(getColumnIndexOrThrow(AGE_COLUMN)),
        Gender.values()[getInt(getColumnIndexOrThrow(GENDER_COLUMN))],
        WeekDay.values()[getInt(getColumnIndexOrThrow(WEEK_DAY_COLUMN))],
        getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN))
    )

    private fun TrainingDay.toContentValues(): ContentValues = with(ContentValues()) {
        put(NAME_COLUMN, name)
        put(WEIGHT_COLUMN, weight)
        put(AGE_COLUMN, age)
        put(DESCRIPTION_COLUMN, description)
        put(GENDER_COLUMN, gender.ordinal)
        put(WEEK_DAY_COLUMN, weekDay.ordinal)
        this
    }
}