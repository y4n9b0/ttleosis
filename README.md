[![License](https://img.shields.io/badge/License-WTFPL-blue.svg)](https://github.com/y4n9b0/ttleosis/blob/master/LICENSE)
[![API](https://img.shields.io/badge/API-21%2B-blue.svg)](https://developer.android.com/about/versions/android-5.0.html)
[![JitPack](https://jitpack.io/v/y4n9b0/ttleosis.svg)](https://jitpack.io/#y4n9b0/ttleosis)

TtleOsis
--------
TransactionTooLargeException onSaveInstanceState since Android 12

* [Root launcher activities are no longer finished on Back press](https://developer.android.google.cn/about/versions/12/behavior-changes-all#back-press)
* [使用火山引擎 APMPlus 解决抖音Top 1 Java 崩溃的通用优化方案](https://juejin.cn/post/7306388118914973734?searchId=20240319120438D5D8E326DA58837413A9)

Requirements
--------
TtleOsis works on Android 5.0+ (API level 21+) and Java 8+.

Releases
--------
```groovy
implementation 'com.github.y4n9b0:ttleosis:1.0.0'
```

The latest release is available on [JitPack](https://jitpack.io/#y4n9b0/ttleosis),
don't forget add JitPack to maven repository.
```groovy
maven { url 'https://jitpack.io' }
```

Usage
--------
TtleOsis will take care of transaction data automatically, nothing else need to do.
But you really should find out why transaction data is so large.
Monitor logcat output to see transaction info of outState:
```shell
adb logcat -s ttleosis
```

Example logcat output:
```text
D ttleosis: onActivityPostSaveInstanceState activity=y4n9b0.ttleosis.app.TtleosisReproduceActivity@8f26a15
D ttleosis: Bundle parcelSize=7972
D ttleosis: ├─ Bundle@android:viewHierarchyState parcelSize=772
D ttleosis: │  └─ SparseArray@android:views size=6
D ttleosis: ├─ Bundle@androidx.lifecycle.BundlableSavedStateRegistry.key parcelSize=6572
D ttleosis: │  ├─ Bundle@androidx:appcompat parcelSize=4
D ttleosis: │  ├─ Bundle@androidx.lifecycle.internal.SavedStateHandlesProvider parcelSize=4
D ttleosis: │  ├─ Bundle@android:support:activity-result parcelSize=2180
D ttleosis: │  │  ├─ ArrayList@KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS size=12
D ttleosis: │  │  ├─ ArrayList@KEY_COMPONENT_ACTIVITY_REGISTERED_RCS size=12
D ttleosis: │  │  ├─ Bundle@KEY_COMPONENT_ACTIVITY_PENDING_RESULT parcelSize=4
D ttleosis: │  │  └─ ArrayList@KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS size=0
D ttleosis: │  └─ Bundle@android:support:fragments parcelSize=4076
D ttleosis: │     └─ FragmentManagerState@android:support:fragments size=3
D ttleosis: │        ├─ FragmentState@com.ly.genjidialog.GenjiDialog(86662b6c-f03a-408f-b394-28d074535130) parcelSize=1208
D ttleosis: │        ├─ FragmentState@com.ly.genjidialog.GenjiDialog(c43b800f-e7e0-466b-9f64-a4fc0085595d) parcelSize=1208
D ttleosis: │        └─ FragmentState@com.ly.genjidialog.GenjiDialog(db20cf61-7974-4793-ac46-7fc9d78d8c2c) parcelSize=1208
D ttleosis: ├─ Integer@android:lastAutofillId 
D ttleosis: └─ FragmentManagerState@android:fragments parcelSize=260
```

License
--------
Released under the WTFPL license. Please see the [LICENSE](https://github.com/y4n9b0/ttleosis/blob/master/LICENSE) file for more information.