package com.github.gfranks.collapsible.calendar.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.gfranks.collapsible.calendar.CollapsibleCalendarView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements CollapsibleCalendarView.Listener<Event> {

    private CollapsibleCalendarView mCalendarView;
    private ListView mListView;
    private EventListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendarView = (CollapsibleCalendarView) findViewById(R.id.calendar);
        mListView = (ListView) findViewById(R.id.calendar_event_list);

        mCalendarView.setMaxDate(LocalDate.now());
        mCalendarView.setMinDate(LocalDate.now().minusYears(1));
        mCalendarView.setListener(this);
        mCalendarView.addEvents(getEvents());
    }

    private List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        for (int i=0; i<20; i++) {
            events.add(new Event("Event " + (i+1), System.currentTimeMillis() - (86400000 * i)));
        }
        return events;
    }

    @Override
    public void onDateSelected(LocalDate date, List<Event> events) {
        if (mAdapter == null || mListView.getAdapter() == null) {
            mAdapter = new EventListAdapter(this, events);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setEvents(events);
        }
    }



    @Override
    public void onMonthChanged(LocalDate date) {
    }

    @Override
    public void onHeaderClick() {
        mCalendarView.toggle();
    }

    private class EventListAdapter extends ArrayAdapter<String> {

        public final DateTimeFormatter mTimeFormat = DateTimeFormat.forPattern(" h:mm a");
        private List<Event> mEvents;

        public EventListAdapter(Context context, List<Event> events) {
            super(context, android.R.layout.simple_list_item_1);
            mEvents = events;
        }

        public void setEvents(List<Event> events) {
            mEvents = events;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mEvents.size();
        }

        @Override
        public String getItem(int position) {
            Event event = mEvents.get(position);
            return mTimeFormat.print(event.getListCellTime()) + " - " + event.getTitle();
        }
    }
}
