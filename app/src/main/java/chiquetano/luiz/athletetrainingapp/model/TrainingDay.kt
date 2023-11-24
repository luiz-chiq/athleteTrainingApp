package chiquetano.luiz.athletetrainingapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
@Entity
data class TrainingDay(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = -1,
    @NonNull
    var name: String = "",
    @NonNull
    var weight: Double = 0.0,
    @NonNull
    var age: Int = 0,
    @NonNull
    var gender: Gender = Gender.UNKNOWN,
    @NonNull
    val weekDay: WeekDay = WeekDay.MONDAY,
    @NonNull
    var description: String = ""

) : Parcelable
