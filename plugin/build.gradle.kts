import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

// TODO: Update value to your plugin's name.
val pluginName = "GDExtensionAndroidPluginTemplate"

// TODO: Update value to match your plugin's package name.
val pluginPackageName = "org.godotengine.plugin.android.gdextension.template"

/**
 * Flag used to specify whether the `plugin.gdextension` config file has libraries for platforms
 * other than Android and can be used by the Godot Editor
 *
 * TODO: Update the flag value based on your plugin's configuration
 */
val gdextensionSupportsNonAndroidPlatforms = false

android {
    namespace = pluginPackageName
    compileSdk = 33

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 21

        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
        ndk {
            abiFilters.add("arm64-v8a")
        }

        manifestPlaceholders["godotPluginName"] = pluginName
        manifestPlaceholders["godotPluginPackageName"] = pluginPackageName
        buildConfigField("String", "GODOT_PLUGIN_NAME", "\"${pluginName}\"")
        setProperty("archivesBaseName", pluginName)
    }
    externalNativeBuild {
        cmake {
            path("CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // TODO: Update the godot dep when 4.2 is stable
    implementation("org.godotengine:godot:4.2.0.dev-SNAPSHOT")
}

// BUILD TASKS DEFINITION
val cleanAssetsAddons by tasks.registering(Copy::class) {
    delete("src/main/assets/addons")
}

val copyExportScriptsTemplate by tasks.registering(Copy::class) {
    description = "Copies the export scripts templates to the plugin's addons directory"

    dependsOn(cleanAssetsAddons)

    from("export_scripts_template")
    into("src/main/assets/addons/$pluginName")
}

val copyDebugAARToAddons by tasks.registering(Copy::class) {
    description = "Copies the generated debug AAR binary to the plugin's addons directory"
    from("build/outputs/aar")
    include("$pluginName-debug.aar")
    into("src/main/assets/addons/$pluginName/.bin/debug")
}

val copyReleaseAARToAddons by tasks.registering(Copy::class) {
    description = "Copies the generated release AAR binary to the plugin's addons directory"
    from("build/outputs/aar")
    include("$pluginName-release.aar")
    into("src/main/assets/addons/$pluginName/.bin/release")
}

val copyDebugSharedLibs by tasks.registering(Copy::class) {
    description = "Copies the generated debug .so shared library to the plugin's addons directory"
    from("build/intermediates/cmake/debug/obj")
    into("src/main/assets/addons/$pluginName/.bin/debug")
}

val copyReleaseSharedLibs by tasks.registering(Copy::class) {
    description = "Copies the generated release .so shared library to the plugin's addons directory"
    from("build/intermediates/cmake/release/obj")
    into("src/main/assets/addons/$pluginName/.bin/release")
}

val cleanDemoAddons by tasks.registering(Delete::class) {
    delete("demo/addons/$pluginName")
}

val copyAddonsToDemo by tasks.registering(Copy::class) {
    description = "Copies the plugin's output artifact to the output directory"

    dependsOn(cleanDemoAddons)
    dependsOn(copyDebugAARToAddons)
    dependsOn(copyReleaseAARToAddons)
    dependsOn(copyDebugSharedLibs)
    dependsOn(copyReleaseSharedLibs)

    from("src/main/assets/addons/$pluginName")
    if (!gdextensionSupportsNonAndroidPlatforms) {
        exclude("plugin.gdextension")
    }
    into("demo/addons/$pluginName")
}

tasks.named("preBuild").dependsOn(copyExportScriptsTemplate)

tasks.named("assemble").configure {
    dependsOn(copyExportScriptsTemplate)
    finalizedBy(copyAddonsToDemo)
}

tasks.named<Delete>("clean").apply {
    dependsOn(cleanDemoAddons)
    dependsOn(cleanAssetsAddons)
}
