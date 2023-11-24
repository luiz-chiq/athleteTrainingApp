package chiquetano.luiz.athletetrainingapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import chiquetano.luiz.athletetrainingapp.R
import chiquetano.luiz.athletetrainingapp.adapter.TrainingDayAdapter
import chiquetano.luiz.athletetrainingapp.controller.TrainingDayRtDbFbController
import chiquetano.luiz.athletetrainingapp.databinding.ActivityMainBinding
import chiquetano.luiz.athletetrainingapp.model.TrainingDay

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val trainingDayList: MutableList<TrainingDay> = mutableListOf()

    private val trainingDayController: TrainingDayRtDbFbController by lazy {
        TrainingDayRtDbFbController(this)
    }

    //Adapter
    private val trainingDayAdapter: TrainingDayAdapter by lazy {
        TrainingDayAdapter(
            this,
            trainingDayList
        )
    }

    companion object {
        const val GET_TRAINING_DAY_MSG = 1
        const val GET_TRAINING_DAY_INTERVAL = 2000L
    }

    val updateTrainingDayListHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)


            if (msg.what == GET_TRAINING_DAY_MSG) {

                trainingDayController.getTrainingDays()
                sendMessageDelayed(
                    obtainMessage().apply { what = GET_TRAINING_DAY_MSG },
                    GET_TRAINING_DAY_INTERVAL
                )
            }
            else {
                msg.data.getParcelableArray("TRAINING_ARRAY")?.also { array ->

                    trainingDayList.clear()
                    array.forEach {
                        trainingDayList.add(it as TrainingDay)
                    }

                    trainingDayAdapter.notifyDataSetChanged()
                }
            }

        }
    }


    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)
        amb.traininDaysLv.adapter=trainingDayAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if (result.resultCode == RESULT_OK){
                val trainingDay = result.data?.getParcelableExtra<TrainingDay>("EXTRA_TRAINING")
                trainingDay?.let { _trainingDay ->
                    if(trainingDayList.any { it.id == trainingDay.id }){
                        trainingDayController.editTrainingDay(_trainingDay)
                    }else {
                        trainingDayController.insertTrainingDay(_trainingDay)
                    }
                }
            }
        }

        amb.traininDaysLv.setOnItemClickListener{ parent, view, position, id->
            val trainingDay = trainingDayList[position]
            val viewTrainingDayIntent = Intent(this, TrainingDayActivity::class.java)
                .putExtra("EXTRA_TRAINING", trainingDay)
                .putExtra("VIEW_TRAINING",true)

            startActivity(viewTrainingDayIntent)
        }

        registerForContextMenu(amb.traininDaysLv)
        updateTrainingDayListHandler.apply {
            sendMessageDelayed(
                obtainMessage().apply { what = GET_TRAINING_DAY_MSG },
                GET_TRAINING_DAY_INTERVAL
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addTrainingMi -> {
                carl.launch(Intent(this,TrainingDayActivity::class.java))
                true
            }
            else -> true
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        val trainingDay = trainingDayList[position]

        return when (item.itemId){
            R.id.removeTrainingMi -> {
                trainingDayController.removeTrainingDay(trainingDay)
                Toast.makeText(this,"Removido", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editTrainingMi -> {
                val trainingToEdit = trainingDayList[position]
                val editTrainingIntent = Intent(this, TrainingDayActivity::class.java)
                editTrainingIntent.putExtra("EXTRA_CONTACT", trainingToEdit)
                carl.launch(editTrainingIntent)
                true
            }
            else -> {true}
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterForContextMenu(amb.traininDaysLv)
    }
}