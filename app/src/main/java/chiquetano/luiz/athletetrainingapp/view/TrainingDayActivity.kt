package chiquetano.luiz.athletetrainingapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import chiquetano.luiz.athletetrainingapp.R
import chiquetano.luiz.athletetrainingapp.databinding.ActivityTrainingDayBinding
import chiquetano.luiz.athletetrainingapp.model.Gender
import chiquetano.luiz.athletetrainingapp.model.TrainingDay
import chiquetano.luiz.athletetrainingapp.model.WeekDay
import kotlin.random.Random

class TrainingDayActivity : AppCompatActivity() {
    private val acb: ActivityTrainingDayBinding by lazy {
        ActivityTrainingDayBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        setSupportActionBar(acb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Training day details"

        val receivedTraining = intent.getParcelableExtra<TrainingDay>("EXTRA_TRAINING")
        receivedTraining?.let {_receivedTraining ->
            val viewTraining: Boolean = intent.getBooleanExtra("VIEW_TRAINING", false)
            with(acb) {
                if (viewTraining) {
                    nameEt.isEnabled = false
                    weightEt.isEnabled = false
                    ageEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    saveBt.visibility = View.GONE
                }
                nameEt.setText(_receivedTraining.name)
                weightEt.setText(_receivedTraining.weight.toString())
                ageEt.setText(_receivedTraining.age.toString())
                descriptionEt.setText(_receivedTraining.description)

            }


        }

        with(acb) {
            saveBt.setOnClickListener {
                val trainingDay = TrainingDay(
                    id = receivedTraining?.id ?: generateId(),
                    name = nameEt.text.toString(),
                    weight = weightEt.text.toString().toDoubleOrNull() ?: 0.0,
                    age = ageEt.text.toString().toIntOrNull() ?: 0,
                    description = descriptionEt.text.toString(),
                    gender = when (genderRadioGroup.checkedRadioButtonId) {
                        R.id.maleRadioButton -> Gender.MALE
                        R.id.femaleRadioButton -> Gender.FEMALE
                        R.id.otherRadioButton -> Gender.OTHER
                        else -> Gender.UNKNOWN
                    },
                    weekDay = when (weekdayRadioGroup.checkedRadioButtonId) {
                        R.id.mondayRadioButton -> WeekDay.MONDAY
                        R.id.tuesdayRadioButton -> WeekDay.TUESDAY
                        R.id.wednesdayRadioButton -> WeekDay.WEDNESDAY
                        R.id.thursdayRadioButton -> WeekDay.THURSDAY
                        R.id.fridayRadioButton -> WeekDay.FRIDAY
                        R.id.saturdayRadioButton -> WeekDay.SATURDAY
                        else -> WeekDay.SUNDAY
                    }
                )

                val resultIntent = Intent()
                resultIntent.putExtra("EXTRA_TRAINING", trainingDay)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun generateId() = Random(System.currentTimeMillis()).nextInt()
}