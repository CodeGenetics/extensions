package com.codegenetics.extensions.extension

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.UiModeManager
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.bluetooth.BluetoothManager
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.ALARM_SERVICE
import android.content.Context.APP_OPS_SERVICE
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.DEVICE_POLICY_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_MAIN
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_APP_EMAIL
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_STREAM
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.createChooser
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.codegenetics.extensions.colorCache
import com.codegenetics.extensions.drawableCache
import com.codegenetics.extensions.helper.Logs
import com.codegenetics.extensions.lib.R
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

fun Context.getScreenWidth(percent: Double): Int {
    return (this.resources.displayMetrics.widthPixels * (percent / 100)).toInt()
}

fun Context.getScreenHeight(percent: Double): Int {
    return (this.resources.displayMetrics.heightPixels * (percent / 100)).toInt()
}

fun Context.getScreenDimension(percentWidth: Double, percentHeight: Double): Pair<Int, Int> {
    val metrics = resources.displayMetrics
    val width = (metrics.widthPixels * (percentWidth / 100)).toInt()
    val height = (metrics.heightPixels * (percentHeight / 100)).toInt()
    return Pair(width, height)
}

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(@StringRes textId: Int, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, textId, duration).show() }

/**
 * Extension method to show toast for Context.
 */
fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun Context.toast(@StringRes id: Int) = toast(resources.getString(id))

fun Context.isRTL(): Boolean {
    val config = resources.configuration
    return config.layoutDirection == View.LAYOUT_DIRECTION_RTL
}

fun Context?.isValidContext(): Boolean {
    try {
        if (this == null) {
            return false
        }
        if (this is Activity) {
            val activity = this
            if (activity.isDestroyed || activity.isFinishing) {
                return false
            }
        }
        return true
    } catch (e: Exception) {
        return false
    }
}



inline fun <reified T : Any> Context.openActivity(vararg params: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    if (params.isNotEmpty()) fillIntentArguments(intent, params)
    startActivity(intent)
}



inline fun <reified T : Any> Context.getActivityIntent(vararg params: Pair<String, Any?>): Intent {
    val intent = Intent(this, T::class.java)
    if (params.isNotEmpty()) fillIntentArguments(intent, params)
    return intent
}

fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
    params.forEach {
        when (val value = it.second) {
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> {}
            }

            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> {}
        }
        return@forEach
    }
}

fun Context.isInternetConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    } else {
        connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

fun Context.getStringResourceByName(resName: String?): String {
    return try {
        val resId: Int = resources.getIdentifier(resName, "string", packageName)
        resources.getString(resId)
    } catch (ex: Exception) {
        ex.printStackTrace()
        "loading.."
    }
}

/** Opens Email Intent
 * @param email recipient to whom email should send
 * @param appName for the title of email*/
fun Context.sendFeedback(email: String, appName: String = "") {
    try {
        val intent = Intent(ACTION_SEND)
        val recipients = arrayOf(email)
        intent.putExtra(EXTRA_EMAIL, recipients)
        intent.putExtra(EXTRA_SUBJECT, "Feedback: ${appName}")
        intent.type = "text/plain"
        intent.setPackage("com.google.android.gm")
        startActivity(createChooser(intent, "Send mail"))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

/** Opens Email Intent*/
fun Context.openEmailApp(title: String, body: String, recipient: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$recipient")
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    // Check if there is an app that can handle this intent
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        // Handle the case where no email app is available
        // You could show a Toast or a Snackbar, for example
        toast("No email app found")
    }
}

/** Opens App in play store with package name
 * that you provide in params */
fun Context.openPlayStore(packageName: String = this.packageName) {
    try {
        // Try to open the Play Store app directly
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: Exception) {
        // If Play Store app is not available, open in a browser
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

/** opens phone brower with the link
 * that you provide in params*/
fun Context.openBrowser(url: String) {
    try {
        val intent = Intent()
        intent.action = ACTION_VIEW
        intent.addCategory(CATEGORY_BROWSABLE)
        intent.data = Uri.parse(url)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

@ColorInt
fun Context.color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}


@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
fun Context.shareText(title:String="Share", text: String="", receiver: Intent) {
    try {
        val intent = Intent(ACTION_SEND)
            intent.type = "text/plain"
        intent.putExtra(EXTRA_SUBJECT, text)
            val shareMsg = text.trim { it <= ' ' }
        intent.putExtra(EXTRA_TEXT, shareMsg)
            val pendingIntent = PendingIntent.getBroadcast(
                this@shareText, 0,
                receiver, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            startActivity(
                createChooser(
                    intent,
                    "Share link",
                    pendingIntent.intentSender
                )
            )
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}


fun Context.shareText(title: String = "Share", text: String = "") {
    try {
        val intent = Intent(ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(EXTRA_SUBJECT, text)
        val shareMsg = text.trim { it <= ' ' }
        intent.putExtra(EXTRA_TEXT, shareMsg)

        startActivity(
            createChooser(
                intent,
                title,
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/** opens chooser to share file
 * @param filePath
 * @param msg
 * default msg will be empty*/
fun Context.shareFile(filePath: String, msg: String = "") {
    val file = File(filePath)
    val share = Intent(ACTION_SEND)
    share.type = "*/*"
    val pdfUri: Uri?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        pdfUri = FileProvider.getUriForFile(this, packageName, file)
    } else {
        pdfUri = Uri.fromFile(file)
    }
    share.addFlags(FLAG_GRANT_READ_URI_PERMISSION)

    share.putExtra(EXTRA_TEXT, msg)
    share.putExtra(EXTRA_STREAM, pdfUri)
    startActivity(createChooser(share, "Share"))
}

@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String = Settings.Secure.getString(
    this.contentResolver, Settings.Secure.ANDROID_ID
)

/** Opens Google play Store with
 * store search
 * @param storeName
 */
fun Context.openStore(storeName: String) {
    try {
        startActivity(
            Intent(
                ACTION_VIEW, Uri.parse("market://search?q=pub:$storeName")
            )
        )
    } catch (exception: ActivityNotFoundException) {
        startActivity(
            Intent(
                ACTION_VIEW,
                Uri.parse("http://play.google.com/store/search?q=pub:$storeName")
            )
        )
    }
}


fun Context.getWhatsappPackage(): String {
    return if (isPackageInstalled("com.whatsapp")) "com.whatsapp"
    else if (isPackageInstalled("com.whatsapp.w4b")) "com.whatsapp.w4b"
    else if (isPackageInstalled("com.gbwhatsapp")) "com.gbwhatsap"
    else if (isPackageInstalled("com.lbe.parallel.intl")) "com.lbe.parallel.intl"
    else ""
}

fun Context.getFacebookPackage(): String {
    return if (isPackageInstalled("com.facebook.katana")) "com.facebook.katana"
    else if (isPackageInstalled("com.facebook.lite")) "com.facebook.lite"
    else if (isPackageInstalled("com.facebook.orca")) "com.facebook.orca"
    else if (isPackageInstalled("com.facebook.mlite")) "com.facebook.mlite"
    else ""
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.getGmailPackage(): String {
    return if (isPackageInstalled("com.google.android.gm")) "com.google.android.gm"
    else if (isPackageInstalled("com.google.android.gm.lite")) "com.google.android.gm.lite"
    else ""
}

fun Context.getSnapchatPackage(): String {
    return if (isPackageInstalled("com.snapchat.android")) "com.snapchat.android"
    else ""
}

fun Context.getInstagramPackage(): String {
    return if (isPackageInstalled("com.instagram.android")) "com.instagram.android"
    else if (isPackageInstalled("com.instagram.lite")) "com.instagram.lite"
    else ""
}

fun Context.getTwitterPackage(): String {
    return if (isPackageInstalled("com.twitter.android")) "com.twitter.android"
    else if (isPackageInstalled("com.twitter.android.lite")) "com.twitter.android.lite"
    else ""
}

/** converts drawable to Bitmap*/
fun Context.getBitmap(@DrawableRes drawable: Int): Bitmap {
    return BitmapFactory.decodeResource(
        resources, drawable
    )

}

/** @param listOf(Manifest.permission.POST_NOTIFICATIONS)*/
fun Context.requestPermission(permission: Collection<String>, callback: (Boolean) -> Unit) {
    Dexter.withContext(this).withPermissions(permission)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()
}

/**
 * @param [name] string
 * @return Drawable */
fun Context.getDrawableByName(name: String): Drawable? {
    val resources = this.resources
    val resourceId = resources.getIdentifier(name, "drawable", this.packageName)
    return if (resourceId != 0) {
        ResourcesCompat.getDrawable(resources, resourceId, null)
    } else {
        null
    }
}

/** @return string contains Manufacturer-Model-Ram*/
fun Context.getDeviceInfo(): String {
    return "(${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.SDK_INT}) - ${getMemoryStatusMegs().first}/${getMemoryStatusMegs().second}"

}

fun Context.getMemoryStatusMegs(): Pair<String, String> {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager?
    activityManager!!.getMemoryInfo(mi)

    val availableMegs = mi.availMem / 1048576L
    val totalMegs = mi.totalMem / 1048576L

    return Pair(availableMegs.toString(), totalMegs.toString())
}

fun Context.preLoadImageWithGlide(url: Any?, callback: (Boolean) -> Unit) {
    Glide.with(this)
        .load(url).dontAnimate()
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                callback(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                callback(true)
                return false
            }

        }).preload()
}

fun Context.preLoadImageWithGlide(urlList: List<Any>, callback: (Boolean) -> Unit) {
    var i = 1
    do {
        Glide.with(this)
            .load(urlList[i])
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(true)
                    i++
                    return false
                }

            }).preload()
    } while (i == urlList.size)

}

fun Context.getDrawable(
    url: String,
    callback: (Drawable) -> Unit
) {
    try {
        if (isValidContext()) {
            Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(resource)
                    return true
                }

            }).preload()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.getDrawable(
    url: String,
    width: Int,
    height: Int,
    callback: (Drawable) -> Unit
) {
    try {
        if (isValidContext()) {
            Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(resource)
                    return true
                }

            }).preload(width, height)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.getBitmap(url: String, callback: (Bitmap) -> Unit) {
    try {
        Glide.with(this).load(url)
            .listener(object : CustomTarget<Bitmap>(), RequestListener<Drawable> {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }


            }).preload()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.getBitmap(
    url: String,
    width: Int,
    height: Int,
    callback: (Bitmap) -> Unit
) {
    try {
        Glide.with(this).load(url)
            .listener(object : CustomTarget<Bitmap>(), RequestListener<Drawable> {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }


            }).preload(width, height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.getWindowSizePercentage(widthPercent: Double, heightPercent: Double): Pair<Int, Int> {
    val width = (this.resources.displayMetrics.widthPixels * (widthPercent / 100)).toInt()
    val height = (this.resources.displayMetrics.heightPixels * (heightPercent / 100)).toInt()
    return Pair(width, height)
}

fun Context.getColorResource(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

/** open youtube link in Youtube App*/
fun Context.handleYoutubeLink(deeplink: String?) {
    try {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse(deeplink)
        this.startActivity(intent)
    } catch (ex: Exception) {
    }
}

fun Context.openEmailIntent() {
    try {
        val intent = Intent(ACTION_MAIN)
        intent.addCategory(CATEGORY_APP_EMAIL)
        startActivity(intent)
    } catch (ex: Exception) {
    }
}

/**
 * Extension method to provide simpler access to {@link ContextCompat#getColor(int)}.
 */
@Deprecated(
    "Use {@link getColorResource or simple color}", ReplaceWith(
        "color(R.color.) or getColorResource(R.color)"
    )
)
fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

/**
 * Extension method to find a device width in pixels
 */
inline val Context.displayWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * Extension method to find a device height in pixels
 */
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * Extension method to get displayMetrics in Context.displayMetricks
 */
inline val Context.displayMetrics: DisplayMetrics
    get() = resources.displayMetrics

/**
 * Extension method to get LayoutInflater
 */
inline val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)

/**
 * Extension method to get a new Intent for an Activity class
 */
inline fun <reified T : Any> Context.intent() = Intent(this, T::class.java)

/**
 * Create an intent for [T] and apply a lambda on it
 */
inline fun <reified T : Any> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

/**
 * Extension method to startActivity for Context.
 */
inline fun <reified T : Activity> Context?.startActivity() =
    this?.startActivity(Intent(this, T::class.java))


/**
 * Extension method to start Service for Context.
 */
inline fun <reified T : Service> Context?.startService() =
    this?.startService(Intent(this, T::class.java))

/**
 * Extension method to startActivity with Animation for Context.
 */
inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = 0,
    exitResId: Int = 0
) {
    val intent = Intent(this, T::class.java)
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

/**
 * Extension method to startActivity with Animation for Context.
 */
inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = 0,
    exitResId: Int = 0,
    intentBody: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.intentBody()
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

/**
 * Extension method to Get Integer resource for Context.
 */
fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

/**
 * Extension method to Get Boolean resource for Context.
 */
fun Context.getBoolean(@BoolRes id: Int) = resources.getBoolean(id)


/**
 * Extension method to Get Drawable for resource for Context.
 */
fun Context.getDrawableResource(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * InflateLayout
 */
fun Context.inflateLayout(
    @LayoutRes layoutId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

/**
 * Extension method to get inputManager for Context.
 */
inline val Context.inputManager: InputMethodManager?
    get() = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

/**
 * Extension method to get notificationManager for Context.
 */
inline val Context.notificationManager: NotificationManager?
    get() = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager


/**
 * Extension method to get if the notifications enabled or not for Context.
 */
fun Context.areNotificationsEnabled(): Boolean {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager

    return if (notificationManager != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            Settings.Secure.getInt(contentResolver, "notification_enabled", 1) == 1
        }
    } else {
        false
    }
}

/**
 * Extension method to navigate user to Notification settings for Context.
 * for Android 11 or above add following in Manifest
 * Consider adding a <queries> declaration to your manifest when calling this method;
 *  @see <queries>
 *         <intent>
 *             <action android:name="android.settings.APP_NOTIFICATION_SETTINGS" />
 *         </intent>
 *     </queries>
 *
 */
fun Context.navigateToNotificationSettings() {
    val intent = Intent()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:$packageName")
        }

        else -> {
            intent.action = Settings.ACTION_APPLICATION_SETTINGS
        }
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }
}

/**
 * Extension method to get keyguardManager for Context.
 */
inline val Context.keyguardManager: KeyguardManager?
    get() = getSystemService(KEYGUARD_SERVICE) as? KeyguardManager

/**
 * Extension method to get telephonyManager for Context.
 */
inline val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(TELEPHONY_SERVICE) as? TelephonyManager

/**
 * Extension method to get devicePolicyManager for Context.
 */
inline val Context.devicePolicyManager: DevicePolicyManager?
    get() = getSystemService(DEVICE_POLICY_SERVICE) as? DevicePolicyManager

/**
 * Extension method to get connectivityManager for Context.
 */
inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager

/**
 * Extension method to get alarmManager for Context.
 */
inline val Context.alarmManager: AlarmManager?
    get() = getSystemService(ALARM_SERVICE) as? AlarmManager

/**
 * Extension method to get clipboardManager for Context.
 */
inline val Context.clipboardManager: ClipboardManager?
    get() = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager

/**
 * Extension method to get jobScheduler for Context.
 */
inline val Context.jobScheduler: JobScheduler?
    get() = getSystemService(JOB_SCHEDULER_SERVICE) as? JobScheduler

/**
 * Extension method to show notification for Context.
 */
inline fun Context.notification(body: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(this)
    builder.body()
    return builder.build()
}


/**
 * Extension method to share for Context.
 */
fun Context.share(text: String, subject: String = ""): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(EXTRA_SUBJECT, subject)
    intent.putExtra(EXTRA_TEXT, text)
    return try {
        startActivity(createChooser(intent, null))
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}

fun Context.shareContent(content: String, subject: String = "", filePath: String? = null) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = if (filePath != null) "*/*" else "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, content)
        filePath?.let {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this@shareContent, packageName, File(it))
            } else {
                Uri.fromFile(File(it))
            }
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    startActivity(Intent.createChooser(intent, "Share via"))
}

/**
 * Extension method to make call for Context.
 */
fun Context.makeCall(number: String): Boolean {
    return try {
        val intent = Intent(ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Extension method to Send SMS for Context.
 */
fun Context.sendSms(number: String, text: String = ""): Boolean {
    return try {
        Intent(ACTION_VIEW, Uri.parse("sms:$number")).apply {
            putExtra("sms_body", text)
            startActivity(this)
        }
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Extension method to browse for Context.
 */
fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    return try {
        Intent(ACTION_VIEW).apply {
            data = Uri.parse(url)
            if (newTask) addFlags(FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Extension method to rate app on PlayStore for Context.
 */
fun Context.rate(): Boolean =
    browse("market://details?id=$packageName") or browse("http://play.google.com/store/apps/details?id=$packageName")

/**
 * Extension method to provide quicker access to the [LayoutInflater] from [Context].
 */
fun Context.getLayoutInflater() =
    getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * Extension method to send sms for Context.
 */
fun Context.sms(phone: String?, body: String = "") {
    val smsToUri = Uri.parse("smsto:" + phone)
    val intent = Intent(ACTION_SENDTO, smsToUri)
    intent.putExtra("sms_body", body)
    startActivity(intent)
}

/**
 * Extension method to dail telephone number for Context.
 */
fun Context.dial(tel: String?) = startActivity(Intent(ACTION_DIAL, Uri.parse("tel:" + tel)))

/**
 * Extension method to get theme for Context.
 */
fun Context.isDarkTheme(): Boolean =
    resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Extension method to get bluetoothManager for Context.
 */
fun Context.bluetoothManager(): BluetoothManager {
    return getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
}

fun Context.locationManager(): LocationManager {
    return this.getSystemService(LOCATION_SERVICE) as LocationManager
}

/** Check if System's Location os On/Off*/
fun Context.isLocationEnabled(): Boolean {
    return locationManager().isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager().isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

/** Navigate to Setting screen*/
fun Context.navigateToLocationSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Context.isNotificationPermissionGranted(): Boolean {
    return isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)
}

/** Extension to fetch color from attribute with caching*/
fun Context.colorFromAttr(@AttrRes attrResId: Int): Int {
    try {
        return colorCache.get(attrResId) ?: run {
            val typedValue = TypedValue()
            if (theme.resolveAttribute(attrResId, typedValue, true)) {
                val color = typedValue.data
                colorCache.put(attrResId, color)
                color
            } else {
                return 0
            }
        }
    } catch (e: Exception) {
        return 0
    }
}

// Extension to fetch drawable from attribute with caching
fun Context.drawableFromAttr(@AttrRes attrResId: Int): Drawable? {
    try {
        return drawableCache.get(attrResId) ?: run {
            val typedValue = TypedValue()
            if (theme.resolveAttribute(attrResId, typedValue, true)) {
                val drawable = ContextCompat.getDrawable(this, typedValue.resourceId)
                if (drawable != null) {
                    drawableCache.put(attrResId, drawable)
                }
                drawable
            } else {
                null
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Context.getScreenSizeInDp(): Pair<Int, Int> {
    val metrics = this.resources.displayMetrics
    val widthDp = metrics.widthPixels / metrics.density
    val heightDp = metrics.heightPixels / metrics.density
    return Pair(widthDp.toInt(), heightDp.toInt())
}

fun Context.isAppInForeground(): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses ?: return false
    val packageName = applicationContext.packageName
    return appProcesses.any {
        it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && it.processName == packageName
    }
}

fun Context.startServicePlease(serviceIntent: Intent) {
    if (isAppInForeground()) {
        startService(serviceIntent)
    }
}

inline fun <reified T : Activity> Context.openActivityWithAnimation(
    intentBody: Intent.() -> Unit
) {
    val enterAnim = R.anim.slide_in_left
    val exitAnim = R.anim.slide_out_left
    val intent = Intent(this, T::class.java)
    intent.intentBody()
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterAnim, exitAnim).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

fun Context.isDarkModeEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android 10 (API level 29) and above
        val uiModeManager = this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    } else {
        // For below Android 10
        val currentNightMode =
            this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}

fun Context.printAdvertisingId() {
    CoroutineScope(Dispatchers.IO).launch {
        val idInfo: AdvertisingIdClient.Info

        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(this@printAdvertisingId)
            val advertisingId = idInfo.id
            Logs.e("AID", advertisingId.toString())
            //do sth with the id
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        }
    }
}

fun Context.openHotspotSettings() {
    try {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val cn = ComponentName("com.android.settings", "com.android.settings.TetherSettings")
        intent.setComponent(cn)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}


/**
 * Copies arbitrary text to the clipboard.
 *
 * @param text        The text to copy.
 * @param label       The clipboard label (default: “Copied Text”).
 * @param showToast   Whether to show a Toast on success/failure.
 * @return            True if the copy succeeded, false otherwise.
 */
fun Context.copyToClipboard(
    text: String,
    label: String = "Copied Text",
    showToast: Boolean = true
): Boolean {
    if (text.isBlank()) {
        if (showToast) Toast.makeText(this, "Nothing to copy", Toast.LENGTH_SHORT).show()
        return false
    }

    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    if (clipboard == null) {
        if (showToast) Toast.makeText(this, "Clipboard unavailable", Toast.LENGTH_SHORT).show()
        return false
    }

    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    if (showToast) toast("Text copied to clipboard")
    return true
}


/**
 * Copies this TextView’s text to the clipboard, with guards & a Toast confirmation.
 *
 * @param label  The clipboard label (default “Copied Text”).
 */
fun TextView.copyToClipboard(label: String = "Copied Text", showToast: Boolean) {
    val textToCopy = this.text.toString()
    if (textToCopy.isBlank()) {
        Toast.makeText(context, "Nothing to copy", Toast.LENGTH_SHORT).show()
        return
    }

    // Standard service lookup + safe cast
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    if (clipboard == null) {
        Toast.makeText(context, "Clipboard unavailable", Toast.LENGTH_SHORT).show()
        return
    }

    // Create clip and copy
    val clip = ClipData.newPlainText(label, textToCopy)
    clipboard.setPrimaryClip(clip)
    if (showToast)
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
}

fun Context.openSystemUpdatePage() {
    try {
        val intent = Intent("android.settings.SYSTEM_UPDATE_SETTINGS")
        startActivity(intent)
    } catch (e: SecurityException) {
        e.printStackTrace()
        // Log a more detailed message for better debugging
        Log.e("SystemUpdate", "Permission error while opening system update page: ${e.message}")

        // Fallback to general settings page if update settings cannot be accessed
        val fallbackIntent = Intent(Settings.ACTION_SETTINGS)
        startActivity(fallbackIntent)
    } catch (e: Exception) {
        e.printStackTrace()
        // Log a more general exception message
        Log.e("SystemUpdate", "Unexpected error: ${e.message}")

        // Fallback to general settings page
        val fallbackIntent = Intent(Settings.ACTION_SETTINGS)
        startActivity(fallbackIntent)
    }
}


fun Context.getIcon(packageName: String): Drawable? {
    return try {
        this.packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
       null
    }
}

fun Context.launchApp(packageName: String) {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
        startActivity(launchIntent)
    }
    else{
        "App Not Found"
    }
}

fun Context.uninstallApp(packageName: String) {
    val uninstallIntent = Intent(Intent.ACTION_DELETE).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(uninstallIntent)

}


/*************************************************************
 *******************DEPRECATED METHODS************************
 *************************************************************/


@Deprecated(
    "Use getColorResource", ReplaceWith(
        "getColorResource"
    )
)
fun Context.getColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

/**
 * Extension method to Get Drawable for resource for Context.
 */

@Deprecated("Use getDrawableResource", ReplaceWith("getDrawableResource"))
fun Context.getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)





