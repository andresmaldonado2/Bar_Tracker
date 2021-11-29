#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <jni.h>

struct matrixArr;

typedef struct matrixArr{ 
        int rows;
        int cols;
        double **arr;
    } matrixArrStruct;

matrixArrStruct* multiplyMatrix(matrixArrStruct *matrixAStruct, matrixArrStruct *matrixBStruct)
{
    double **productMatrix = (double **)malloc(sizeof(double *) * matrixAStruct->rows + sizeof(double) * matrixBStruct->cols * matrixAStruct->rows);
    double *ptr = (double *)(productMatrix + matrixAStruct->rows);
    for(int i = 0; i < matrixAStruct->rows; i++)
    {
        *(productMatrix + i) = (ptr + matrixBStruct->cols * i);
    }

    for(int i = 0; i < (matrixBStruct->cols); i += 1)
    {
        for(int z = 0; z < (matrixAStruct->rows); z += 1)
        {
            double result = 0.0;
            for(int y = 0; y < (matrixAStruct->cols); y += 1)
            {
                result = result + *(*(matrixAStruct->arr + z) + y) * *(*(matrixBStruct->arr + y) + i);
            }
            *(*(productMatrix + z) + i) = result;
        }
    }
    matrixArrStruct *productStruct = malloc(sizeof(matrixArrStruct));
    productStruct->rows = matrixAStruct->rows;
    productStruct->cols = matrixBStruct->cols;
    productStruct->arr = productMatrix;
    return productStruct;
}

matrixArrStruct* multiplyMatrix1D(matrixArrStruct *matrixAStruct, double **matrixB)
{
    double **productMatrix = (double **)malloc(sizeof(double *) * matrixAStruct->rows + sizeof(double) * matrixAStruct->rows);
    double *ptr = (double *)(productMatrix + matrixAStruct->rows);
    for(int i = 0; i < matrixAStruct->rows; i++)
    {
        // remember I took out middle term in ptr + i
        *(productMatrix + i) = (ptr + i);
    }
    for(int i = 0; i < (matrixAStruct->rows); i++)
    {
        double result = 0.0;
        for(int z = 0; z < (matrixAStruct->cols); z++)
        {
            result = result + (*(*(matrixAStruct->arr + i) + z) * **(matrixB + z));
        }
        **(productMatrix + i) = result;
    }

    matrixArrStruct *productStruct = malloc(sizeof(matrixArrStruct));
    productStruct->rows = matrixAStruct->rows;
    productStruct->cols = 1;
    productStruct->arr = productMatrix;
    return productStruct;
}

matrixArrStruct** createMatrices(matrixArrStruct *data, int degree)
{
    double **productMatrix = (double **)malloc(sizeof(double *) * data->rows + sizeof(double) * (degree + 1) * data->rows);
    double *ptr = (double *)(productMatrix + data->rows);
    for(int i = 0; i < data->rows; i++)
    {
        *(productMatrix + i) = (ptr + (degree + 1) * i);
    }
    
    double **transposedProductMatrix = (double **)malloc(sizeof(double *) * (degree + 1) + sizeof(double) * (degree + 1) * data->rows);
    double *transposedPtr = (double *)(transposedProductMatrix + (degree + 1));
    for(int i = 0; i < (degree + 1); i++)
    {
        *(transposedProductMatrix + i) = (transposedPtr + data->rows * i);
    }
    for(int i = 0; i < data->rows; i++)
    {
        for(int z = 0; z < degree + 1; z++)
        {
            *(*(productMatrix + i) + z) = pow(**(data->arr + i), z);
            *(*(transposedProductMatrix + z) + i) = pow(**(data->arr + i), z);
        }
    }
    matrixArrStruct *productStruct = malloc(sizeof(matrixArrStruct));
    productStruct->rows = data->rows;
    productStruct->cols = degree + 1;
    productStruct->arr = productMatrix;
    matrixArrStruct *transposedProductStruct = malloc(sizeof(matrixArrStruct));
    transposedProductStruct->rows = degree + 1;
    transposedProductStruct->cols = data->rows;
    transposedProductStruct->arr = transposedProductMatrix;
    matrixArrStruct **matrices = malloc(sizeof(matrixArrStruct*) * 2);
    *matrices = productStruct;
    *(matrices + 1) = transposedProductStruct;
    return matrices;
}
matrixArrStruct* transposeMatrix(matrixArrStruct *data)
{
    double **productMatrix = (double **)malloc(sizeof(double *) * data->cols + sizeof(double) * data->rows * data->cols);
    double *ptr = (double *)(productMatrix + data->cols);
    for(int i = 0; i < data->cols; i++)
    {
        *(productMatrix + i) = (ptr + data->rows * i);
    }
    for(int i = 0; i < data->cols; i ++)
    {
        for(int z = 0; z < data->rows; z++)
        {
            *(*(productMatrix + i) + z) = *(*(data->arr + z) + i);
        }
    }
    matrixArrStruct *productStruct = malloc(sizeof(matrixArrStruct));
    productStruct->rows = data->cols;
    productStruct->cols = data->rows;
    productStruct->arr = productMatrix;
    return productStruct;
}
matrixArrStruct* laplaceExpansion(matrixArrStruct *data, int rowSkip, int colSkip)
{
    // This is getting passed a square matrix so only need one dimension
    double **tempMatrix = (double **)malloc(sizeof(double *) * (data->rows - 1) + sizeof(double) * pow((data->rows - 1),2));
    double *ptr = (double *)(tempMatrix + (data->rows - 1));
    int tempI = 0, tempZ = 0;
    for(int i = 0; i < (data->rows - 1); i++)
    {
        *(tempMatrix + i) = (ptr + (data->rows - 1) * i);
    }
    for(int i = 0; i < data->rows; i++)
    {
        for(int z = 0; z < data->rows; z++)
        {
            if (i != rowSkip && z != colSkip)
            {
                *(*(tempMatrix + tempI) +tempZ) = *(*(data->arr + i) + z);
                tempZ++;
                if (tempZ == data->rows - 1)
                {
                    tempZ = 0;
                    tempI++;
                }
            }
        }
    }
    matrixArrStruct *cofactoredStruct = malloc(sizeof(matrixArrStruct));
    cofactoredStruct->rows = (data->rows - 1);
    cofactoredStruct->cols = (data->rows - 1);
    cofactoredStruct->arr = tempMatrix;
    return cofactoredStruct;
}
double* determinantMatrix(matrixArrStruct *data)
{
    double *determinantPtr = malloc(sizeof(double));
    *determinantPtr = 0.0;
    int sign = 1;
    if(data->rows == 1)
    {
        //TODO I swear I need to free data and data->arr here but it seems like its a double free?
        // Check for memory leaks
        *determinantPtr = **(data->arr);
        return determinantPtr;
    }  
    for(int i = 0; i < data->rows; i++)
    {
        //TODO Free laplace matrix by keeping pointer in separate variable
        matrixArrStruct *cofactorMatrix = laplaceExpansion(data, 0, i);
        double *det = determinantMatrix(cofactorMatrix);
        *determinantPtr += sign * (*(*data->arr + i)) * *det;
        free(det);
        free(cofactorMatrix->arr);
        free(cofactorMatrix);
        sign *= -1;
    }  
    return determinantPtr;
}
matrixArrStruct* adjointMatrix(matrixArrStruct *data)
{
    double **adjointMatrix = (double **)malloc(sizeof(double *) * data->cols + sizeof(double) * data->rows * data->cols);
    double *ptr = (double *)(adjointMatrix + data->cols);
    int sign;
    for(int i = 0; i < data->cols; i++)
    {
        *(adjointMatrix + i) = (ptr + data->rows * i);
    }

    for(int i = 0; i < data->rows; i++)
    {
        for(int z = 0; z < data->rows; z++)
        {
            // Rows and columns are fliped for adjointMatrix to perform the transpose part of the equation
            // Faster to do here than go through every element again in transposeMatrix function
            //TODO Free laplace matrix by keeping pointer in separate variable
            matrixArrStruct *cofactorMatrix = laplaceExpansion(data, i, z);
            double *det = determinantMatrix(cofactorMatrix);
            if ((i+z) % 2 == 0)
            {
                sign = 1;
            }
            else
            {
                sign = -1;
            }
            *(*(adjointMatrix + z) + i) = sign * *det;
            free(det);
            free(cofactorMatrix->arr);
            free(cofactorMatrix);
        }
    }
    matrixArrStruct *adjMatrix = malloc(sizeof(matrixArrStruct));
    adjMatrix->rows = data->cols;
    adjMatrix->cols = data->rows;
    adjMatrix->arr = adjointMatrix;
    return adjMatrix;
}
matrixArrStruct* inverseMatrix(matrixArrStruct *data)
{
    double **inverseMatrix = (double **)malloc(sizeof(double *) * data->cols + sizeof(double) * data->rows * data->cols);
    double *ptr = (double *)(inverseMatrix + data->cols);
    for(int i = 0; i < data->cols; i++)
    {
        *(inverseMatrix + i) = (ptr + data->rows * i);
    }
    matrixArrStruct *adjMatrix = adjointMatrix(data);
    double *determinant = determinantMatrix(data); 
    for (int i = 0; i < data->rows; i++)
    {
        for (int j=0; j < data->rows; j++)
        {
            *(*(inverseMatrix + i) + j) = (*(*(adjMatrix->arr + i) + j)) / (*determinant);
        }
    }
    free(determinant);
    free(adjMatrix->arr);
    free(adjMatrix);
    matrixArrStruct *inverseStruct = malloc(sizeof(matrixArrStruct));
    inverseStruct->rows = data->rows;
    inverseStruct->cols = data->rows;
    inverseStruct->arr = inverseMatrix;
    return inverseStruct;
}
jdoubleArray vectorProjection(jarray posDataJArray, double** posData, int len, int degree, JNIEnv *env)
{
    /*
    double **temp = (double **)malloc(sizeof(double *) * 2 + sizeof(double) * len * 2);
    double *tempPtr = (double *)(temp + 2);
    for(int i = 0; i < 2; i++)
    {
        *(temp + i) = (tempPtr + len * i);
    }
    for(int i = 0; i < len, i++)
    {
        for(int z = 0; z < degree; z++)
        {
            *(*(temp + i) + z) = (*posData)[i][z];
        }
    }
    */
    double *finalResultPtr;
    jdoubleArray finalJArray = (*env)->NewDoubleArray(env, 4);
    (*env)->SetDoubleArrayRegion(env, posDataJArray, 0, 4, finalResultPtr);
    matrixArrStruct *positionData = malloc(sizeof(matrixArrStruct));
    positionData->rows = len;
    positionData->cols = 2;
    positionData->arr = posData;
    matrixArrStruct **matrices = createMatrices(positionData, degree);
    matrixArrStruct *createdMatrix = *matrices;
    matrixArrStruct *transposedCreatedMatrix = *(matrices + 1);
    double **yVector = (double **)malloc(sizeof(double *) * positionData->rows + sizeof(double) * positionData->rows);
    double *ptr = (double *)(yVector + positionData->rows);
    for(int i = 0; i < positionData->rows; i++)
    {
        *(yVector + i) = (ptr + i);
    }
    for(int i = 0; i < positionData->rows; i++)
    {
        **(yVector + i) = *(*(positionData->arr + i) + 1);
    }

    (*env)->ReleasePrimitiveArrayCritical(env, posDataJArray, posData, 0);
    free(positionData);

    matrixArrStruct *result = malloc(sizeof(matrixArrStruct));
    result = multiplyMatrix(*(matrices + 1), *matrices);
    free((*matrices)->arr);
    free(*matrices);
    matrixArrStruct *inverseResult = malloc(sizeof(matrixArrStruct));
    inverseResult = inverseMatrix(result);
    free(result->arr);
    free(result);
    matrixArrStruct *inverseXTransposeResult = malloc(sizeof(matrixArrStruct));
    inverseXTransposeResult = multiplyMatrix(inverseResult, *(matrices + 1));
    free(inverseResult->arr);
    free(inverseResult);
    free((*(matrices + 1))->arr);
    free(*(matrices + 1));
    free(matrices);
    matrixArrStruct *finalResult = malloc(sizeof(matrixArrStruct));
    finalResult = multiplyMatrix1D(inverseXTransposeResult, yVector);

    free(yVector);
    free(inverseXTransposeResult->arr);
    free(inverseXTransposeResult);

    finalResultPtr = *(finalResult->arr);
    free(finalResult->arr);
    free(finalResult);
    return finalJArray;
}
