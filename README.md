CollapsibleCalendarView
===========

CollapsibleCalendarView is open source Android Library that allows developers to easily add calendar to
their app. CollapsibleCalendarView can be toggled between month and week view and allows adding events.
NOTE: Events are displayed via an indicator on the DayView cell. When a date is selected, you will receive
the events pertaining to that day in the listener's callback method.

What It Looks Like:
------------------

![Example screenshot](https://raw.githubusercontent.com/gfranks/CollapsibleCalendarView/develop/images/preview.png)

How To Use It:
-------------

### Basic Example

```java
public class MainActivity extends Activity implements CollapsibleCalendarView.Listener<Event> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CollapsibleCalendarView calendarView = (CollapsibleCalendarView) findViewById(R.id.calendar);
        calendarView.setListener(this);
        calendarView.addEvents(getEvents());
    }

    private List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        for (int i=0; i<20; i++) {
            events.add(new Event(System.currentTimeMillis() + (86400000 * i)));
        }
        return events;
    }

    @Override
    public void onDateSelected(LocalDate date, List<Event> events) {
        // do something with the day's events
    }

    @Override
    public void onMonthChanged(LocalDate date) {
    }

    @Override
    public void onHeaderClick() {
        // toggle between week and month view as needed
        mCalendarView.toggle();
    }
}

public class Event extends CollapsibleCalendarEvent {

    private long mDate;

    public Event(long date) {
        mDate = date;
    }

    @Override
    public long getTimeInMillis() {
        return mDate;
    }
}
```

XML Usage
----------------

```java
<com.github.gfranks.collapsible.calendar.CollapsibleCalendarView
    android:id="@+id/calendar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#48848F"
    android:paddingRight="5dp"
    android:paddingLeft="5dp"
    app:ccv_state="month"
    app:ccv_arrowColor="#FFFFFF"
    app:ccv_headerTextColor="#FFFFFF"
    app:ccv_weekDayTextColor="#FFFFFF"
    app:ccv_dayTextColor="#FFFFFF"
    app:ccv_eventIndicatorColor="#FFE18B"
    app:ccv_selectedDayBackgroundColor="#175662"
    app:ccv_allowStateChange="true"/>

```

Please see the sample project included in this repo for an example.

Customization:
----------------
* `ccv_state` The starting state of the Calender (May be `month` or `week`)
* `ccv_arrowColor` Color to be used the default arrow colors
* `ccv_prevArrowSrc` Custom resource for the previous arrow
* `ccv_nextArrowSrc` Custom resource for the next arrow
* `ccv_headerTextColor` Header (Month title) text color to be used
* `ccv_boldHeaderText` Bold header text
* `ccv_weekDayTextColor` Color of the week days displayed at the top of each column
* `ccv_boldWeekDayText` Bold week day text
* `ccv_dayTextColor` Color to be used when drawing each day view cell
* `ccv_eventIndicatorColor` Indicator color to be used when a day view cell has an event
* `ccv_selectedDayTextColor` Color to be set as the background of the currently selected day
* `ccv_selectedDayBackgroundColor` Color to be used for the text in the currently selected day view cell
* `ccv_smallHeader` Boolean determining if small header version should be shown over large version
* `ccv_noHeader` Boolean determining if header view should be shown
* `ccv_showInactiveDays` boolean determining if inactive (Days outside of the current viewing month) appear as inactive
* `ccv_allowStateChange` boolean determining if the Calendar state may be changed. Setting to false will disable scrolling only but toggling is still available
* `ccv_disableSwipe` boolean determining if swipes should be disabled

Listener Methods:
----------------

    public interface Listener<T extends CollapsibleCalendarEvent> {

        void onDateSelected(LocalDate date, List<T> events);

        void onMonthChanged(LocalDate date);

        void onHeaderClick();
    }

Installation:
------------

### Directly include source into your projects

- Simply copy the source/resource files from the library folder into your project.

### Use binary approach

- Follow these steps to include aar binary in your project:

    1: Copy com.github.gfranks.collapse.calendar-1.0.aar into your projects libs/ directory.

    2: Include the following either in your top level build.gradle file or your module specific one:
    ```
      repositories {
         flatDir {
             dirs 'libs'
         }
     }
    ```
    3: Under your dependencies for your main module's build.gradle file, you can reference that aar file like so:
    ```compile 'com.github.gfranks.collapse.calendar:com.github.gfranks.collapse.calendar-1.0@aar'```

License
-------
Copyright (c) 2015 Garrett Franks. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
