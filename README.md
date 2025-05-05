# PAM_Labs

========== Lab #1 ==========

Topic: UI View Model

Objectives:
To develop an application on one of the platforms established during Laboratory No. 0, using the appropriate development environment for it.

Purpose:
To present an application that runs on a device or emulator, which will include the following elements in its interface:

4 buttons (which will perform the actions described below)

1 TextBox (for input)

1 custom Object List (to display a list of items according to the requirement below)

Requirements:

Use UI components to implement the following functionalities:

Create a push notification on the device screen, which will be triggered after 10 seconds.

Use the device’s internal browser to initiate a Google search based on the keyword entered in the TextBox.

For the List Option/Entity View element, a theme of your choice must be used. The displayed elements in the application must meet the following conditions:

Head Title (a general name for the object)

Content Option (detailed content for the displayed item)

This is a simple task organizer Android app written in Java.
It supports adding, deleting, saving tasks, and searching them on Google.

=============================================

✨ Features
Add tasks
Delete tasks with confirmation
Store tasks between sessions (SharedPreferences + Gson)
Search task text on Google
RecyclerView-based list with dynamic "Add Task" button at the end
Clean modern structure with clear code and comments

📦 Technologies
Java
RecyclerView
SharedPreferences
Gson
AndroidX

🧠 How it works
Tasks are stored in ArrayList<String>
Tasks are saved as JSON string in SharedPreferences
RecyclerView shows all tasks + the Add button as the last item
Long press and confirmation dialog to delete
Search icon opens Google with the task as a query

📂 Project structure
MainActivity.java: main screen, data management
AddTaskActivity.java: form to create a new task
TaskAdapter.java: custom RecyclerView adapter
item_task.xml: layout for each task row
item_add_button.xml: layout for "Add Task" button at the end of list

🚀 How to run
Open the project in Android Studio
Connect an emulator or device
Click "Run"

========== Lab #2 ==========

Topic: Organiser Mobile Application (DAILY PLANNER)

Objective of the Laboratory Work

To develop an Organiser-type application on the selected platform.
During the creation and design of the application, strict points are introduced which must be followed.
The visual design of the application and the choice of API/Framework for Laboratory Work #2 are left to personal preference.
The components and structure of each Activity are described below.

UI Components

The application must include at least 3 basic Activities, which will be numbered in the report as follows:

1. MainActivity (structure/components)

Calendar View (custom or default)

Buttons (Add / Remove / Update)

Search (searches based on keywords)

2. AddActivity

Date/Time Picker

Info TextBox

Buttons

and others (optional, depending on the specifics of the app)

3. UpdateActivity – functionally the same as AddActivity, but with pre-filled data.

Data Storage

The operational data within the application will be stored in XML file(s),
the structure of which is at the developer’s discretion (keywords, XML Serialization).

Logical/Operational Component

All events and notification/signaling actions (sound/visual) in the Organiser
must be handled in a separate service, which will functionally extract data from the XML file.

Theme/Functionality (User-Defined)

Each user may choose the theme/functionality of their application.
Example of how the theme (specified in the report) can change:

Functionality: Class schedule planner
Theme: Student Organiser

Functionality: Medication schedule planner
Theme: Medication Reminder

=============================================

✨ Features
Add events with title, time, reminder, notes, and category
Edit and delete events with confirmation
Schedule notifications using AlarmManager
CalendarView for date selection
Color-coded categories for visual clarity
Persist events between sessions (XML + Serialization)
Search events by title
Clean architecture with structured code and clear comments

📦 Technologies
Java
XML serialization
AlarmManager
RecyclerView
Material Components
AndroidX

🧠 How it works
Events are stored as a list of Event objects
Events are serialized into an events.xml file
Reminders are scheduled using AlarmManager with PendingIntent
RecyclerView displays daily events filtered by selected date
Categories affect event card background color
Exact alarms require user-granted permission on Android 12+

📂 Project structure
MainActivity.java: calendar, search, and event list
AddEventActivity.java: form to create/edit events
ReminderReceiver.java: receives alarm broadcasts and shows notifications
EventAdapter.java: custom adapter for RecyclerView
item_event.xml: layout for each event card
activity_add_event.xml: layout for event form

🚀 How to run
Open project in Android Studio
Run on emulator or physical device (API 21+)
Grant permissions if prompted for notifications or exact alarms
