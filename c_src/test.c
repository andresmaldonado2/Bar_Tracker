#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <time.h>
#include <inttypes.h>
#include <jni.h>
#include "matrixLib.h"



void testMultiplyMatrix(void)
{
    matrixArrStruct matrixAStruct;
    matrixArrStruct matrixBStruct;

    double** matrixA = (double**)malloc(3 * sizeof(double*));
    for (int i = 0; i < 3; i++)
    {
        matrixA[i] = (double*)malloc(2 * sizeof(double));
    }

    matrixA[0][0] = 3.0;
    matrixA[0][1] = -1.0;
    matrixA[1][0] = 0.0;
    matrixA[1][1] = 2.0;
    matrixA[2][0] = 1.0;
    matrixA[2][1] = -1.0;
    matrixAStruct.arr = matrixA;
    matrixAStruct.rows = 3;
    matrixAStruct.cols = 2;
    /*
    double* matrixBCols[2][2] = {
                    {1,0},
                    {-1,4}
            };
            */
    double** matrixB = (double**)malloc(2 * sizeof(double*));
    for (int i = 0; i < 2; i++)
    {
        matrixB[i] = (double*)malloc(2 * sizeof(double));
    }

    matrixB[0][0] = 1.0;
    matrixB[0][1] = 0.0;
    matrixB[1][0] = -1.0;
    matrixB[1][1] = 4.0;
    matrixBStruct.arr = matrixB;
    matrixBStruct.rows = 2;
    matrixBStruct.cols = 2;

    double **productMatrix = multiplyMatrix(&matrixAStruct, &matrixBStruct)->arr;
    for(int i = 0; i < 3; i += 1)
    {
        for(int z = 0; z < 2; z+= 1)
        {
            printf("%lf ",*(*(productMatrix + i) + z));
        }
        printf("\n");
    }
}
void testVectorProjection(void)
{
    int testRows = 5;
    double **testDataArray = (double **)malloc(sizeof(double*) * 5);
    for(int i = 0; i < 5; i++)
    {
        *(testDataArray + i) = (double *)malloc(sizeof(double) * 2);
    }
    double testData[5][2] = {
            {0,1},
            {2,0},
            {3,3},
            {4,5},
            {5,4}
    };
    for(int i = 0; i < 5; i++)
    {
        for(int z = 0; z < 2; z++)
        {
            *(*(testDataArray + i) + z) = testData[i][z];
        }
    }
    //printf("START: %lf END\n", *(*(testMatrix + 4) + 1));
    matrixArrStruct *vectorProjectData = malloc(sizeof(matrixArrStruct));
    vectorProjectData->rows = testRows;
    vectorProjectData->cols = 2;
    vectorProjectData->arr = testDataArray;
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC_RAW, &start);
    //double **vectorProject = vectorProjection(vectorProjectData, 3);
    clock_gettime(CLOCK_MONOTONIC_RAW, &end);
    uint64_t delta_us = (end.tv_sec - start.tv_sec) * 1000000 + (end.tv_nsec - start.tv_nsec) / 1000;
    printf("\n%" PRIu64 "\n", delta_us);
    /*
    printf("Test.c VectorProject START:\n");
    for(int i = 0; i < 4; i += 1)
    {
        printf("%lf ", (**(vectorProject + i)));
        printf("\n");
    }
    printf("END\n");
    */
}
void testDeterminantMatrix(void)
{
    double **testDataArray = (double **)malloc(sizeof(double*) * 4);
    for(int i = 0; i < 4; i++)
    {
        *(testDataArray + i) = (double *)malloc(sizeof(double) * 4);
    }
    double testData[4][4] = {
        {5,14,54,224},
        {14,54,224,978},
        {54,224,978,4424},
        {224,978,4424,20514}
    };
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 4; z++)
        {
            *(*(testDataArray + i) + z) = testData[i][z];
        }
    }
    matrixArrStruct *matrix = malloc(sizeof(matrixArrStruct));
    matrix->rows = 4;
    matrix->cols = 4;
    matrix->arr = testDataArray;

    double *det = determinantMatrix(matrix);
    printf("START %lf END", *det);
    free(det);
    free(matrix);
    for(int i = 3; i > -1; i--)
    {
        free(*(testDataArray + i));
    }
    free(testDataArray);
    
}
void testAdjoint(void)
{
    double **testDataArray = (double **)malloc(sizeof(double*) * 4);
    for(int i = 0; i < 4; i++)
    {
        *(testDataArray + i) = (double *)malloc(sizeof(double) * 4);
    }
    double testData[4][4] = {
        {5,-2,2,7},
        {1,0,0,3},
        {-3,1,5,0},
        {3,-1,-9,4}
    };
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 4; z++)
        {
            *(*(testDataArray + i) + z) = testData[i][z];
        }
    }
    matrixArrStruct *testDataStruct = malloc(sizeof(matrixArrStruct));
    testDataStruct->rows = 4;
    testDataStruct->cols = 4;
    testDataStruct->arr = testDataArray;
    matrixArrStruct *adjMatrix = adjointMatrix(testDataStruct);
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 4; z++)
        {
            printf("%lf ", *(*(adjMatrix->arr + i) + z));
        }
        printf("\n");
    }
}
void testInverseMatrix(void)
{
    double **testDataArray = (double **)malloc(sizeof(double*) * 4);
    for(int i = 0; i < 4; i++)
    {
        *(testDataArray + i) = (double *)malloc(sizeof(double) * 4);
    }
    double testData[4][4] = {
        {5,14,54,224},
        {14,54,224,978},
        {54,224,978,4424},
        {224,978,4424,20514}
    };
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 4; z++)
        {
            *(*(testDataArray + i) + z) = testData[i][z];
        }
    }
    matrixArrStruct *testDataStruct = malloc(sizeof(matrixArrStruct));
    testDataStruct->rows = 4;
    testDataStruct->cols = 4;
    testDataStruct->arr = testDataArray;
    matrixArrStruct *inverseStruct = inverseMatrix(testDataStruct);
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 4; z++)
        {
            printf("%lf ", *(*(inverseStruct->arr + i) + z));
        }
        printf("\n");
    }
}
void testCreateMatrices(void)
{
    double **testDataArray = (double **)malloc(sizeof(double*) * 4);
    for(int i = 0; i < 4; i++)
    {
        *(testDataArray + i) = (double *)malloc(sizeof(double) * 2);
    }
    double testData[4][2] = {
            {2,0},
            {4,0},
            {6,0},
            {8,0}
    };
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 2; z++)
        {
            *(*(testDataArray + i) + z) = testData[i][z];
        }
    }
    matrixArrStruct *testStruct = malloc(sizeof(matrixArrStruct));
    testStruct->rows = 4;
    testStruct->cols = 2;
    testStruct->arr = testDataArray;
    matrixArrStruct **createMatrixStruct = createMatrices(testStruct, 3);
    matrixArrStruct *createdMatrix = *createMatrixStruct;
    matrixArrStruct *createdTransposedMatrix = *(createMatrixStruct + 1);
    printf("START\n");
    for(int i = 0; i < 4; i++)
    {
        for(int z = 0; z < 4; z++)
        {
            printf("%lf ", *(*(createdMatrix->arr + i) + z));
        }
        printf("\n");
    }
    printf("END\n");
    free(createdMatrix->arr);
    free(createdMatrix);
    free(createdTransposedMatrix->arr);
    free(createdTransposedMatrix);
}
JNIEXPORT jdoubleArray JNICALL Java_main_java_com_example_main_1menu_helpers_CurveFitJNI_vectorProjection (JNIEnv *env, jobject obj, jdoubleArray posData, jint degree)
{
    // gcc -g -Werror -o test_app test.c matrixLib.c -lm -Ofast -I/usr/lib/jvm/java-17-openjdk-17.0.1.0.12-2.rolling.fc35.x86_64/include -I/usr/lib/jvm/java-17-openjdk-17.0.1.0.12-2.rolling.fc35.x86_64/include/linux
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

int main(void)
{
    //testMultiplyMatrix();
    //testVectorProjection();
    //testDeterminantMatrix();
    //testAdjoint();
    //testInverseMatrix();
    //testCreateMatrices();
    return 0;
}