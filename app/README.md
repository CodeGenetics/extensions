
# Kotlin Extensions

## Installation

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
