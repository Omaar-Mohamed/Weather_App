package com.example.weatherapp.alert.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatherapp.R
import com.example.weatherapp.alert.viewModel.AlertViewModel
import com.example.weatherapp.alert.viewModel.AlertViewModelFactory
import com.example.weatherapp.databinding.CustomPopupLayoutBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.favourit.view.MapActivity
import com.example.weatherapp.model.AppRepoImpl
import com.example.weatherapp.model.db.AlertDbState
import com.example.weatherapp.model.db.AppLocalDataSourseImpL
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.network.AppRemoteDataSourseImpl
import com.example.weatherapp.shared.ApiConstants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.log


class AlertFragment : Fragment() {

    lateinit var binding: FragmentAlertBinding
    lateinit var alartAdapter: AlartAdapter
    lateinit var alartViewModelFactory: AlertViewModelFactory
    lateinit var alartViewModel: AlertViewModel
    lateinit var alartLayoutManager: LinearLayoutManager
     var desiredDateTime: Calendar? = null
    lateinit var sharedPrefrence: SharedPreferences
    var locationLiveData: MutableLiveData<String> = MutableLiveData()
     var lastindex: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)

        return binding.root

    }



    override fun onResume() {
        super.onResume()
        val locationString = sharedPrefrence.getString("alertLocation", "Select Location")
        locationLiveData.value = locationString?: "Select Location"
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alartAdapter = AlartAdapter(
            action = {
                alartViewModel.deleteAlert(it)
                Toast.makeText(requireContext(), "Alert Deleted", Toast.LENGTH_SHORT).show()
            }
        )
        alartLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewAlert.layoutManager = alartLayoutManager
        binding.recyclerViewAlert.adapter = alartAdapter
        alartViewModelFactory = AlertViewModelFactory(
            AppRepoImpl.getInstance(
                AppRemoteDataSourseImpl, AppLocalDataSourseImpL.getInstance(requireContext())
            )
        )
        alartViewModel = ViewModelProvider(this, alartViewModelFactory).get(AlertViewModel::class.java)
        alartViewModel.getAllAlerts()
        lifecycleScope.launch {
            alartViewModel.alert.collect {
                when(it){
                    is AlertDbState.Success -> {
                        alartAdapter.submitList(it.data)
//                        if (it.data.isNotEmpty()){
//                            lastindex = it.data.last().id +1
//                         Log.i("alertsResult", "onCreateView: "+it.data.last().id)
//
//                        }else {
////                        lastindex = it.data.last().id +1
//                        }
                    }
                    is AlertDbState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AlertDbState.Failure -> {
//                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error: ${it.error}", Toast.LENGTH_SHORT).show()
                        Log.i("alertsResult", "onCreateView: "+it.error)
                    }

                    else -> {}
                }
            }
        }



        sharedPrefrence = requireContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        binding.fabAlert.setOnClickListener {
//            val delay = 1L // Delay in minutes
//            val inputData = Data.Builder()
//                .putString("title", "Weather Alert")
//                .putString("message", "It's going to rain today!")
//                .build()
//            val alertWorker = OneTimeWorkRequestBuilder<AlertWorker>()
//                .setInputData(inputData)
//                .setInitialDelay(delay, TimeUnit.MINUTES)
//                .build()
//            WorkManager.getInstance(requireContext()).enqueue(alertWorker)
            showCustomPopupDialog(requireContext())
        }
    }
    fun showCustomPopupDialog(context: Context) {
        val dialog = Dialog(context)
        var alartDto: AlertDto
        var lastInsertedDto : AlertDto = AlertDto(0,"","","")
        val myCustomPopupDialogBinding = CustomPopupLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(myCustomPopupDialogBinding.root)

        // Set custom width and height for the dialog
        val width = context.resources.displayMetrics.widthPixels * 0.8 // 80% of screen width
        val height = WindowManager.LayoutParams.WRAP_CONTENT // Adjust height as needed
        locationLiveData.observe(viewLifecycleOwner) { location ->
            // Update the TextView's text inside the observer block
            if (ApiConstants.alertlat != "" && ApiConstants.alartlon != "") {
                myCustomPopupDialogBinding.selectedLocationTextView.text = location
            } else {
                myCustomPopupDialogBinding.selectedLocationTextView.text = "Select Location"
            }

        }


        // Set dialog width and height
        dialog.window?.setLayout(width.toInt(), height.toInt())

//        myCustomPopupDialogBinding.selectedLocationTextView.text = "select Location"

        // Set click listeners for buttons
        myCustomPopupDialogBinding.locationimageButton.setOnClickListener {
            // Handle location image button click
            // Example: Start a new activity
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra("request_fragment", "Alert")
            context.startActivity(intent)

        }

        myCustomPopupDialogBinding.CalenderimageButton.setOnClickListener {
            // Handle calendar image button click to show date picker dialog
            showDatePickerDialog(myCustomPopupDialogBinding.selectedDateTextView, context)
        }

        myCustomPopupDialogBinding.CalenderTimeimageButton.setOnClickListener {
            // Handle calendar time image button click to show time picker dialog
            showTimePickerDialog(myCustomPopupDialogBinding.selectedTimeTextView, context)
        }
        myCustomPopupDialogBinding.confirmButton.setOnClickListener {
            // Handle confirm button click
            if (ApiConstants.alertlat == "" && ApiConstants.alartlon == "") {
                Toast.makeText(context, "Please select a location", Toast.LENGTH_SHORT).show()

            } else {
                alartDto = AlertDto(
                    0,
                    myCustomPopupDialogBinding.selectedLocationTextView.text.toString(),
                    myCustomPopupDialogBinding.selectedDateTextView.text.toString(),
                    myCustomPopupDialogBinding.selectedTimeTextView.text.toString()
                )

                lifecycleScope.launch {
                    val delay =
                        desiredDateTime?.timeInMillis?.minus(System.currentTimeMillis())?.div(60000)
                            ?: 0
                    val insertedId = alartViewModel.insertAlert(alartDto)
                    // Now you have the inserted ID
                    lastindex = insertedId
                    Log.i("lastIndexinInsert", "showCustomPopupDialog: ${lastindex}")
                    val inputData = Data.Builder()
                        .putString("title", "Weather Alert")
                        .putString("message", "It's going to rain today!")
                        .putString("alertLat", ApiConstants.alertlat)
                        .putString("alertLon", ApiConstants.alartlon)

                        .putLong("lastIndex" , lastindex)

                        .build()
                    val alertWorker = OneTimeWorkRequestBuilder<AlertWorker>()
                        .setInputData(inputData)
                        .setInitialDelay(delay, TimeUnit.MINUTES)
                        .build()
                    WorkManager.getInstance(requireContext()).enqueue(alertWorker)
                }

//                lifecycleScope.launch {
//                    alartViewModel.alert.collect {
//                        when (it) {
//                            is AlertDbState.Success -> {
//                                alartAdapter.submitList(it.data)
//                                if (it.data.isNotEmpty()) {
//                                    lastindex = it.data.last().id + 1
//                                    Log.i("alertsResult", "onCreateView: " + it.data.last().id)
//
//                                } else {
////                        lastindex = it.data.last().id +1
//                                }
//                            }
//
//                            else -> {}
//                        }
//                    }
//                }

                Log.i("lastIndex", "showCustomPopupDialog: "+lastindex)
//                val inputData = Data.Builder()
//                    .putString("title", "Weather Alert")
//                    .putString("message", "It's going to rain today!")
//                    .putString("alertLat", ApiConstants.alertlat)
//                    .putString("alertLon", ApiConstants.alartlon)
//
//                    .putLong("lastIndex" , lastindex)
//
//                    .build()
                Log.i(
                    "alertlonandlan",
                    "showCustomPopupDialog: " + ApiConstants.alertlat + " " + ApiConstants.alartlon
                )

//                val alertWorker = OneTimeWorkRequestBuilder<AlertWorker>()
//                    .setInputData(inputData)
//                    .setInitialDelay(delay, TimeUnit.MINUTES)
//                    .build()
//                WorkManager.getInstance(requireContext()).enqueue(alertWorker)



                Log.i("alartobjectid", "showCustomPopupDialog: " + alartDto.id)






//                 lifecycleScope.launch {
//                    alartViewModel.lastAlertInserted.collectLatest {
//                        when(it){
//                            is AlertDbState.Success -> {
//                                Log.i("lastInserted", "onCreateView: "+it.data)
//                                Toast.makeText(context, "Alert Inserted ${it.data[0]}" , Toast.LENGTH_SHORT).show()
//                                lastInsertedDto = it.data[0]
//                                Log.i("lastInsertedAlert", "showCustomPopupDialog: "+lastInsertedDto.id)
//
//                            }
//                            is AlertDbState.Loading -> {}
//                            is AlertDbState.Failure -> {}
//
//                            else -> {
//
//                            }
//                        }
//                    }
//                }

//                WorkManager.getInstance(context).getWorkInfoByIdLiveData(alertWorker.id)
//                    .observe(viewLifecycleOwner) { workInfo ->
//                        if (workInfo != null && workInfo.state.isFinished) {
//                            alartViewModel.deleteAlert(
//                                lastInsertedDto
//                            )
//                        }
//                    }
                dialog.dismiss()
                Toast.makeText(
                    context,
                    "Alarm set at ${desiredDateTime?.time ?: ""}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        dialog.show()
    }

    // Function to show the DatePickerDialog
    fun showDatePickerDialog(dateTextView: TextView, context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update desiredDateTime
                desiredDateTime = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay, 0, 0)
                }
                // Format the selected date
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateTextView.text = formattedDate
                // Show the TimePickerDialog after selecting the date

            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun showTimePickerDialog( TimeTextView: TextView,context: Context ) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->

                // Update desiredDateTime with the selected time
                desiredDateTime?.set(Calendar.HOUR_OF_DAY, selectedHour)
                desiredDateTime?.set(Calendar.MINUTE, selectedMinute)
                // Format the selected time
                val formattedTime = "$selectedHour:$selectedMinute"
                // Perform any action with the selected time, such as displaying it in a TextView
                // timeTextView.text = formattedTime
                TimeTextView.text = formattedTime
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

}