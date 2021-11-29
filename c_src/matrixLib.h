#ifndef MATRIXLIB_H
#define MATRIXLIB_H

typedef struct matrixArr{ 
        int rows;
        int cols;
        double **arr;
    } matrixArrStruct;
matrixArrStruct* multiplyMatrix(matrixArrStruct *matrixAStruct, matrixArrStruct *matrixBStruct); 
matrixArrStruct* multiplyMatrix1D(matrixArrStruct *matrixAStruct, matrixArrStruct *matrixBStruct);
matrixArrStruct** createMatrices(matrixArrStruct *data, int degree);
matrixArrStruct* transposeMatrix(matrixArrStruct *data);
matrixArrStruct* adjointMatrix(matrixArrStruct *data);
matrixArrStruct* inverseMatrix(matrixArrStruct *data);
double* determinantMatrix(matrixArrStruct *data);
matrixArrStruct* laplaceExpansion(matrixArrStruct *data, int rowSkip, int colSkip);
jdoubleArray vectorProjection(jarray posDataJArray, double** posData, int len, int degree, JNIEnv *env);
#endif
