package com.pam.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Xml;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button buttonAdd;
    private EditText editTextSearch;
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> allEvents = new ArrayList<>();
    private long selectedDateMillis = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        buttonAdd = findViewById(R.id.buttonAdd);
        editTextSearch = findViewById(R.id.editTextSearch);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(new ArrayList<>(), new EventAdapter.OnEventActionListener() {
            @Override
            public void onEdit(Event event) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("editIndex", allEvents.indexOf(event));
                intent.putExtra("editEventTitle", event.title);
                intent.putExtra("editEventDate", event.date);
                intent.putExtra("editEventStartTime", event.startTime);
                intent.putExtra("editEventEndTime", event.endTime);
                intent.putExtra("editEventReminder", event.reminder);
                intent.putExtra("editEventNotes", event.notes);
                intent.putExtra("editEventCategory", event.category);
                startActivity(intent);
            }

            @Override
            public void onDelete(Event event) {
                allEvents.remove(event);
                saveAllEventsToXml();
                filterEvents();
            }
        });
        eventsRecyclerView.setAdapter(eventAdapter);

        updateEventList();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedDateMillis = calendar.getTimeInMillis();
            filterEvents();
        });

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            intent.putExtra("selectedDate", selectedDateMillis);
            startActivity(intent);
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventList();
    }

    private void updateEventList() {
        allEvents = loadEventsFromXml();
        filterEvents();
    }

    private void filterEvents() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String selectedDateStr = sdf.format(new Date(selectedDateMillis));

        String query = editTextSearch.getText().toString().trim().toLowerCase();

        List<Event> filtered = new ArrayList<>();
        for (Event e : allEvents) {
            boolean matchDate = e.date != null && e.date.equals(selectedDateStr);
            boolean matchQuery = e.title != null && e.title.toLowerCase().contains(query);
            if (matchDate && matchQuery) {
                filtered.add(e);
            }
        }

        eventAdapter.updateList(filtered);
    }

    private List<Event> loadEventsFromXml() {
        List<Event> events = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("events.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, "UTF-8");

            Event currentEvent = null;
            String tagName = "";
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if (tagName.equals("event")) {
                            currentEvent = new Event();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (currentEvent != null && tagName != null) {
                            switch (tagName) {
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
                        if (parser.getName().equals("event") && currentEvent != null) {
                            events.add(currentEvent);
                            currentEvent = null;
                        }
                        tagName = "";
                        break;
                }
                eventType = parser.next();
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    private void saveAllEventsToXml() {
        try {
            FileOutputStream fos = openFileOutput("events.xml", MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");

            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "events");

            for (Event event : allEvents) {
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
}
