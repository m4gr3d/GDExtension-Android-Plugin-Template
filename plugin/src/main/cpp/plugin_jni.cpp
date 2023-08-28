#include <jni.h>

#include <godot_cpp/variant/utility_functions.hpp>

#include "utils.h"

#undef JNI_PACKAGE_NAME
// TODO: Update to match plugin's package name
#define JNI_PACKAGE_NAME org_godotengine_plugin_android_gdextension_template

#undef JNI_CLASS_NAME
#define JNI_CLASS_NAME GDExtensionAndroidPlugin

extern "C" {
    JNIEXPORT void JNICALL JNI_METHOD(helloWorld)(JNIEnv *env, jobject) {
        godot::UtilityFunctions::print("Hello GDExtension World!");
    }
};
