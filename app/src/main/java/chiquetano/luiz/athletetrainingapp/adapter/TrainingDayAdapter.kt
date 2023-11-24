package chiquetano.luiz.athletetrainingapp.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import chiquetano.luiz.athletetrainingapp.R
import chiquetano.luiz.athletetrainingapp.databinding.TileTrainingDayBinding
import chiquetano.luiz.athletetrainingapp.model.TrainingDay

class TrainingDayAdapter(
    context: Context,
    private val trainingDayList: MutableList<TrainingDay>
): ArrayAdapter<TrainingDay>(context, R.layout.tile_training_day, trainingDayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val trainingDay = trainingDayList[position]
        var trainingDayTileView = convertView
        var tcb: TileTrainingDayBinding?= null

        if(trainingDayTileView == null) {
            tcb = TileTrainingDayBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            trainingDayTileView = tcb.root
            val tileContactHolder = TileTrainingHolder(tcb.nameTv, tcb.weekDayTv)
            trainingDayTileView.tag = tileContactHolder
        }

        // as -> forma de fazer cast
        val holder = trainingDayTileView.tag as TileTrainingHolder
        holder.nameTv.setText(trainingDay.name)
        holder.emailTv.setText(trainingDay.weekDay.toString())


        return trainingDayTileView
    }

    private data class TileTrainingHolder(val nameTv: TextView, val emailTv: TextView)
}