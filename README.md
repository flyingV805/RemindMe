# RemindMe
![image](https://github.com/flyingV805/RemindMe/blob/master/app/src/main/res/drawable-xhdpi/ic_splash.png)

Regular events reminder app

<a href="https://play.google.com/store/apps/details?id=kz.flyingv.remindme"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="64"></a>

## Tech-stack

Project takes advantages of MVI (UDF) approach for displaying immutable UI State, wich is can be modified in one place.

Min API level is set to 24, so the presented approach is suitable for over 94% of devices running Android. 
This project takes advantage of many popular libraries and tools of the Android ecosystem. 

-   [Jetpack](https://developer.android.com/jetpack):
    -   [Android KTX](https://developer.android.com/kotlin/ktx.html);
    -   [JetpackCompose](https://developer.android.com/jetpack/compose);
    -   [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle);
    -   [Room](https://developer.android.com/topic/libraries/architecture/room);
    -   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel);
-   [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html):
    -   [Coroutines Flow](https://kotlinlang.org/docs/reference/coroutines-overview.html);
-   [Koin](https://insert-koin.io/docs/quickstart/android)
-   [Lottie](https://github.com/airbnb/lottie-android)
