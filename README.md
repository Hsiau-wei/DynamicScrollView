# DynamicScrollView

A physics-based Android scrollview that provides a silky smooth feeling when you scroll it.

## Screenshot

![Screenshot](./images/screenshot1.gif)

The DynamicScrollView uses physics-based animation, which was released by Google in I/O '17. When you fling the content, the FlingAnimation will animate it smoothly at a comfortable rate. When its content scrolls to the edge of the wrapper, the DynamicScrollView helps to create an intuitive rebound effect.

![FlingAnimation](./images/screenshot2.gif)

![SpringAnimation](./images/screenshot3.gif)

The DynamicScrollAnimation also supports nested scrolling, so you can put some scroll views (EXCEPT DynamicScrollView ITSELF currently, otherwise it will result in some problems) into the content.

## Usage

Add dependencies to your app's `build.gradle`:

```groovy
dependencies {
	implementation 'me.tictok:dynamicscrollview:1.0.0'
}
```

Put the view in your layout:

```xml
<me.tictok.library.DynamicScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <!-- Put the child View/ViewGroup here. -->
    <!-- Note: ONLY ONE direct child is allowed. -->

</me.tictok.library.DynamicScrollView>
```

## Changelog

- 1.0.0 (Apr 03, 2019)
  - Initial Release

## License

Copyright 2019 Hsiau-wei Mu

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.