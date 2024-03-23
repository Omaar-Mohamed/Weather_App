package com.example.weatherapp.alert.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CustomPopupLayoutBinding
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.favourit.view.MapActivity
import com.example.weatherapp.shared.ApiConstants
import java.util.Calendar
import java.util.concurrent.TimeUnit


class AlertFragment : Fragment() {

    lateinit var binding: FragmentAlertBinding
     var desiredDateTime: Calendar? = null
    lateinit var sharedPrefrence: SharedPreferences
    var locationLiveData: MutableLiveData<String> = MutableLiveData()


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
        val myCustomPopupDialogBinding = CustomPopupLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(myCustomPopupDialogBinding.root)

        // Set custom width and height for the dialog
        val width = context.resources.displayMetrics.widthPixels * 0.8 // 80% of screen width
        val height = WindowManager.LayoutParams.WRAP_CONTENT // Adjust height as needed
        locationLiveData.observe(viewLifecycleOwner) { location ->
            // Update the TextView's text inside the observer block
            myCustomPopupDialogBinding.selectedLocationTextView.text = location
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
            val delay =  desiredDateTime?.timeInMillis?.minus(System.currentTimeMillis())?.div(60000) ?: 0
            val inputData = Data.Builder()
                .putString("title", "Weather Alert")
                .putString("message", "It's going to rain today!")

                .build()
            val alertWorker = OneTimeWorkRequestBuilder<AlertWorker>()
                .setInputData(inputData)
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(requireContext()).enqueue(alertWorker)
            dialog.dismiss()
            Toast.makeText(context, "Alarm set at ${desiredDateTime?.time?:""}", Toast.LENGTH_SHORT).show()
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