# numpadview &middot; [![](https://jitpack.io/v/yoanngoular/numpadview.svg)](https://jitpack.io/#yoanngoular/numpadview)
A nice and fully customizable Kotlin numeric keypad view for Android
 
<img src="https://media.giphy.com/media/bqsJUifrQ0tpniVmtr/giphy.gif" width="227" height="480" />
 
# Try it out
 
### Gradle dependency
Add the JitPack repository in your root build.gradle at the end of repositories
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency in your app build.gradle file
```groovy
implementation 'com.github.yoanngoular:numpadview:1.0.0'
```
 
### Maven dependency
Add the JitPack repository to your build file
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Add the dependency
```xml
<dependency>
  <groupId>com.github.yoanngoular</groupId>
  <artifactId>numpadview</artifactId>
  <version>1.0.0</version>
</dependency>
```
 
### Get Started
Just declare the custom view inside your xml :
```xml
<com.ygoular.numpadview.NumPadView
    android:id="@+id/num_pad_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    />
```
It should now look just like this.

<img width="227" height="480" align="center" src="https://i.ibb.co/4JxnGhM/device-2019-12-15-123125.png"/>
 
### Customize your keypad
 
There is a bunch of configurations that you can add to the declaration of the custom view inside your xml file.
```xml
  app:text_size="13sp"
  app:text_color="@color/gray_dark"
  app:text_style="italic"
  app:text_ripple_effect="false"
  app:font_path="/fonts/Monaco.ttf"
  app:left_icon="@drawable/ic_fingerprint"
  app:left_icon_tint="@color/red"
  app:left_icon_ripple_effect="true"
  app:drawable_right="false"
  app:background_resource="@drawable/numpad_bg"
```
The drawable_right and drawable_left properties defines whether or not there is an icon to set on the left and/or the right.
 
You can also decide to change configurations programmatically at runtime. In that case, use the proper setter from the following list.
 ```kotlin
  num_pad_view.setTextSize(13F)
              .setTextColor(R.color.gray_dark) // You can pass unresolved colors reference
              .setTextStyle(Typeface.ITALIC)
              .setTextRippleEffect(false)
              .setTextFont("fonts/Monaco.ttf")
              .setLeftIcon(R.drawable.ic_fingerprint) // You can pass unresolved drawable reference
              .setLeftIconTint(255, 255, 255) // You can pass RGB color format
              .setRightIcon(resources.getDrawable(R.drawable.ic_check, theme)) // You can pass resolved Drawable object
              .setRightIconTint("#000000") // You can pass hexa color format
              .setRightIconRippleEffect(false)
              .setBackgroundDrawableResource(R.drawable.numpad_bg) // You can set a custom Drawable background to the view
              .apply() // This method has to be called when changes have been made on the view's attributes
 
```
To restore defaults properties just use `restoreDefaults()` method as follow :
```kotlin
  num_pad_view.restoreDefaults() // This method already implicitly call apply() method so you don't need to call it
```
The library offers you different ways of implementing a custom listener for this view.
 
#### The Kotlin way
 
##### Implementing all 3 callback methods
```kotlin
  num_pad_view.setOnInteractionListener(
  
          onLeftIconClick = { /* Do some stuff here */ },
          onRightIconClick = { /* Do some stuff here */ },
          onNewValue = { /* Do some stuff here */ }
          
          )
```
##### Implementing onNewValue callback only
```kotlin
  num_pad_view.setOnInteractionListener {
      when (it) {
          NumPadView.DEFAULT_LEFT_ICON_VALUE -> { /* Do some stuff here */ }
          NumPadView.DEFAULT_RIGHT_ICON_VALUE -> { /* Do some stuff here */ }
          else -> { /* Do some stuff here */ }
      }
  }
```
or
```kotlin
  num_pad_view.setOnInteractionListener { value -> /* Do some stuff here */ }
```
By default, onLeftIconClick and onRightIconClick call onNewValue with their respective default value.
```kotlin
  DEFAULT_LEFT_ICON_VALUE = "LEFT"
  DEFAULT_RIGHT_ICON_VALUE = "RIGHT"
```
##### Implementing `NumPadView.OnNumPadInteractionListener`
```kotlin
  override fun onNewValue(value: String) { /* Do some stuff here */ }
```
Set your class as listener with
```kotlin
  num_pad_view.setOnInteractionListener(this)`
```
#### The Java way
```java
  numPadView.setOnInteractionListener(new NumPadView.OnNumPadInteractionListener() {
      @Override
      public void onRightIconClick() { /* Do some stuff here */ }
 
      @Override
      public void onLeftIconClick() { /* Do some stuff here */ }
 
      @Override
      public void onNewValue(@NotNull String value) { /* Do some stuff here */ }
  });
```
 
# Contributing
Do not hesitate to contribute if this is a useful library for you !
 
### Bug report? 
- If at all possible, please attach a *minimal* sample project or code which reproduces the bug. 
- Screenshots are also a huge help if the problem is visual.
### Send a pull request!
- If you're fixing a bug, please add a failing test or code that can reproduce the issue.
 
Please hit me up at ygoular@gmail.com for any feedback or issues you may encounter.
