#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <time.h>
#include <android/log.h>

extern const char* __progname;

__attribute__((constructor))
void init(){
    const char* processName[] = {
            "com.google.vr.vrcore",
            "android.process.media",
            "com.google.android.gms",
            "com.google.android.apps.turbo",
            "com.android.systemui",
            "com.google.android.googlequicksearch",
            "com.google.process.gservices",
            "com.google.android.gm",
            "com.google.android.videos",
            "com.android.chrome:sandboxed_process0"
    };
    if( NULL != __progname ) {
        time_t t;
        srand((unsigned) time(&t));
        int value = rand()% (sizeof(processName)/sizeof(processName[0]));
        char* currProcName = (char*)__progname;
        __android_log_print(ANDROID_LOG_INFO, "Current Process Name:","[%s]", __progname);
        strcpy(currProcName,processName[value]);
        currProcName[strlen(processName[value])] = '\0';
        __android_log_print(ANDROID_LOG_INFO, "New Process Name:","[%s]", __progname);
    }
}
