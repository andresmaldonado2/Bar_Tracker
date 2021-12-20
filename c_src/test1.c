#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <time.h>
#include <inttypes.h>
#include <math.h>
#include "matrixLib.h"

void testTotalDistanceTraveledBetweenTwoPoints(void)
{
    double testArr[2][2] = {
        {0.0, 0.0},
        {1.0, 5.0}
    };
    double **actualTestArr = (double **)malloc(sizeof(double *) * 2 + sizeof(double) * 2 * 2);
    double *ptr = (double *)(actualTestArr + 2);
    for(int i = 0; i < 2; i++)
    {
        *(actualTestArr + i) = (ptr + 2 * i);
    }
    for(int i = 0; i < 2; i++)
    {
        for(int z = 0; z < 2; z++)
        {
            *(*(actualTestArr + i) + z) = testArr[i][z];
        }
    }
    double distance = totalDistanceTraveledBetweenTwoPoints(actualTestArr, 0, 1);
    printf("Distance: %lf\n", distance);
}

void testCurrentVelocity(void)
{
    dataPointArr *positionData = malloc(sizeof(dataPointArr));
    double testArr[2][2] = {
        {0.0, 0.0},
        {0.0, 0.004}
    };
    double **actualTestArr = (double **)malloc(sizeof(double *) * 2 + sizeof(double) * 2 * 2);
    double *ptr = (double *)(actualTestArr + 2);
    for(int i = 0; i < 2; i++)
    {
        *(actualTestArr + i) = (ptr + 2 * i);
    }
    for(int i = 0; i < 2; i++)
    {
        for(int z = 0; z < 2; z++)
        {
            *(*(actualTestArr + i) + z) = testArr[i][z];
        }
    }
    double overTimeTestArr[2][2] = {
        {0.0, 0.0},
        {0.02, 0.004}
    };
    double **actualOverTimeTestArr = (double **)malloc(sizeof(double *) * 2 + sizeof(double) * 2 * 2);
    double *overTimePtr = (double *)(actualOverTimeTestArr + 2);
    for(int i = 0; i < 2; i++)
    {
        *(actualOverTimeTestArr + i) = (overTimePtr + 2 * i);
    }
    for(int i = 0; i < 2; i++)
    {
        for(int z = 0; z < 2; z++)
        {
            *(*(actualOverTimeTestArr + i) + z) = testArr[i][z];
        }
    }
    positionData->arr = actualOverTimeTestArr;
    positionData->arrOverTime = actualTestArr;
    positionData->size = 2;
    double velocity = currentVelocity(positionData, 0, 1);
    printf("Velocity: %lf\n", velocity);
}

void testCalculatePositionalData(void)
{
    int sizeOfArr = 2;
    int lastExtremaIndex = 0;
    double testArr[2][2] = {
        {20.0, 60},
        {10.0, 30}
    };
    double *actualTestArr = malloc(sizeof(double) * 4);
    for(int i = 0; i < sizeOfArr * 2; i += 2)
    {
        *(actualTestArr + i) = testArr[i][0];
        *(actualTestArr + i + 1) = testArr[i][1];
    }

    double **positionData = malloc(sizeof(double *) * sizeOfArr + sizeof(double) * 2 * sizeOfArr);
    double *ptr = (double *)(positionData + sizeOfArr);
    for(int i = 0; i < sizeOfArr; i++)
    {
        *(positionData + i) = (ptr + 2 * i);
    }
    int count = (2 * lastExtremaIndex);
    // Change the size variable here doesn't look like it makes sense
    for(int i = 0; i < (sizeOfArr - lastExtremaIndex); i++)
    {
        *(*(positionData + i)) = calculateXCoordinate(actualTestArr[count], actualTestArr[count + 1]);
        *(*(positionData + i) + 1) = calculateYCoordinate(actualTestArr[count], actualTestArr[count + 1]);
        printf("NORMAL X: %lf Y: %lf\n", positionData[i][0], positionData[i][1]);
        count += 2;
    }
}

double* testVectorProjection(void)
{
    double testArr[5][2] = {
            {0,1},
            {2,0},
            {3,3},
            {4,5},
            {5,4}
    };
    double **actualTestArr = (double **)malloc(sizeof(double *) * 5 + sizeof(double) * 5 * 2);
    double *ptr = (double *)(actualTestArr + 5);
    for(int i = 0; i < 5; i++)
    {
        *(actualTestArr + i) = (ptr + 2 * i);
    }
    for(int i = 0; i < 5; i++)
    {
        for(int z = 0; z < 2; z++)
        {
            *(*(actualTestArr + i) + z) = testArr[i][z];
        }
    }
    double *results = vectorProjection(actualTestArr, 5, 3);
    printf("Vector Project: ");
    for(int i = 0; i < 4; i++)
    {
        printf("%lf ", *(results + i));
    }
    printf("\n");
    return results;
}

void testFindLocalExtrema(void)
{
    dataPointArr *testDataPointArrStruct = malloc(sizeof(dataPointArr));
    double **actualTestArr = (double **)malloc(sizeof(double *) * 10 + sizeof(double) * 10 * 2);
    double *ptr = (double *)(actualTestArr + 10);
    for(int i = 0; i < 10; i++)
    {
        *(actualTestArr + i) = (ptr + 2 * i);
    }
    int z = 0;
    for(int i = 2; i < 12; i++)
    {
        *(*(actualTestArr + z)) = i;
        *(*(actualTestArr + z) + 1) = pow(i - 2, 3) + pow(i - 2, 2) - (i -2) + 1;
        z++;
    }
    double *coefficients = vectorProjection(actualTestArr, 10, 3);
    testDataPointArrStruct->arr = NULL;
    testDataPointArrStruct->arrOverTime = actualTestArr;
    testDataPointArrStruct->size = 5;
    int *testIndex;
    *testIndex = 1;
    localExtremaStruct *results = findLocalExtrema(testDataPointArrStruct, testIndex, coefficients, 3);
    printf("Actual index: %d\n", *testIndex);
    printf("Local Extrema Index: %d\n", results->lastIndex);
}

int main(void)
{
    /*
    matrixArrStruct *testStruct = malloc(sizeof(matrixArrStruct));
    testStruct->rows = 3;
    testStruct->cols = 3;
    double testArr[3][3] = {
                {2,4,9},
                {7,6,1},
                {5,9,3}
    };
    double **actualTestArr = (double **)malloc(sizeof(double *) * 3 + sizeof(double) * 3 * 3);
    double *ptr = (double *)(actualTestArr + 3);
    for(int i = 0; i < 3; i++)
    {
        *(actualTestArr + i) = (ptr + 3 * i);
    }
    for(int i = 0; i < 3; i++)
    {
        for(int z = 0; z < 3; z++)
        {
            *(*(actualTestArr + i) + z) = testArr[i][z];
        }
    }
    testStruct->arr = actualTestArr;
    double **testingArr = malloc(sizeof(double *) * 3 + sizeof(double) * 3);
    double *testPtr = (double *)(testingArr + 3);
    for(int i = 0; i < 3; i++)
    {
        *(testingArr + i) = (testPtr + 1 * i);
    }
    **(testingArr) = 8;
    **(testingArr + 1) = 6;
    **(testingArr + 2) = 3;
    double *resultArr = multiplyMatrix1D(testStruct, testingArr);
    
    printf("START\n");
    for(int i = 0; i < 3; i++)
    {
        printf("%lf ", *(resultArr + i));
    }
    printf("END\n");
    */


    testCurrentVelocity();
    testTotalDistanceTraveledBetweenTwoPoints();
    testVectorProjection();
    testCalculatePositionalData();
    testFindLocalExtrema();
    return 0;
}