package com.example.medcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BookMedicNow extends AppCompatActivity {

    private ImageView medicImageView;
    private TextView medicNameTextView, medicRoleTextView;
    private CalendarView calendarView;
    private EditText locationEditText;

    private TimePicker timePicker;
    private EditText notesEditText;
    private Button confirmButton;
    private static final int REQUEST_MAP_PICKER = 101;


    private String selectedDate;
    private int selectedHour, selectedMinute;

    private String medicId, medicName, medicRole, medicImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_medic_now);

        Toast.makeText(this, "BookMedicNow launched", Toast.LENGTH_SHORT).show();

        locationEditText = findViewById(R.id.bookingLocationEditText);
        Button locationPickerButton = findViewById(R.id.pickLocationBtn);

        locationPickerButton.setOnClickListener(v -> {
            Intent intent = new Intent(BookMedicNow.this, MapPickerActivity.class);
            startActivityForResult(intent, REQUEST_MAP_PICKER);
        });


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

        });

        // Get time from TimePicker (24-hour format)
        timePicker.setIs24HourView(true);
        selectedHour = timePicker.getHour();
        selectedMinute = timePicker.getMinute();


        // Save time when changed
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;

        });

        confirmButton.setOnClickListener(v -> {

            String notes = notesEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("MedCarePrefs", MODE_PRIVATE);
            String userId = prefs.getString("userId", null);

            String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

            DatabaseReference usersRef = FirebaseDatabase
                    .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Users");

            usersRef.child(userId).get().addOnSuccessListener(userSnapshot -> {
                String userName = userSnapshot.child("name").getValue(String.class);

                // Now fetch medic fee
                DatabaseReference medicRef = FirebaseDatabase
                        .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Medics")
                        .child(medicId);

                medicRef.get().addOnSuccessListener(medicSnapshot -> {
                    Long fee = medicSnapshot.child("fee").getValue(Long.class);
                    if (fee == null) fee = 0L;

                    DatabaseReference appointmentRef = FirebaseDatabase
                            .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("appointments")
                            .push();

                    String appointmentId = appointmentRef.getKey();

                    Appointment appointment = new Appointment();
                    appointment.setId(appointmentId);
                    appointment.setUserId(userId);
                    appointment.setUserName(userName);
                    appointment.setMedicId(medicId);
                    appointment.setMedicName(medicName);
                    appointment.setProfileImageUrl(medicImageUrl);
                    appointment.setDate(selectedDate);
                    appointment.setTime(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                    appointment.setLocation(locationEditText.getText().toString().trim());
                    appointment.setNotes(notesEditText.getText().toString().trim());
                    appointment.setFees(fee.intValue()); // ✅ set actual fee
                    appointment.setStatus("pending");

                    appointmentRef.setValue(appointment)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Appointment booked!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch medic fee", Toast.LENGTH_SHORT).show();
                });

            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to fetch user name", Toast.LENGTH_SHORT).show();
            });
        });

        }
            private String getFormattedDate ( long timeInMillis){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                return sdf.format(timeInMillis);
            }

            @Override
            protected void onActivityResult ( int requestCode, int resultCode, Intent data){
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == REQUEST_MAP_PICKER && resultCode == RESULT_OK) {
                    String selectedLocation = data.getStringExtra("location");
                    locationEditText.setText(selectedLocation);
                }
            }
}