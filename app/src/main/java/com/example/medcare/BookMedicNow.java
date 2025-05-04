package com.example.medcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BookMedicNow extends AppCompatActivity {

    private ImageView medicImageView;
    private TextView medicNameTextView, medicRoleTextView;
    private CalendarView calendarView;
    private TimePicker timePicker;
    private EditText notesEditText;
    private Button confirmButton;

    private String selectedDate;
    private int selectedHour, selectedMinute;

    private String medicId, medicName, medicRole, medicImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_medic_now);

        Toast.makeText(this, "BookMedicNow launched", Toast.LENGTH_SHORT).show();

        // Bind views
        medicImageView = findViewById(R.id.bookingMedicImage);
        medicNameTextView = findViewById(R.id.bookingMedicName);
        medicRoleTextView = findViewById(R.id.bookingMedicRole);
        calendarView = findViewById(R.id.bookingCalendar);
        timePicker = findViewById(R.id.bookingTimePicker);
        notesEditText = findViewById(R.id.bookingNotes);
        confirmButton = findViewById(R.id.confirmBookingButton);

        // Get data from Intent
        medicId = getIntent().getStringExtra("id");
        medicName = getIntent().getStringExtra("name");
        medicRole = getIntent().getStringExtra("role");
        medicImageUrl = getIntent().getStringExtra("imageUrl");

        // Debug Toasts
        Toast.makeText(this, "Received: ID=" + medicId + ", Name=" + medicName, Toast.LENGTH_LONG).show();

        if (medicId == null || medicName == null || medicImageUrl == null) {
            Toast.makeText(this, "Error: Missing data from Intent", Toast.LENGTH_LONG).show();
        }

        medicNameTextView.setText(medicName);
        medicRoleTextView.setText(medicRole);

        try {
            Glide.with(this).load(medicImageUrl).placeholder(R.drawable.boy).into(medicImageView);
        } catch (Exception e) {
            Toast.makeText(this, "Image load failed", Toast.LENGTH_SHORT).show();
        }

        // Handle calendar date
        Calendar calendar = Calendar.getInstance();
        selectedDate = getFormattedDate(calendar.getTimeInMillis());
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedDate = getFormattedDate(calendar.getTimeInMillis());
            Toast.makeText(this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();
        });

        // Get time from TimePicker (24-hour format)
        timePicker.setIs24HourView(true);
        selectedHour = timePicker.getHour();
        selectedMinute = timePicker.getMinute();
        Toast.makeText(this, "Default time: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();

        // Save time when changed
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;
            Toast.makeText(this, "Time changed: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
        });

        confirmButton.setOnClickListener(v -> {
            Toast.makeText(this, "Confirm button clicked", Toast.LENGTH_SHORT).show();

            String notes = notesEditText.getText().toString().trim();
            SharedPreferences prefs = getSharedPreferences("MedCarePrefs", MODE_PRIVATE);
            String userId = prefs.getString("userId", null);


            String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

            // Create a simple map for Firebase
            HashMap<String, Object> appointment = new HashMap<>();
            appointment.put("userId", userId);
            appointment.put("medicId", medicId);
            appointment.put("medicName", medicName);
            appointment.put("medicRole", medicRole);
            appointment.put("date", selectedDate);
            appointment.put("time", timeFormatted);
            appointment.put("notes", notes);

            Toast.makeText(this, "Booking: " + selectedDate + " " + timeFormatted, Toast.LENGTH_LONG).show();

            // Push to Firebase
            FirebaseDatabase
                    .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("appointments")
                    .push()
                    .setValue(appointment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    private String getFormattedDate(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(timeInMillis);
    }
}
