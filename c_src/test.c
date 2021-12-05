#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <time.h>
#include <inttypes.h>
#include <jni.h>
#include "matrixLib.h"

JNIEXPORT jdoubleArray JNICALL Java_main_java_com_example_main_1menu_helpers_CurveFitJNI_vectorProjection (JNIEnv *env, jobject obj, jdoubleArray posData, jint degree)
{
    // gcc -g -Werror -o test_app test.c matrixLib.c -lm -Ofast -I%JAVA_HOME%\include -I%JAVA_HOME%\include\win32
    jint arrLen = (*env)->GetArrayLength(env, posData);

    double **positionData = (*env)->GetPrimitiveArrayCritical(env,posData, 0);
    if(positionData == NULL)
    {
        // TODO Print out error here (C doesnt have exceptions) strerror perror stuff
    }
    else
    {
        return vectorProjection(posData, positionData, arrLen, degree, env);
    }
}

/*
int main(void)
{

    return 0;
}
*/