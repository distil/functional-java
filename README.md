# functional-java
A library to ease functional programming in Java.

`functional-java` has borrowed alot of the interfaces and ideas from how `Rust` does it.
More importantly it adds mitigations to contain some of Java's shortcomings like breaking control flow with exceptions etc, and project it down to a more functional style.

Designed to also work with Android projects.

## Motivation
This is an example of how to fend off parts of Javas ordinary control-flow (returning null/throwing IOException) by instead using `Result`.
By using `Result` you are consistent with "error"-handling and have more explicit control.

```java
// LoadJson.java
import static functional.ThrowsAsResult.throwsAsResult;

JSONObject standardLoadJson(String filename) throws IOException {
    // The standard way of loading a json file.
}

public class LoadJson {
    public static Result<JSONObject, IOException> loadJson(String filename) {
        return throwsAsResult(
            () -> standardLoadJson("config.json"),
            IOException.class
        ).get()
    }
}


// main.java
import static functional.Result.okOrNullException;
import static LoadJson.loadJson;

public Result<String, Exception> getConfigured(String key) {
    return ok("config.json", Exception.class)
            .andThen(::loadJson)
            .andThen(config -> okOrNullException(config.getString(key)));
    }
}

public int main() {
    int width = getConfigured("width").unwrapOr(80);

    System.out.println("width = " + width);
}
```
If you wrap the parts in Java that missbehaves you can start writing readable java with minimal clutter.

This example reads the configurated `width` or defaults to `80`.

## Requirements
`functional-java` relies on lambdas and therefore requires java 8 or backporting with retrolambda or similar.

## Installation
### Gradle
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```gradle
dependencies {
        compile 'com.github.distil:functional-java:1.0'
}
```
