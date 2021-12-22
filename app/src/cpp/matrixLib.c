#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <jni.h>

// This frequency is assumed to be in Hz
const double FREQUENCY_OF_TRACKER_SIGNAL = 50.0;

struct matrixArr;
struct dataArr;
struct localExtremaArr;

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

double* multiplyMatrix1D(matrixArrStruct *matrixAStruct, double **matrixB)
{
    double result = 0.0;
    double *productMatrix = malloc(sizeof(double) * (matrixAStruct->rows));
    for(int i = 0; i < (matrixAStruct->rows); i++)
    {
        result = 0.0;
        for(int z = 0; z < (matrixAStruct->cols); z++)
        {
            result += (*(*(matrixAStruct->arr + i) + z) * **(matrixB + z));
        }
        *(productMatrix + i) = result;
    }
    return productMatrix;
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
        *determinantPtr = **(data->arr);
        return determinantPtr;
    }  
    for(int i = 0; i < data->rows; i++)
    {
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
double convertLBToKG(double LB)
{
    return LB * 0.453592;
}
double calculateXCoordinate(double distance, double angle)
{
    return distance * sin(M_PI/2 - (angle * (M_PI / 180.0)));
}
double calculateYCoordinate(double distance, double angle)
{
    return distance * sin(angle * (M_PI / 180.0));
}
dataPointArr* newCalculatePositionalData(JNIEnv *env, jdoubleArray *posDataJArr, int sizeOfArr, int lastExtremaIndex)
{
    jsize length = (*env)->GetArrayLength(env, *posDataJArr);
    jdouble positionDataJArr[length];
    (*env)->GetDoubleArrayRegion(env, *posDataJArr, 0, length, positionDataJArr);

    double **positionData = malloc(sizeof(double *) * sizeOfArr + sizeof(double) * 2 * sizeOfArr);
    double *ptr = (double *)(positionData + sizeOfArr);
    for(int i = 0; i < sizeOfArr; i++)
    {
        *(positionData + i) = (ptr + 2 * i);
    }
    double **positionHeightOverTime = malloc(sizeof(double *) * sizeOfArr + sizeof(double) * 2 * sizeOfArr);
    double *overTimePtr = (double *)(positionHeightOverTime + sizeOfArr);
    for(int i = 0; i < sizeOfArr; i++)
    {
        *(positionHeightOverTime + i) = (overTimePtr + 2 * i);
    }

    int count = 0;
    for(int i = 0; i < sizeOfArr; i++)
    {
        *(*(positionData + i)) = calculateXCoordinate(positionDataJArr[count], positionDataJArr[count + 1]);
        *(*(positionData + i) + 1) = calculateYCoordinate(positionDataJArr[count], positionDataJArr[count + 1]);

        *(*(positionHeightOverTime + i)) = (i + lastExtremaIndex) * (1/FREQUENCY_OF_TRACKER_SIGNAL);
        *(*(positionHeightOverTime + i) + 1) = calculateYCoordinate(positionDataJArr[count], positionDataJArr[count + 1]);
        count += 2;
    }
    dataPointArr *positionArr = malloc(sizeof(dataPointArr));
    positionArr->arr = positionData;
    positionArr->size = sizeOfArr;
    positionArr->arrOverTime = positionHeightOverTime;
    return positionArr;
}
dataPointArr* calculatePositionalData(JNIEnv *env, jdoubleArray *posDataJArr, int sizeOfArr, int lastExtremaIndex)
{
    jsize length = (*env)->GetArrayLength(env, *posDataJArr);
    jdouble positionDataJArr[length];
    (*env)->GetDoubleArrayRegion(env, *posDataJArr, 0, length, positionDataJArr);

    double **positionData = malloc(sizeof(double *) * (sizeOfArr - lastExtremaIndex + 1) + sizeof(double) * 2 * (sizeOfArr - lastExtremaIndex + 1));
    double *ptr = (double *)(positionData + (sizeOfArr - lastExtremaIndex + 1));
    for(int i = 0; i < (sizeOfArr - lastExtremaIndex + 1); i++)
    {
        *(positionData + i) = (ptr + 2 * i);
    }
    double **positionHeightOverTime = malloc(sizeof(double *) * (sizeOfArr - lastExtremaIndex + 1) + sizeof(double) * 2 * (sizeOfArr - lastExtremaIndex + 1));
    double *overTimePtr = (double *)(positionHeightOverTime + (sizeOfArr - lastExtremaIndex + 1));
    for(int i = 0; i < (sizeOfArr - lastExtremaIndex + 1); i++)
    {
        *(positionHeightOverTime + i) = (overTimePtr + 2 * i);
    }
    int count = (2 * lastExtremaIndex);
    for(int i = 0; i < (sizeOfArr - lastExtremaIndex); i++)
    {
        *(*(positionData + i)) = calculateXCoordinate(positionDataJArr[count], positionDataJArr[count + 1]);
        *(*(positionData + i) + 1) = calculateYCoordinate(positionDataJArr[count], positionDataJArr[count + 1]);

        *(*(positionHeightOverTime + i)) = (i + lastExtremaIndex) * (1/FREQUENCY_OF_TRACKER_SIGNAL);
        *(*(positionHeightOverTime + i) + 1) = calculateYCoordinate(positionDataJArr[count], positionDataJArr[count + 1]);
        count += 2;
    }
    dataPointArr *positionArr = malloc(sizeof(dataPointArr));
    positionArr->arr = positionData;
    positionArr->size = (sizeOfArr - lastExtremaIndex + 1);
    positionArr->arrOverTime = positionHeightOverTime;
    return positionArr;
}

localExtremaStruct* findLocalExtrema(dataPointArr *positionData, int *actualLastExtremaIndex, double *coefficents, int degree)
{
    printf("COF START\n");
    for(int i = 0; i <= degree; i++)
    {
        printf("%lf ", *(coefficents + i));
    }
    printf("\nEND\n");
    bool lastPointNegative = false;
    // Default condition if no change is found, just go until the end of the data set
    int localExtremaIndex = positionData->size - 1;
    printf("Before finding: %d\n", localExtremaIndex);
    double *newCoefficents = malloc(sizeof(double) * (degree));
    for(int i = 1; i <= degree; i++)
    {
        *(newCoefficents + i - 1) = *(coefficents + i) * i;
    }

    printf("NEWCOF START\n");
    for(int i = 0; i < degree; i++)
    {
        printf("%lf ", *(newCoefficents + i));
    }
    double result = 0.0;
    for(int k = 0; k < degree; k++)
    {
        printf("\nResult (Local Extrema): %lf\n", result);
        result += *(newCoefficents + k) * pow(*(*(positionData->arrOverTime)), k);
    }
    if(result < 0)
    {
        lastPointNegative = true;
    }

    for(int i = 1; i < positionData->size; i++)
    {
        result = 0.0;
        for(int k = 0; k < degree; k++)
        {
            result += *(newCoefficents + k) * pow(*(*(positionData->arrOverTime + i)), k);
        }
        if(result > 0)
        {
            if(lastPointNegative)
            {
                localExtremaIndex = i;
                *actualLastExtremaIndex += i;
                lastPointNegative = false;
                break;
            }
        }
        else
        {
            if(!lastPointNegative)
            {
                localExtremaIndex = i;
                *actualLastExtremaIndex += i;
                lastPointNegative = true;
                break;
            }
        }
    }
    printf("Local Extrema Index: %d\n", localExtremaIndex);
    localExtremaStruct *localExtrema = malloc(sizeof(localExtremaStruct));
    localExtrema->lastIndex = localExtremaIndex;
    localExtrema->lastExtremaConcentric = lastPointNegative;
    return localExtrema;
}

double calculateTime(int dataPointIndex)
{
    return dataPointIndex * (1.0/FREQUENCY_OF_TRACKER_SIGNAL);
}

double pythagoreanTheorem(double xDistance, double yDistance)
{
    return sqrt(pow(xDistance, 2.0) + pow(yDistance, 2.0));
}

double totalDistanceTraveledBetweenTwoPoints(double **positionData, int startPointIndex, int endPointIndex)
{
    double totalDistanceTraveled;
    totalDistanceTraveled = 0.0;
    for(int i = startPointIndex + 1; i <= endPointIndex; i++)
    {
        totalDistanceTraveled += pythagoreanTheorem(*(*(positionData + i)) - *(*(positionData + i - 1)), *(*(positionData + i) + 1) - *(*(positionData + i - 1) + 1));
    }
    return totalDistanceTraveled;
}

double currentVelocity(dataPointArr *positionData, int startPointIndex, int endPointIndex)
{
    printf("Distance traveled: %lf\n", totalDistanceTraveledBetweenTwoPoints(positionData->arr, startPointIndex, endPointIndex));
    return totalDistanceTraveledBetweenTwoPoints(positionData->arr, startPointIndex, endPointIndex) / (calculateTime(endPointIndex) - calculateTime(startPointIndex));
}

double* vectorProjection(double **positionData, int size, int degree)
{
    matrixArrStruct *positionDataStruct = malloc(sizeof(matrixArrStruct));
    positionDataStruct->rows = size;
    positionDataStruct->cols = 2;
    positionDataStruct->arr = positionData;
    matrixArrStruct **matrices = createMatrices(positionDataStruct, degree);
    matrixArrStruct *createdMatrix = *matrices;
    matrixArrStruct *transposedCreatedMatrix = *(matrices + 1);
    double **yVector = (double **)malloc(sizeof(double *) * positionDataStruct->rows + sizeof(double) * positionDataStruct->rows);
    double *ptr = (double *)(yVector + positionDataStruct->rows);
    for(int i = 0; i < positionDataStruct->rows; i++)
    {
        *(yVector + i) = (ptr + i);
    }
    for(int i = 0; i < positionDataStruct->rows; i++)
    {
        **(yVector + i) = *(*(positionDataStruct->arr + i) + 1);
    }

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
    double *finalArr = multiplyMatrix1D(inverseXTransposeResult, yVector);

    free(yVector);
    free(inverseXTransposeResult->arr);
    free(inverseXTransposeResult);
    return finalArr;
}
double averageForceProduction(dataPointArr *posDataArray, localExtremaStruct *localExtrema, double intialVelocity, int weight, bool inKG)
{
        int convertedWeight;
        if(!inKG)
        {
            convertedWeight = convertLBToKG(weight);
        }
        else
        {
            convertedWeight = weight;
        }
        double avgForce = 0.0;

       if(localExtrema->lastExtremaConcentric)
       {
            avgForce = (convertedWeight * (currentVelocity(posDataArray, 0, localExtrema->lastIndex) - intialVelocity) / (calculateTime(localExtrema->lastIndex) - calculateTime(0)));
       }
    return avgForce;
}
double forceProduction(dataPointArr *posDataArray, double intialVelocity, int weight, bool inKG)
{
    int convertedWeight;
    if(!inKG)
    {
        convertedWeight = convertLBToKG(weight);
    }
    else
    {
        convertedWeight = weight;
    }
    double force = fabs((convertedWeight * (currentVelocity(posDataArray, posDataArray->size - 2, posDataArray->size - 1) - intialVelocity) / calculateTime(1)));
    return force;
}

JNIEXPORT jdoubleArray JNICALL Java_com_example_main_1menu_helpers_PerformanceMetricHelper_performanceMetrics(JNIEnv *env, jobject obj, jdoubleArray posDataJArr, jint weight, jdouble intialVelocity, jboolean inKG)
{
    // If bar is going down defaults to zero
    jdouble *finalResults = calloc(3, sizeof(double));
    dataPointArr *positionDataStruct = newCalculatePositionalData(env, &posDataJArr, 2, 0);
    // If the bar went is going up and not down
    if(*(*(positionDataStruct->arr) + 1) < (*(*(positionDataStruct->arr + 1) + 1)))
    {
        *finalResults = forceProduction(positionDataStruct, intialVelocity, weight, inKG);
        *(finalResults + 1) = fabs(currentVelocity(positionDataStruct, positionDataStruct->size - 2, positionDataStruct->size - 1));
        *(finalResults + 2) = 1.0;
    }
    jdoubleArray finalResultsJArr = (*env)->NewDoubleArray(env, 3);
    (*env)->SetDoubleArrayRegion(env, finalResultsJArr, 0, 3, finalResults);
    return finalResultsJArr;
}