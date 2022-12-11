#include <stdio.h>
#include <vector>
#include <iostream>
#include <Eigen/SparseCore>
#include <Eigen/SparseLU>

using namespace std;
using namespace Eigen;

class EnergyComputationAlgo
{
public:
	int dimx, dimy;
	EnergyComputationAlgo(int dimx, int dimy);
	vector<vector<double> > computeSignalStrengths(vector<vector<double> > imageMatrix, int routerXpos, int routerYpos, int materialType, double freq);

private:
	int calculateLinearIndex(int x, int y);
};
