#ifndef MATRIXLIB_H
#define MATRIXLIB_H
#include <jni.h>
#include <stdbool.h>

typedef struct matrixArr{ 
        int rows;
        int cols;
        double **arr;
    } matrixArrStruct;
typedef struct dataArr{
        int size;
        double **arr;
        double **arrOverTime;
    }dataPointArr;
typedef struct extremaStruct{
    int lastIndex;
    bool lastExtremaConcentric;
    }localExtremaStruct;
matrixArrStruct* multiplyMatrix(matrixArrStruct *matrixAStruct, matrixArrStruct *matrixBStruct); 
double* multiplyMatrix1D(matrixArrStruct *matrixAStruct, double **matrixB);
matrixArrStruct** createMatrices(matrixArrStruct *data, int degree);
matrixArrStruct* transposeMatrix(matrixArrStruct *data);
matrixArrStruct* adjointMatrix(matrixArrStruct *data);
matrixArrStruct* inverseMatrix(matrixArrStruct *data);
double* determinantMatrix(matrixArrStruct *data);
matrixArrStruct* laplaceExpansion(matrixArrStruct *data, int rowSkip, int colSkip);

double convertLBToKG(double LB);
double calculateXCoordinate(double distance, double angle);
double calculateYCoordinate(double distance, double angle);
dataPointArr* newCalculatePositionalData(JNIEnv *env, jdoubleArray *posDataJArr, int sizeOfArr, int lastExtremaIndex);
dataPointArr* calculatePositionalData(JNIEnv *env, jdoubleArray *posDataJArr, int sizeOfArr, int lastExtremaIndex);
localExtremaStruct* findLocalExtrema(dataPointArr *positionData, int *actualLastExtremaIndex, double *coefficents, int degree);
double calculateTime(int dataPointIndex);
double pythagoreanTheorem(double xDistance, double yDistance);
double totalDistanceTraveledBetweenTwoPoints(double **positionData, int startPointIndex, int endPointIndex);
double currentVelocity(dataPointArr *positionData, int startPointIndex, int endPointIndex);
double* vectorProjection(double **positionData, int size, int degree);
double averageForceProduction(dataPointArr *posDataArray, localExtremaStruct *localExtrema, double intialVelocity, int weight, bool inKG);
JNIEXPORT jdoubleArray JNICALL Java_CurveFitJNI_performanceMetrics(JNIEnv *env, jobject obj, jdoubleArray posDataJArr, jint weight, jdouble intialVelocity, jboolean inKG);

#endif
