# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class top.yvyan.guettable.bean.** { *; }
-keep class top.yvyan.guettable.data.** { *; }
-keep class top.yvyan.guettable.Gson.** { *; }
-keep class top.yvyan.guettable.service.** { *; }
-keep class top.yvyan.guettable.activity.** { *; }
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class com.github.zfman.** { *; }
-keep interface com.github.zfman.** { *; }
-keep class com.zhuangfei.** { *; }
-keep interface com.zhuangfei.** { *; }
-keep class com.google.android.material.** { *; }
-keep interface com.google.android.material.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-keep class com.google.code.gson.** { *; }
-keep interface com.google.code.gson.** { *; }
-keep class junit.** { *; }
-keep interface junit.** { *; }
-keep class com.github.xuexiangjys.** { *; }
-keep interface com.github.xuexiangjys.** { *; }
-keep class com.github.bumptech.glide.** { *; }
-keep interface com.github.bumptech.glide.** { *; }
-keep class com.bumptech.** { *; }
-keep interface com.bumptech.** { *; }
-keep class com.github.yalantis.** { *; }
-keep interface com.github.yalantis.** { *; }
-keep class com.github.xuexiangjys.** { *; }
-keep interface com.github.xuexiangjys.** { *; }
-keep class com.zhy.** { *; }
-keep interface com.zhy.** { *; }
-keep class com.tencent.** { *; }
-keep interface com.tencent.** { *; }

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------


## Umeng
-keep class com.uc.** {*;}
-keep class com.zui.** {*;}
-keep class com.miui.** {*;}
-keep class com.heytap.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}
-dontwarn com.umeng.**
-keep class com.umeng.** {*;}
-keep class org.repackage.** {*;}
-keep class com.uyumao.** { *; }


-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class top.yvyan.guettable.R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static.** valueOf(java.lang.String);
}

