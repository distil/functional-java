# functional-java
A library to ease functional programming in Java.

`functional-java` has borrowed alot of the interfaces and ideas from how `Rust` does it.
More importantly it adds mitigations to contain some of Java's shortcomings like breaking control flow with exceptions etc, and project it down to a more functional style.

Designed to also work with Android projects.

## Motivations
This is an example of how to fend off parts of Javas ordinary control-flow (returning null/IOException) by instead using `Result`.
By using `Result` you are consistent with "error"-handling and have more explicit control.

```java
import lang.NotNull;

import static functional.Result.okOrNullException;
import static functional.ThrowsAsResult.throwsAsResult;

public standardLoadJson(String filename) throws IOException {
    // The standard way of loading a json file.
}

@NotNull
public Result<JSONObject, IOException> loadJson(@NotNull String filename) {
    return throwsAsResult(
        () -> standardLoadJson("config.json"),
        IOException.class
    ).get()
}

public Result<String, Exception> getConfigured(String key) {
    return loadJson("config.json")
        .andThen(
            config -> okOrNullException(config.getString(key))
        );
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
Maven support is coming, for now just copy this into your project or have it as a submodule.
