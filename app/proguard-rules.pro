-keepattributes SourceFile,LineNumberTable

##Annotations
-dontwarn javax.lang.model.**
-dontwarn javax.annotation.**
-dontwarn javax.tools.**
-dontwarn java.lang.invoke**
-dontwarn org.slf4j.**
-dontwarn rx.internal.util.**

## butter knife
-keep public class * implements butterknife.Unbinder { public <init>(...); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

## picasso
-dontwarn com.squareup.okhttp.**

## Timber
-dontwarn org.jetbrains.annotations.**

## Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.msgque.play.model.** { *; }

## Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

## google classes
-keep class com.google.**
-dontwarn com.google.**

## crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

##Dagger
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
        @javax.inject.* *;
        @dagger.* *;
        <init>();
    }
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection
