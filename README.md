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
implementation 'com.github.y4n9b0:ttleosis:1.0.2'
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
D ttleosis: onActivityPostSaveInstanceState activity=y4n9b0.ttleosis.app.TtleosisReproduceActivity@fc1a9d2
D ttleosis: Bundle bytes=8184
D ttleosis: ├─ Bundle@android:viewHierarchyState bytes=984
D ttleosis: │  └─ SparseArray@android:views size=7
D ttleosis: ├─ Bundle@androidx.lifecycle.BundlableSavedStateRegistry.key bytes=6572
D ttleosis: │  ├─ Bundle@androidx:appcompat bytes=4
D ttleosis: │  ├─ Bundle@androidx.lifecycle.internal.SavedStateHandlesProvider bytes=4
D ttleosis: │  ├─ Bundle@android:support:activity-result bytes=2180
D ttleosis: │  │  ├─ ArrayList@KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS size=12
D ttleosis: │  │  ├─ ArrayList@KEY_COMPONENT_ACTIVITY_REGISTERED_RCS size=12
D ttleosis: │  │  ├─ Bundle@KEY_COMPONENT_ACTIVITY_PENDING_RESULT bytes=4
D ttleosis: │  │  └─ ArrayList@KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS size=0
D ttleosis: │  └─ Bundle@android:support:fragments bytes=4076
D ttleosis: │     └─ FragmentManagerState@android:support:fragments size=3
D ttleosis: │        ├─ FragmentState@com.ly.genjidialog.GenjiDialog(4598bdc9-3e22-4971-bb46-6b2dc56af298) bytes=1208
D ttleosis: │        ├─ FragmentState@com.ly.genjidialog.GenjiDialog(13d44e6c-49c7-48e5-8ad5-7830ddf71859) bytes=1208
D ttleosis: │        └─ FragmentState@com.ly.genjidialog.GenjiDialog(af4fdee8-e074-4ee8-b590-203a93f06958) bytes=1208
D ttleosis: ├─ Integer@android:lastAutofillId 
D ttleosis: └─ FragmentManagerState@android:fragments bytes=260
```

License
--------
Released under the WTFPL license. Please see the [LICENSE](https://github.com/y4n9b0/ttleosis/blob/master/LICENSE) file for more information.