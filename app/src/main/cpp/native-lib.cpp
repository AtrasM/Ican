#include <jni.h>
#include <string>
#include <iostream>
#include <vector>
#include <bitset>
#include <android/log.h>

using namespace std;


vector<string> words_to_binary(string words);

string *strToBinary(string words);

string coding_data(vector<string> strArray);

extern "C" JNIEXPORT jstring JNICALL
Java_avida_ican_Farzin_Presenter_SetLicenseKeyPresenter_GetApplicationName(
        JNIEnv *env,
        jobject /* this */) {
    string applicationName = "IcanApp";

    return env->NewStringUTF(applicationName.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_avida_ican_Farzin_Presenter_SetLicenseKeyPresenter_GetRandom(
        JNIEnv *env,
        jobject /* this */, jstring data) {

    const char *chrwords = env->GetStringUTFChars(data, 0);
    string s = chrwords;
    string result = "" + coding_data(words_to_binary(s));
    return env->NewStringUTF(result.c_str());
}


vector<string> words_to_binary(string words) {
    string myString = words;
    int len = static_cast<int>(myString.size());
    vector<std::string> binaryArrayString;

    for (size_t i = 0; i < len; ++i) {
        string bin = "";
        bin = bitset<8>(words.c_str()[i]).to_string();
        binaryArrayString.push_back(bin);
    }

/*    for (size_t i = 0; i < len; ++i) {
        __android_log_print(ANDROID_LOG_INFO, "sometag", "words_to_binary = %s",
                            binaryArrayString[i].c_str());
    }*/
    len = binaryArrayString.size();
    return binaryArrayString;
}

string coding_data(vector<string> strArray) {
    int size = static_cast<int>(strArray.size());
    string result = "";
    vector<string> output;
    for (size_t i = 0; i < size; ++i) {
        const char *str1 = "";
        const char *str2 = "";
        string temp = "";
        if ((i + 1) < size) {
            str1 = strArray[i].c_str();
            str2 = strArray[i + 1].c_str();
        } else {
            str1 = strArray[i].c_str();
            str2 = strArray[0].c_str();
        }
        for (size_t k = 0; k < strlen(str1); ++k) {
            char result = ((str1[k] - '0') ^ (str2[k] - '0')) + '0';
            //char result = str1[k] ^ str2[k];
            temp += result;

        }
        output.push_back(temp);
    }
/*    for (size_t i = 0; i < size; ++i) {
        __android_log_print(ANDROID_LOG_INFO, "sometag", "xor output = %s",
                            output[i].c_str());
    }*/

    for (size_t l = 0; l < size; l = l + 2) {
        string str1;
        string str2;
        long bin_to_dec = 0;
        basic_string<char, char_traits<char>, allocator<char>> temp = output[l + 1];
        output[l + 1] = output[l];
        output[l] = temp;
        bin_to_dec = ((bitset<8>(output[l]).to_ulong()) + 1) * 5;
        str1 = to_string(bin_to_dec);
        bin_to_dec = ((bitset<8>(output[l + 1]).to_ulong()) + 1) * 5;
        str2 = to_string(bin_to_dec);

        result = result + str1 + str2;
    }
    return result;
}





