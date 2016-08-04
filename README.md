SwipeButton
===========

A widget that is enabled by circular swiping out of the center and above a given distance.

Gradle
------
```
dependencies {
    ...
    compile 'com.alessandromulloni.swipebutton:1.0.2'
}
```

Usage
-----
```xml
    <com.alessandromulloni.swipebutton.SwipeButton
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_gravity="bottom"
        app:background_button="@drawable/button1_button"
        app:background_target="@drawable/button1_target"
        app:background_finger_cancel="@drawable/button1_finger_cancel"
        app:background_finger_confirm="@drawable/button1_finger_confirm"
        app:animation_enter="@anim/button2_enter"
        app:animation_exit_cancel="@anim/button2_exit_cancel"
        app:animation_exit_confirm="@anim/button2_exit_confirm"
        app:src="@drawable/ic_close_black_24dp"
        />
```

Changelog
---------
* **1.0.2**
    * More custom parameters to control animations and backgrounds differently based on cancel/confirm status
* **1.0.1**
    * Parameters can be customized in XML from the layout editor
* **1.0.0**
    * Initial release

License
-------

The MIT License (MIT)

Copyright (c) 2016 Alessandro Mulloni

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
