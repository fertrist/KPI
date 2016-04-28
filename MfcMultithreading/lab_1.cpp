// MFCLAB.cpp: определяет точку входа для консольного приложения.
//

#include "stdafx.h"
#include "MFCLAB.h"
#include <Windows.h>
#include <io.h>
#include <iostream>
//for CEvent class
#include <afxmt.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// Единственный объект приложения

CWinApp theApp;

using namespace std;

static const int N = 4;
static const int P = 4;
static int H = N / P;

UINT count1(LPVOID param);
UINT count2(LPVOID param);
UINT count3(LPVOID param);
UINT minD(LPVOID param);

void generateArray(int arr[]) {
	for (int i = 0; i < N; i++) {
		arr[i] = 1;
	}
}

void generateMultiArray(int arr[][N]) {
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			arr[i][j] = 1;
		}
	}
}

int getMin(int arr[], int start, int end) {
	int min = 1000000;
	for (int i = start; i < end; i++) {
		if (arr[i] < min) {
			min = arr[i];
		}
	}
	return min;
}

void initEmptyArray(int arr[]) {
	for (int j = 0; j < N; j++) {
		arr[j] = 0;
	}
}

class CommonData1 {
public:
	int A[N];
	int B[N];
	int D[N];
	int MC[N][N];
	int MZ[N][N];
	CommonData1() {
		generateArray(B);
		generateArray(D);
		generateMultiArray(MC);
		generateMultiArray(MZ);
		initEmptyArray(A);
		cout << "Common data1 initialized.\n";
	}

};

struct Data1{
public:
	CommonData1 *commonData;
	int p;
};

HANDLE t1;
HANDLE t2;
HANDLE t3;
HANDLE t4;

/*
А= В*МС + D*MZ;
*/
void lab1() {
	cout << "Lab_1: \n";
	t1 = CreateSemaphore(NULL, 0, 1, NULL);
	t2 = CreateSemaphore(NULL, 0, 1, NULL);
	t3 = CreateSemaphore(NULL, 0, 1, NULL);
	t4 = CreateSemaphore(NULL, 0, 1, NULL);
	//generate input
	CommonData1 *commonData = new CommonData1;
	Data1 data[P];
	for (int p = 0; p < P; p++) {
		data[p] = *new Data1;
		data[p].commonData = commonData;
		data[p].p = p;
		CWinThread *thread = AfxBeginThread(count1, &data[p]);
	}
	WaitForSingleObject(t1, INFINITE);
	WaitForSingleObject(t2, INFINITE);
	WaitForSingleObject(t3, INFINITE);
	WaitForSingleObject(t4, INFINITE);
	for (int i = 0; i < N; i++) {
		cout << commonData->A[i] << " ";
	}
}

UINT count1(LPVOID param){
	Data1 *data = (Data1*)param;
	printf("Thread#%d started.\n", data->p + 1);
	CommonData1 * cd = data->commonData;
	int end = (data->p + 1) * H;
	int start = end - H;
	//loop for MC and MZ; full B and D are used
	for (int i = start; i < end; i++) {
		int BMC = 0;
		int DMZ = 0;
		for (int x = 0; x < N; x++) {
			BMC += cd->B[x] * cd->MC[i][x];
			DMZ += cd->D[x] * cd->MZ[i][x];
		}
		cd->A[i] = BMC + DMZ;
	}
	printf("Thread#%d ended.\n", data->p + 1);
	switch (data->p){
	case 0: ReleaseSemaphore(t1, 1, NULL); break;
	case 1: ReleaseSemaphore(t2, 1, NULL); break;
	case 2: ReleaseSemaphore(t3, 1, NULL); break;
	case 3: ReleaseSemaphore(t4, 1, NULL); break;
	}
	return 0;
}

void initEmptyMultiArray(int arr[][N]) {
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			arr[i][j] = 0;
		}
	}
}

class CommonData2 {
public:
	int MB[N][N];
	int MK[N][N];
	int MC[N][N];
	int MX[N][N];
	int MA[N][N];
	CommonData2() {
		generateMultiArray(MC);
		generateMultiArray(MK);
		generateMultiArray(MB);
		generateMultiArray(MX);
		initEmptyMultiArray(MA);
		cout << "Common data2 initialized.\n";
	}

};

struct Data2{
public:
	CommonData2 *commonData;
	int p;
};

/*
МА= МВ*MK + МС*МХ;
*/
void lab2(){
	cout << "Lab_2: \n";
	t1 = CreateSemaphore(NULL, 0, 1, NULL);
	t2 = CreateSemaphore(NULL, 0, 1, NULL);
	t3 = CreateSemaphore(NULL, 0, 1, NULL);
	t4 = CreateSemaphore(NULL, 0, 1, NULL);
	//generate input
	CommonData2 *commonData = new CommonData2;
	Data2 data[P];
	for (int p = 0; p < P; p++) {
		data[p] = *new Data2;
		data[p].commonData = commonData;
		data[p].p = p;
		AfxBeginThread(count2, &data[p]);
	}
	WaitForSingleObject(t1, INFINITE);
	WaitForSingleObject(t2, INFINITE);
	WaitForSingleObject(t3, INFINITE);
	WaitForSingleObject(t4, INFINITE);
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			printf("%5d ", commonData->MA[i][j]);
		}
		cout << "\n";
	}
}

/*
МА= МВ*MK + МС*МХ;
*/
UINT count2(LPVOID param){
	Data2 *data = (Data2*)param;
	printf("Thread#%d started.\n", data->p + 1);
	CommonData2 * cd = data->commonData;
	int end = (data->p + 1) * H;
	int start = end - H;
	//loop for MK and MX; full MB and MC are used
	for (int i = start; i < end; i++) {
		for (int y = 0; y < N; y++) {
			int MBMK = 0;
			int MCMX = 0;
			for (int x = 0; x < N; x++) {
				MBMK += cd->MB[y][x] * cd->MK[x][i];
				MCMX += cd->MC[y][x] * cd->MX[x][i];
			}
			cd->MA[y][i] = MBMK + MCMX;
		}
	}
	printf("Thread#%d ended.\n", data->p + 1);
	switch (data->p){
	case 0: ReleaseSemaphore(t1, 1, NULL); break;
	case 1: ReleaseSemaphore(t2, 1, NULL); break;
	case 2: ReleaseSemaphore(t3, 1, NULL); break;
	case 3: ReleaseSemaphore(t4, 1, NULL); break;
	}
	return 0;
}

class CommonData3 {
public:
	int MD[N][N];
	int MT[N][N];
	int MZ[N][N];
	int ME[N][N];
	int MA[N][N];
	int D[N];
	int min = 100000;
	CommonData3() {
		generateMultiArray(MD);
		generateMultiArray(MT);
		generateMultiArray(MZ);
		generateMultiArray(ME);
		for (int i = 0; i < N; i++) {
			D[i] = 2;
		}
		D[N] = 1;
		initEmptyMultiArray(MA);
		cout << "Common data3 initialized.\n";
	}

	void writeMin(int m) {
		WaitForSingleObject(sem, INFINITE);
		if (m < min) { min = m; };
		ReleaseSemaphore(sem, 1, NULL);
	}

private:
	HANDLE sem = CreateSemaphore(NULL, 1, 1, NULL);
};

struct Data3{
public:
	CommonData3 *commonData;
	int p;
};


/*
MА= min(D)*MD*MT + MZ*ME.
*/
void lab3(){
	cout << "Lab_3: \n";
	t1 = CreateSemaphore(NULL, 0, 1, NULL);
	t2 = CreateSemaphore(NULL, 0, 1, NULL);
	t3 = CreateSemaphore(NULL, 0, 1, NULL);
	t4 = CreateSemaphore(NULL, 0, 1, NULL);
	//generate input
	CommonData3 *commonData = new CommonData3;
	Data3 data[P];
	for (int p = 0; p < P; p++) {
		data[p] = *new Data3;
		data[p].commonData = commonData;
		data[p].p = p;
		AfxBeginThread(minD, &data[p]);
	}
	WaitForSingleObject(t1, INFINITE);
	WaitForSingleObject(t2, INFINITE);
	WaitForSingleObject(t3, INFINITE);
	WaitForSingleObject(t4, INFINITE);
	printf("Lab_3: min was found: %d.\n", commonData -> min);
	for (int p = 0; p < P; p++) {
		AfxBeginThread(count3, &data[p]);
	}
	WaitForSingleObject(t1, INFINITE);
	WaitForSingleObject(t2, INFINITE);
	WaitForSingleObject(t3, INFINITE);
	WaitForSingleObject(t4, INFINITE);
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			printf("%5d ", commonData->MA[i][j]);
		}
		cout << "\n";
	}
}

UINT minD(LPVOID param){
	Data3 *data = (Data3*)param;
	printf("Thread#%d started. Finding min.\n", data->p + 1);
	CommonData3 * cd = data->commonData;
	int end = (data->p + 1) * H;
	int start = end - H;
	int min = 1000000;
	for (int i = start; i < end; i++) {
		if (cd->D[i] < min) {min = cd->D[i];}
	}
	cd->writeMin(min);
	printf("Thread#%d ended. Min found.\n", data->p + 1);
	switch (data->p){
		case 0: ReleaseSemaphore(t1, 1, NULL); break;
		case 1: ReleaseSemaphore(t2, 1, NULL); break;
		case 2: ReleaseSemaphore(t3, 1, NULL); break;
		case 3: ReleaseSemaphore(t4, 1, NULL); break;
	}
	return 0;
}

UINT count3(LPVOID param) {
	Data3 *data = (Data3*)param;
	printf("Thread#%d started.\n", data->p + 1);
	CommonData3 * cd = data->commonData;
	int end = (data->p + 1) * H;
	int start = end - H;
	//loop for MT and ME; full MD and MZ are used
	for (int i = start; i < end; i++) {
		for (int y = 0; y < N; y++) {
			int MDMT = 0;
			int MZME = 0;
			for (int x = 0; x < N; x++) {
				MDMT += cd->MD[y][x] * cd->MT[x][i];
				MZME += cd->MZ[y][x] * cd->ME[x][i];
			}
			cd->MA[y][i] = cd->min * MDMT + MZME;
		}
	}
	printf("Thread#%d ended.\n", data->p + 1);
	switch (data->p){
	case 0: ReleaseSemaphore(t1, 1, NULL); break;
	case 1: ReleaseSemaphore(t2, 1, NULL); break;
	case 2: ReleaseSemaphore(t3, 1, NULL); break;
	case 3: ReleaseSemaphore(t4, 1, NULL); break;
	}
	return 0;
}

/* Лабораторная  работа.  Разработать параллельную программу для вычисления трех математических функций:
   А= В*МС + D*MZ;
   МА= МВ*MK + МС*МХ;
   MА= min (D)*MD*MT + MZ*ME.
   Язык программирования С++.
   */
int _tmain(int argc, TCHAR* argv[], TCHAR* envp[])
{
	int nRetCode = 0;

	HMODULE hModule = ::GetModuleHandle(NULL);

	if (hModule != NULL)
	{
		// инициализировать MFC, а также печать и сообщения об ошибках про сбое
		if (!AfxWinInit(hModule, NULL, ::GetCommandLine(), 0))
		{
			// TODO: измените код ошибки соответственно своим потребностям
			_tprintf(_T("Критическая ошибка: сбой при инициализации MFC\n"));
			nRetCode = 1;
		}
		else
		{
			lab1();
			lab2();
			lab3();
			// TODO: Вставьте сюда код для приложения.
		}
	}
	else
	{
		// TODO: Измените код ошибки соответственно своим потребностям
		_tprintf(_T("Критическая ошибка: неудачное завершение GetModuleHandle\n"));
		nRetCode = 1;
	}

	return nRetCode;
}

