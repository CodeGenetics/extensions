# Kotlin Extensions/Utils

## Whats New in 2.1.1:
## Whats New in 2.1.1:

```
HeavyLinearLayout
(To avoid Inconsistency Index of Bound Excception)
```

## Whats New in 2.1.0:

```
Coroutine Scopes for Activity,Fragment and ViewModel
launchMain{} [NEW]
launchDefault{} [NEW]
launchIO{} [NEW]
```

```
Context.getColor(@ColorRes id: Int) [DEPRICATED}
Context.color(@ColorRes id: Int) [DEPRICATED}
Context.getColorResource(@ColorRes id: Int) [ALTERNATIVE]
```

```
Context.getDrawable(@DrawableRes id: Int) [DEPRICATED}
Context.getDrawableResource(@DrawableRes id: Int) [ALTERNATIVE]
```

```
Context.isNotificationPermissionGranted() [NEW]
```

## Whats New 2.0.9:

```
NetworkConnectivityMonitor
```

## Usage

```
class MainActivity : AppCompatActivity(), NetworkConnectivityMonitor.NetworkStatusCallback {

    private lateinit var networkMonitor: NetworkConnectivityMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkMonitor = NetworkConnectivityMonitor(this)
        networkMonitor.startMonitoring(this)
    }

    override fun onNetworkAvailable() {
        // Handle network available
    }

    override fun onNetworkUnavailable() {
        // Handle network unavailable
    }

    override fun onDestroy() {
        networkMonitor.stopMonitoring()
        super.onDestroy()
    }
}

```

```
BaseAdapter
```

## Installation:

-Add it in your root build.gradle at the end of repositories:

```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

-Add the dependency

```
dependencies {
	        implementation 'com.github.CodeGenetics:extensions:latest_release'
	}
```

# Usage

## Context

```

openActivity<ClassName>(vararg params: Pair<String, Any?>)

toast("text")

isInternetConnected():Boolean

openPlayStore(packageName: String)

openBrowser(url: String)

openEmailIntent()

color(@ColorRes res: Int)

isValidContext(): Boolean

getScreenHeight(percent: Double): Int

getScreenWidth(percent: Double): Int

shareFile(filePath: String)

getDeviceId(): String

openStore(storeName: String)

getDrawableByName(name: String): Drawable?

requestPermission(permission: Collection<String>, callback: (Boolean) -> Unit)

getMemoryStatusMegs(): Pair<String, String>

preLoadImageWithGlide(url: Any?, callback: (Boolean) -> Unit)

Context.getDrawable(url: String,callback: (Drawable) -> Unit)

getBitmap(url: String, callback: (Bitmap) -> Unit)

displayWidth: Int

displayHeight: Int

displayMetrics: DisplayMetrics


```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.
