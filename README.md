CollapseCalendarView
====================

CollapseCalendarView is open source Android Library that allows developers to easily add calendar to
their app. CollapseCalendarView can be toggled between month and week view. 

[![Build Status](https://travis-ci.org/blazsolar/android-collapse-calendar-view.svg?branch=develop)](https://travis-ci.org/blazsolar/android-collapse-calendar-view)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wefika/collapse-calendar-view/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wefika/collapse-calendar-view)

Example
-------
![Example screenshot](https://raw.githubusercontent.com/blazsolar/android-collapse-calendar-view/develop/images/preview.gif)

Source code with examples is included in repository.

Dependencies
------------
```groovy
dependencies {
    ...
    compile 'com.wefika:collapse-calendar-view:<version>'
    ...
}
```

Usage
-----
First you add view to your layout.
```xml
<com.wefika.calendar.CollapseCalendarView
        android:id="@+id/calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

and than you initialize it in your code.
```java
CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));

CollapseCalendarView calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
calendarView.init(manager);
```

License
-------
    The MIT License (MIT)
    
    Copyright (c) 2014 Blaž Šolar
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
