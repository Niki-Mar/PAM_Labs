package com.pam.lab2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDate, editTextStartTime, editTextTime, editTextNotes;
    private MaterialAutoCompleteTextView editTextReminder, editTextCategory;
    private MaterialButton buttonSave;
    private Calendar selectedDateTime = Calendar.getInstance();

    private boolean isEdit = false;
    private int editIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDate = findViewById(R.id.editTextDate);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextTime = findViewById(R.id.editTextTime);
        editTextNotes = findViewById(R.id.editTextNotes);
        editTextReminder = findViewById(R.id.editTextReminder);
        editTextCategory = findViewById(R.id.editTextCategory);
        buttonSave = findViewById(R.id.buttonSave);

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        editIndex = intent.getIntExtra("editIndex", -1);

        if (intent.hasExtra("selectedDate")) {
            long dateMillis = intent.getLongExtra("selectedDate", 0);
            selectedDateTime.setTimeInMillis(dateMillis);
            String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(dateMillis));
            editTextDate.setText(formattedDate);
        }

        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
                editTextDate.setText(formattedDate);
            }, year, month, day).show();
        });

        if (isEdit) {
            editTextTitle.setText(intent.getStringExtra("editEventTitle"));
            editTextDate.setText(intent.getStringExtra("editEventDate"));
            editTextStartTime.setText(intent.getStringExtra("editEventStartTime"));
            editTextTime.setText(intent.getStringExtra("editEventEndTime"));
            editTextReminder.setText(intent.getStringExtra("editEventReminder"));
            editTextNotes.setText(intent.getStringExtra("editEventNotes"));
            editTextCategory.setText(intent.getStringExtra("editEventCategory"));
        }

        editTextStartTime.setOnClickListener(v -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(selectedDateTime.get(Calendar.HOUR_OF_DAY))
                    .setMinute(selectedDateTime.get(Calendar.MINUTE))
                    .setTitleText("Выберите время начала")
                    .build();
            timePicker.show(getSupportFragmentManager(), "START_TIME_PICKER");
            timePicker.addOnPositiveButtonClickListener(dialog -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                selectedDateTime.set(Calendar.MINUTE, timePicker.getMinute());
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                editTextStartTime.setText(formattedTime);
            });
        });

        editTextTime.setOnClickListener(v -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(selectedDateTime.get(Calendar.HOUR_OF_DAY))
                    .setMinute(selectedDateTime.get(Calendar.MINUTE))
                    .setTitleText("Выберите время окончания")
                    .build();
            timePicker.show(getSupportFragmentManager(), "END_TIME_PICKER");
            timePicker.addOnPositiveButtonClickListener(dialog -> {
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                editTextTime.setText(formattedTime);
            });
        });

        String[] reminderOptions = {"5 сек", "5 минут", "10 минут", "15 минут", "30 минут", "1 час", "2 часа", "1 день", "2 дня"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reminderOptions);
        editTextReminder.setAdapter(adapter);
        editTextReminder.setOnClickListener(v -> editTextReminder.showDropDown());

        String[] categories = {"Work", "Study", "Personal", "Sport", "Meetings", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        editTextCategory.setAdapter(categoryAdapter);
        editTextCategory.setOnClickListener(v -> editTextCategory.showDropDown());

        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String date = editTextDate.getText().toString().trim();
            String startTime = editTextStartTime.getText().toString().trim();
            String endTime = editTextTime.getText().toString().trim();
            String reminder = editTextReminder.getText().toString().trim();
            String notes = editTextNotes.getText().toString().trim();
            String category = editTextCategory.getText().toString().trim();

            if (date.isEmpty()) {
                Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event(title, date, startTime, endTime, reminder, notes, category);
            List<Event> events = loadEventsFromXml();

            if (isEdit && editIndex >= 0 && editIndex < events.size()) {
                cancelReminder(editIndex);
                events.set(editIndex, event);
            } else {
                editIndex = events.size();
                events.add(event);
            }

            saveAllEventsToXml(events);
            scheduleReminder(event, editIndex);
            finish();
        });
    }

    private void scheduleReminder(Event event, int requestCode) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date eventDateTime = sdf.parse(event.date + " " + event.startTime);

            long reminderMillis = eventDateTime.getTime() - parseReminderOffset(event.reminder);
            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("title", event.title);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderMillis, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderMillis, pendingIntent);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cancelReminder(int requestCode) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Разрешение на точные напоминания не получено", Toast.LENGTH_LONG).show();
            }
        }
    }

    private long parseReminderOffset(String reminder) {
        if (reminder.contains("sek")) {
            int sec = Integer.parseInt(reminder.replaceAll("\\D+", ""));
            return sec * 1000L;
        } else if (reminder.contains("min")) {
            int min = Integer.parseInt(reminder.replaceAll("\\D+", ""));
            return min * 60 * 1000L;
        } else if (reminder.contains("hour")) {
            int hours = Integer.parseInt(reminder.replaceAll("\\D+", ""));
            return hours * 60 * 60 * 1000L;
        } else if (reminder.contains("day")) {
            int days = Integer.parseInt(reminder.replaceAll("\\D+", ""));
            return days * 24 * 60 * 60 * 1000L;
        }
        return 0;
    }

    private void saveAllEventsToXml(List<Event> events) {
        try {
            FileOutputStream fos = openFileOutput("events.xml", MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");

            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "events");

            for (Event event : events) {
                serializer.startTag(null, "event");

                serializer.startTag(null, "title");
                serializer.text(event.title);
                serializer.endTag(null, "title");
                serializer.startTag(null, "date");
                serializer.text(event.date);
                serializer.endTag(null, "date");
                serializer.startTag(null, "startTime");
                serializer.text(event.startTime);
                serializer.endTag(null, "startTime");
                serializer.startTag(null, "endTime");
                serializer.text(event.endTime);
                serializer.endTag(null, "endTime");
                serializer.startTag(null, "reminder");
                serializer.text(event.reminder);
                serializer.endTag(null, "reminder");
                serializer.startTag(null, "notes");
                serializer.text(event.notes);
                serializer.endTag(null, "notes");
                serializer.startTag(null, "category");
                serializer.text(event.category);
                serializer.endTag(null, "category");

                serializer.endTag(null, "event");
            }

            serializer.endTag(null, "events");
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Event> loadEventsFromXml() {
        List<Event> events = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("events.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, "UTF-8");

            int eventType = parser.getEventType();
            Event currentEvent = null;
            String currentTag = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if ("event".equals(currentTag)) {
                            currentEvent = new Event();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (currentEvent != null && currentTag != null) {
                            switch (currentTag) {
                                case "title":
                                    currentEvent.title = text;
                                    break;
                                case "date":
                                    currentEvent.date = text;
                                    break;
                                case "startTime":
                                    currentEvent.startTime = text;
                                    break;
                                case "endTime":
                                    currentEvent.endTime = text;
                                    break;
                                case "reminder":
                                    currentEvent.reminder = text;
                                    break;
                                case "notes":
                                    currentEvent.notes = text;
                                    break;
                                case "category":
                                    currentEvent.category = text;
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("event".equals(parser.getName()) && currentEvent != null) {
                            events.add(currentEvent);
                            currentEvent = null;
                        }
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }
            fis.close();
        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
}
