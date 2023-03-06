package com.codegenetics.extensions.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.util.*

fun Context.getScreenWidth(percent: Double): Int {
    return (this.resources.displayMetrics.widthPixels * (percent / 100)).toInt()
}

fun Context.getScreenHeight(percent: Double): Int {
    return (this.resources.displayMetrics.heightPixels * (percent / 100)).toInt()
}

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

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

    // For 29 api or above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    // For below 29 api
    else {
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
            return true
        }
    }
    return false
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
        val intent = Intent(Intent.ACTION_SEND)
        val recipients = arrayOf(email)
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: ${appName}")
        intent.type = "text/plain"
        intent.setPackage("com.google.android.gm")
        startActivity(Intent.createChooser(intent, "Send mail"))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

/** Opens App in play store with package name
 * that you provide in params */
fun Context.openPlayStore(packageName: String) {
    try {
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

/** opens phone brower with the link
 * that you provide in params*/
fun Context.openBrowser(url: String) {
    try {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
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

/** opens chooser to share file
 * @param filePath
 * @param msg
 * default msg will be empty*/
fun Context.shareFile(filePath: String, msg: String = "") {
    val file = File(filePath)
    val share = Intent(Intent.ACTION_SEND)
    share.type = "*/*"
    val pdfUri: Uri?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        pdfUri = FileProvider.getUriForFile(this, packageName, file)
    } else {
        pdfUri = Uri.fromFile(file)
    }
    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    share.putExtra(Intent.EXTRA_TEXT, msg)
    share.putExtra(Intent.EXTRA_STREAM, pdfUri)
    startActivity(Intent.createChooser(share, "Share"))
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
                Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:$storeName")
            )
        )
    } catch (exception: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
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

/** @return string contains Manufacturer-Model-Ram*/
fun Context.getDeviceInfo(): String {
    return "(${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.SDK_INT}) - ${getMemoryStatusMegs().first}/${getMemoryStatusMegs().second}"

}

fun Context.getMemoryStatusMegs(): Pair<String, String> {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
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
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                callback(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
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
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
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
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(resource!!)
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
    width: Int ,
    height: Int,
    callback: (Drawable) -> Unit
) {
    try {
        if (isValidContext()) {
            Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback(resource!!)
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
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
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
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
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
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(deeplink)
        this.startActivity(intent)
    } catch (ex: Exception) {
    }
}

fun Context.openEmailIntent() {
    try {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        startActivity(intent)
    } catch (ex: Exception) {
    }
}


