# PieView
Android自定义饼图

![](http://upload-images.jianshu.io/upload_images/2746415-7b431f33211c7020.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## Installing

Users of your library will need add the jitpack.io repository:

```gradle
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

and:

```gradle
dependencies {
    compile 'com.github.zhouchupen:PieView:v1.0'
}
```

Note: do not add the jitpack.io repository under `buildscript` 

## Adding a sample app 

If you add a sample app to the same repo then your app needs to depend on the library. To do this in your app/build.gradle add a dependency in the form:

```gradle
dependencies {
    compile project(':library')
}
```

where 'library' is the name of your library module.

## Using

You may need this to use the pieview.  Put this into your xml file:
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.scnu.zhou.widget.PieView
        android:id="@+id/pieView"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_centerInParent="true"
        app:backgroundColor="#ffffff"
        app:textColor="#333333"
        app:startAngle="0"/>
</RelativeLayout>
```
And put this into your activity file:
```java
PieView pieView = (PieView) findViewById(R.id.pieView);

List<PieView.PieData> mData = new ArrayList<>();
PieView.PieData data1 = new PieView.PieData("blue", 50, Color.BLUE);
PieView.PieData data2 = new PieView.PieData("red", 30, Color.RED);
PieView.PieData data3 = new PieView.PieData("green", 30, Color.GREEN);
PieView.PieData data4 = new PieView.PieData("yellow", 60, Color.YELLOW);
PieView.PieData data5 = new PieView.PieData("black", 40, Color.BLACK);

mData.add(data1);
mData.add(data2);
mData.add(data3);
mData.add(data4);
mData.add(data5);

pieView.setData(mData);
```
