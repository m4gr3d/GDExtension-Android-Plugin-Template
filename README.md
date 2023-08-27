# GDExtension Android Plugin Template
This repository serves as a quickstart template for building a GDExtension Android plugin for Godot 
4.2+.

## Contents
* [godot-cpp](godot-cpp) submodule for the GDExtension C++ bindings
* An illustrative simple Godot project: [`plugin/demo`](plugin/demo)
* Preconfigured gradle build file to build and package the contents for the Android plugin: 
  [`plugin/build.gradle.kts`](plugin/build.gradle.kts)
* Preconfigured export scripts template: 
  [`plugin/export_scripts_template`](plugin/export_scripts_template)
* Preconfigured manifest for the Android plugin:
  [`plugin/src/main/AndroidManifest.xml`](plugin/src/main/AndroidManifest.xml)
* Preconfigured source files for the Kotlin/Java logic of the Android plugin: 
  [`plugin/src/main/java`](plugin/src/main/java)
* Preconfigured source files for the C++ logic of the plugin: [`plugin/src/main/cpp`](plugin/src/main/cpp)

## Usage
**Note:** [Android Studio](https://developer.android.com/studio) is the recommended IDE for 
developing Godot Android plugins. You can install the latest version from https://developer.
android.com/studio.

To use this template, log in to github and click the green "Use this template" button at the top of the repository page.
This will let you create a copy of this repository with a clean git history.

Once the project is cloned to your local machine, run the following command in the project root 
directory to initialize the `godot-cpp` submodule:
```
git submodule update --init
```

### Building the C++ bindings
Build the Android C++ bindings using the following commands. To speed up compilation, add `-jN` at 
the end of the SCons command line where `N` is the number of CPU threads you have on your system.
The example below uses 4 threads.
```
cd godot-cpp
scons platform=android target=template_debug -j4
scons platform=android target=template_release -j4
```

When the command is completed, you should have static libraries stored in `godot-cpp/bin` that 
will be used for compilation by the plugin.

### Configuring the template
After cloning your own copy to your local machine, configure the project as needed. Several 
`TODO` have been added to the project to help identify where changes are needed; here's an 
overview of the minimum set of modifications needed:
* Update the name of the Android plugin. Note that the name should not contain any spaces:
  * Open [`settings.gradle.kts`](settings.gradle.kts) and update the value for `rootProject.name`
  * Open [`plugin/build.gradle.kts`](plugin/build.gradle.kts) and update the value for `pluginName`
  * Open [`plugin/CMakeLists.txt`](plugin/CMakeLists.txt) and update the value for the CMake project
  * Open [`plugin/export_scripts_template/plugin.cfg`](plugin/export_scripts_template/plugin.cfg)
    and update the value for `name`
  * Open [`plugin/export_scripts_template/.export/editor_export_plugin.gd`](plugin/export_scripts_template/.export/editor_export_plugin.gd)
    and update the value for `_plugin_name`
* Update the package name of the Android plugin:
  * Open [`plugin/build.gradle.kts`](plugin/build.gradle.kts) and update the value for `pluginPackageName`
  * Make sure subdirectories under [`plugin/src/main/java`](plugin/src/main/java) match the 
    updated package name
  * Make sure that `package` at the top of [`GDExtensionAndroidPlugin.kt`](plugin/src/main/java/org/godotengine/plugin/android/gdextension/template/GDExtensionAndroidPlugin.kt)
    matches the updated package name
  * Make sure that `JNI_PACKAGE_NAME` in [`plugin/src/main/cpp/plugin_jni.cpp`](plugin/src/main/cpp/plugin_jni.cpp)
    is updated accordingly
* Complete the plugin configuration
  * Open [`plugin/export_scripts_template/plugin.cfg`](plugin/export_scripts_template/plugin.cfg)
    * Update the `description` field
    * Update the `author` field
    * Update the `version` field
  * Open [`plugin/export_scripts_template/plugin.gdextension`](plugin/export_scripts_template/plugin.gdextension)
    * Update the names and paths to the generated Android shared libraries
    * Add any other platform your plugin intends to support

### Building the configured Android plugin
- In a terminal window, navigate to the project's root directory and run the following command:
```
./gradlew assemble
```
- On successful completion of the build, the output files can be found in
  [`plugin/demo/addons`](plugin/demo/addons)

### Testing the Android plugin
You can use the included [Godot demo project](plugin/demo/project.godot) to test the built Android 
plugin

- Open the demo in Godot (4.2 or higher)
- Navigate to `Project` -> `Project Settings...` -> `Plugins`, and ensure the plugin is enabled
- Install the Godot Android build template by clicking on `Project` -> `Install Android Build Template...`
- Open [`plugin/demo/main.gd`](plugin/demo/main.gd) and update the logic as needed to reference 
  your plugin and its methods
- Connect an Android device to your machine and run the demo on it
