package com.wefika.calendar.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.manager.CalendarManager;

import org.joda.time.LocalDate;

public class MainActivity extends Activity implements OnClickListener {

    private CollapseCalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));

        mCalendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        mCalendarView.init(manager);

        findViewById(R.id.next_month).setOnClickListener(this);
        findViewById(R.id.toggle).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_month:
                nextMonth();
                break;
            case R.id.toggle:
                mCalendarView.toggle();
                break;
        }
    }

    private void nextMonth() {
        LocalDate nextMonth = mCalendarView.getManager().getSelectedDay().plusMonths(1);
        mCalendarView.selectDate(nextMonth);
    }
}
