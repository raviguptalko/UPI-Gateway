#include <jni.h>
#include <string>

std::string jstring2string(JNIEnv *pEnv, jstring pJstring);

extern "C" JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t1(
        JNIEnv* env,
        jclass clazz) {
    std::string api_key = "https://app.badabazar.in/login.php?";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
        Java_com_treatpay_upigateway_Wrap_t2(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://random.badabazar.in/kyc.php";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t3(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://treatpay.com/API/v2/agent/aeps.php?for=key_live";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t4(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://agent.treatpay.com/response.php?";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t5(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://agent.treatpay.com/pay.php";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t6(JNIEnv * env, jclass clazz) {
    std::string api_key = "xayjuqy79po6nrhp";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t7(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://test.treatpay.com/GTW/payments_update_enc.php?";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t8(JNIEnv * env, jclass clazz) {
    std::string api_key = "anoroc";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t9(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://test.treatpay.com/GTW/payments_enc.php?";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t10(JNIEnv * env, jclass clazz) {
    std::string api_key = "9fb663ccd2bc5c1e9656427563634503";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t11(JNIEnv * env, jclass clazz) {
    std::string api_key = "9005065666";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t12(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://treatpay.com/img/logo_aeps.png";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t13(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://treatpay.com/aeps_android_b2c.php";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_t14(JNIEnv * env, jclass clazz) {
    std::string api_key = "https://random.badabazar.in/dashboard.php?device_id=";
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_treatpay_upigateway_Wrap_enc(
        JNIEnv* pEnv,
        jclass pThis,
        jint a) {
    return (a + 1)*(a + 1);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_r1(JNIEnv * env, jclass clazz, jint n) {
    std::string api_key = "";
    char chars[]= "abcdefghijklmnopqrstuvwxyz0123456789";
    jint len=strlen(chars);
    for(int  i=0;i<n;i++) {
        api_key+=chars[rand() % len];
    }
    return env->NewStringUTF(api_key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_treatpay_upigateway_Wrap_r2(JNIEnv * env, jclass clazz, jstring pa, jstring pn, jstring tn, jstring am, jstring tr) {
    std::string url = "upi://pay?pa=";
    std::string paa = jstring2string(env, pa);
    std::string pna = jstring2string(env, pn);
    std::string tna = jstring2string(env, tn);
    std::string ama = jstring2string(env, am);
    std::string tra = jstring2string(env, tr);
    std::string last = "&mc=0000&mam=1";
    url = url + paa + "&pn=" + pna + "&tn=" + tna + "&am=" + ama + "&tr=" + tra + last;
    return env->NewStringUTF(url.c_str());
}

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}
