
#include "../headers/application_EnergyComputationAlgoNative.h"
#include "../headers/EnergyComputationAlgo.h"

jobjectArray JNICALL Java_application_EnergyComputationAlgoNative_computeWifiEnergy
  (JNIEnv *env, jobject thisObject, jobjectArray imageMatrix, jint routerXPos, jint routerYPos, jint materialType, jdouble freq) {
	int dimx = env -> GetArrayLength(imageMatrix);
	int dimy = env -> GetArrayLength( (jdoubleArray) env -> GetObjectArrayElement(imageMatrix, 0));
	vector<vector<double> > imageMatrixNative(dimx);
	for(int i=0; i<dimx; i++) {
		double temp[dimy];
		env -> GetDoubleArrayRegion((jdoubleArray) env -> GetObjectArrayElement(imageMatrix, i), 0, dimy, temp);
		imageMatrixNative[i] = vector<double>(temp, temp+dimy);
	}
	EnergyComputationAlgo computer(dimx, dimy);
	imageMatrixNative = computer.computeSignalStrengths(imageMatrixNative, routerXPos, routerYPos, materialType, freq);

	jclass doubleClass = env->FindClass("[D");
	// Create the returnable 2D array
	jobjectArray processedImage = env -> NewObjectArray(dimx, doubleClass, NULL);

	// Go through the first dimension and add the second dimension arrays
	for (int i = 0; i < dimx; i++) {
		double temp[dimy];
		std::copy(imageMatrixNative[i].begin(), imageMatrixNative[i].end(), temp);

	    jdoubleArray doubleArray = env->NewDoubleArray(dimy);
	    env->SetDoubleArrayRegion(doubleArray, (jsize) 0, (jsize) dimy, (jdouble*) &temp);
	    env->SetObjectArrayElement(processedImage, (jsize) i, doubleArray);
	    env->DeleteLocalRef(doubleArray);
	}
	return processedImage;
}
