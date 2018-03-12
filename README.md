# functional-java
A library to ease functional programming in Java.

`functional-java` has borrowed alot of the interfaces and ideas from how `Rust` does it.
More importantly it adds mitigations to contain some of Java's shortcomings like breaking control flow with exceptions etc, and project it down to a more functional style.

Designed to also work with Android projects.

## Motivation


## Examples
### Option
```java
.unwrapOr(0);
```

### Result
```java
final Unary 
    .andThen()
    .andThen();
```

### Java Exceptions
```java
```

### Supplier / Reciever
```java
```

## Requirements
`functional-java` relies on lambdas and therefore requires java 8 or backporting with retrolambda or similar.

## Installation
Maven support is coming, for now just copy this into your project or have it as a submodule.
