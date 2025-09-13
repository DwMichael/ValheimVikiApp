# Comprehensive ProGuard rules for Valheim Viki App
# These rules prevent crashes in release builds by keeping necessary classes

# ============= General Android Rules =============
-keepattributes SourceFile,LineNumberTable
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions

# ============= Hilt Dependency Injection =============
# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keep class **_HiltModules$*Module { *; }
-keep class **_HiltComponents$* { *; }

# Keep @HiltAndroidApp and @AndroidEntryPoint classes
-keep @dagger.hilt.android.HiltAndroidApp class * {
    <init>(...);
}
-keep @dagger.hilt.android.AndroidEntryPoint class * {
    <init>(...);
}

# Keep classes annotated with @Module, @Component, etc.
-keep @dagger.Module class * { *; }
-keep @dagger.Component class * { *; }
-keep @dagger.Subcomponent class * { *; }
-keep @javax.inject.Singleton class * { *; }

# ============= Retrofit & OkHttp =============
# Keep Retrofit service interfaces
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Keep Retrofit annotations
-keepattributes RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Keep API service interfaces
-keep interface com.rabbitv.valheimviki.data.remote.api.** { *; }

# OkHttp platform calls common on JVM, Android, and iOS
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Additional network-related rules for HTTPS
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okio.**
-keep class okio.** { *; }

# ============= Gson JSON Serialization =============
# Keep Gson annotations
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# Keep data classes used for JSON serialization
-keep class com.rabbitv.valheimviki.domain.model.** { *; }
-keep class com.rabbitv.valheimviki.data.remote.dto.** { *; }

# Keep fields annotated with @SerializedName
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ============= Kotlinx Serialization =============
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.rabbitv.valheimviki.**$$serializer { *; }
-keepclassmembers class com.rabbitv.valheimviki.** {
    *** Companion;
}
-keepclasseswithmembers class com.rabbitv.valheimviki.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable classes
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>(...);
}

# ============= Room Database =============
# Keep Room annotations
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }

# Keep Room generated classes
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# ============= Jetpack Compose =============
# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }

# ============= Navigation Component =============
-keep class androidx.navigation.** { *; }
-keep class * extends androidx.fragment.app.Fragment{}

# ============= Work Manager =============
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker {
    <init>(android.content.Context, androidx.work.WorkerParameters);
}

# ============= Coil Image Loading =============
-keep class coil.** { *; }
-keep interface coil.** { *; }

# ============= Paging 3 =============
-keep class androidx.paging.** { *; }

# ============= DataStore =============
-keep class androidx.datastore.** { *; }

# ============= Security Crypto =============
-keep class androidx.security.crypto.** { *; }

# ============= Play Integrity =============
-keep class com.google.android.play.integrity.** { *; }

# ============= Play Core (In-App Updates) =============
-keep class com.google.android.play.core.** { *; }
-keep interface com.google.android.play.core.** { *; }
-keepclassmembers class com.google.android.play.core.** {
    <methods>;
    <fields>;
}

# Keep AppUpdateManager and related classes
-keep class com.google.android.play.core.appupdate.** { *; }
-keep class com.google.android.play.core.install.** { *; }
-keep class com.google.android.play.core.tasks.** { *; }

# ============= Application Specific Rules =============
# Keep BuildConfig - CRITICAL for network configuration (baseUrl)
-keep class com.rabbitv.valheimviki.BuildConfig { *; }

# Keep main application class
-keep class com.rabbitv.valheimviki.MainApplication { *; }
-keep class com.rabbitv.valheimviki.MainActivity { *; }

# Keep all model classes with their fields
-keep class com.rabbitv.valheimviki.domain.model.** { 
    <fields>; 
    <init>(...);
}

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep UseCase classes
-keep class com.rabbitv.valheimviki.domain.use_cases.** { *; }

# Keep Repository classes
-keep class com.rabbitv.valheimviki.data.repository.** { *; }

# ============= Debugging (keep for crash reports) =============
# Keep line numbers for debugging crashes
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile