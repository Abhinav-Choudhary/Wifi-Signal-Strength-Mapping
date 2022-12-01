#include <stdio.h>
#include <vector>
#include <iostream>
#include <Eigen/Sparse>
#include <Eigen/SparseCore>
#include <Eigen/Dense>
#include <Eigen/SparseLU>
//#include <Eigen/UmfPackSupport>

using namespace std;
using namespace Eigen;

class EnergyComputationAlgo
{
public:
	int dimx, dimy;
	EnergyComputationAlgo(int dimx, int dimy);
	vector<vector<double> > computeSignalStrengths(vector<vector<double> > imageMatrix, int routerXpos, int routerYpos);

private:
	int calculateLinearIndex(int x, int y);
};
