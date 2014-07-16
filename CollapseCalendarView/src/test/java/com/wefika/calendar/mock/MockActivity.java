package com.wefika.calendar.mock;

import android.app.Activity;
import android.os.Bundle;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.R;

/**
 * Created by Blaz Solar on 15/07/14.
 */
public class MockActivity extends Activity {

    CollapseCalendarView mCallendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wefika.calendar.test.R.layout.test_activity);

        mCallendarView = (CollapseCalendarView) findViewById(com.wefika.calendar.test.R.id.calendar);
    }

    public CollapseCalendarView getCallendarView() {
        return mCallendarView;
    }
}
