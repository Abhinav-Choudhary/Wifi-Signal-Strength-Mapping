#include "../headers/EnergyComputationAlgo.h"

int main() {
    std::cout << "Hello World 1!\n"<<endl;
	double image[5][5] = {
		{1.0, 2.0, 3.0, 4.0, 5.0},
		{1.0, 2.0, 3.0, 4.0, 5.0},
		{1.0, 2.0, 3.0, 4.0, 5.0},
		{1.0, 2.0, 3.0, 4.0, 5.0},
		{1.0, 2.0, 3.0, 4.0, 5.0} };
	vector<vector<double> > imageMatrix(5);
	for(int i=0; i<5; i++) {
		imageMatrix[i] = vector<double>(image[i], image[i]+5);
 	}
	EnergyComputationAlgo computer(5, 5);
	cout<<"Computing"<<endl;
	computer.computeSignalStrengths(imageMatrix, 0, 0, 1, 2.4);
    return 0;
}
