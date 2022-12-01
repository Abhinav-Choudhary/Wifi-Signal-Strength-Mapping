//============================================================================
// Name        : EnergyComputationAlgo.cpp
// Author      : Hariharan Sundaram
// Version     :
// Copyright   : Your copyright notice
// Description : Wifi Energy Computation in C++
//============================================================================

#include "../headers/EnergyComputationAlgo.h"

typedef Triplet<complex<double> > ComplexTriplet;

	double π = 3.141592653589793238;
	double λ = 0.12;
	double k = (2 * π) / λ;
	double δ = 0.03;
	int dimx, dimy;
	// Refraction index for concrete 2.55 - 0.01im. The imaginary part conveys the absorption.
	complex<double> η_concrete = complex<double>(2.55, -0.01);
	// Refraction index for air 1.0
	complex<double> η_air = complex<double>(1.0, 0.0);

	EnergyComputationAlgo::EnergyComputationAlgo(int dimx, int dimy) {
		this -> dimx = dimx;
		this -> dimy = dimy;
	}

	vector<vector<double> > EnergyComputationAlgo::computeSignalStrengths(vector<vector<double> > imageMatrix, int routerXpos, int routerYpos) {
		cout<<"Hello from Signal Strength Computer!\n"<<endl;

		pair<int, int> routerPos(routerXpos, routerYpos);
		MatrixXcd plan(dimx,dimy);
		complex<double> airConstant = pow(k / η_air, 2);
		complex<double> concreteConstant = pow(k / η_concrete, 2);
		for(int i=0; i<dimx; i++) {
			for(int j=0; j<dimy; j++) {
				if(isnan(imageMatrix[i][j]) || imageMatrix[i][j] < 0.1) imageMatrix[i][j] = 0.0;
			}
		}
		cout<<"Air Constant: "<<airConstant<<endl;
		cout<<"Concrete Constant: "<<concreteConstant<<endl;

		for(int i=0; i<dimx; i++) {
			for(int j=0; j<dimy; j++) {
				if (imageMatrix[i][j] != 0) plan(i, j) = airConstant;
				else plan(i, j) = concreteConstant;
			}
		}

		cout<<"Plan: "<<plan<<endl;

		vector<ComplexTriplet> tripletList;
		tripletList.reserve(5*dimx*dimy);

		for (int x = 1; x <= dimx; x++) {
			for (int y = 1; y <= dimy; y++) {
				int xm = (x + dimx - 2) % dimx + 1;
				int xp = x % dimx + 1;
				int ym = (y + dimy - 2) % dimy + 1;
				int yp = y % dimy + 1;

				int xVal = 0;
				int yVal = 0;
				complex<double> vVal = 0;

				xVal = calculateLinearIndex(x, y);
				yVal = calculateLinearIndex(x, y);
				vVal = plan(x-1, y-1) - (2 * pow(δ, -2));
				tripletList.push_back(ComplexTriplet(xVal, yVal, vVal));

				xVal = calculateLinearIndex(x, y);
				yVal = calculateLinearIndex(xp, y);
				vVal = pow(δ, -2);
				tripletList.push_back(ComplexTriplet(xVal, yVal, vVal));

				xVal = calculateLinearIndex(x, y);
				yVal = calculateLinearIndex(xm, y);
				vVal = pow(δ, -2);
				tripletList.push_back(ComplexTriplet(xVal, yVal, vVal));

				xVal = calculateLinearIndex(x, y);
				yVal = calculateLinearIndex(x, yp);
				vVal = pow(δ, -2);
				tripletList.push_back(ComplexTriplet(xVal, yVal, vVal));

				xVal = calculateLinearIndex(x, y);
				yVal = calculateLinearIndex(x, ym);
				vVal = pow(δ, -2);
				tripletList.push_back(ComplexTriplet(xVal, yVal, vVal));
			}
			cout<<"Processing"<<endl;
		}

		long fsize = dimx * dimy;
		SparseMatrix<complex<double> > s(fsize,fsize);
		SparseMatrix<complex<double> > f(dimx,dimy);

		cout<<"Building Sparse Matrix"<<endl;
		// Create sparse matrix
		s.setFromTriplets(tripletList.begin(), tripletList.end());
		cout<<"Sparse Matrix Successfully Built"<<endl;

		// Create router position vector
		for (int x = routerPos.first; x < routerPos.first + 5; x++) {
			for (int y = routerPos.second; y < routerPos.second + 5; y++) {
				f.insert(x, y) = complex<double>(1.0, 0.0);
			}
		}

		cout<<f<<endl;

		f = (MatrixXcd(f).reshaped()).sparseView();

		cout<<"Done Major chunk"<<endl;

//		LeastSquaresConjugateGradient<SparseMatrix<complex<double> > > solver;
//		solver.setMaxIterations(200);
//		// fill A and b;
//		// Compute the ordering permutation vector from the structural pattern of A
//		solver.compute(s);
//		cout<<"Solving Matrix"<<endl;
//		//Use the factors to solve the linear system
//		SparseMatrix<complex<double> > energyMatrix = solver.solve(f);

		s.makeCompressed();
		SparseLU<SparseMatrix<complex<double> > > solver;
		solver.analyzePattern(s);   // for this step the numerical values of A are not used
		solver.factorize(s);
		SparseMatrix<complex<double> > energyMatrix = solver.solve(f);

		MatrixXcd reshapedEnergyMatrix = MatrixXcd(energyMatrix).reshaped(dimx, dimy);

		cout<<reshapedEnergyMatrix<<"\n\n\n"<<endl;

		double minVal = 100000000;
		for(int i=0; i<dimx; i++) {
			for(int j=0; j<dimy; j++) {
				imageMatrix[i][j] = abs(real(reshapedEnergyMatrix(i, j)));
				minVal = min(imageMatrix[i][j], minVal);
			}
		}

		cout<<"Calculated Energy Matrix"<<endl;
		for(int i=0; i<dimx; i++) {
			for(int j=0; j<dimy; j++) {
				imageMatrix[i][j] -= minVal;
				cout<<imageMatrix[i][j]<<" ";
			}
			cout<<"\n";
		}
		cout<<endl;
		return imageMatrix;
	}

	int EnergyComputationAlgo::calculateLinearIndex(int x, int y) {
		return (y-1)*dimx+(x-1);
	}

//	int main() {
//	    std::cout << "Hello World 1!\n"<<endl;
//		double image[5][5] = {
//			{1.0, 2.0, 3.0, 4.0, 5.0},
//			{1.0, 2.0, 3.0, 4.0, 5.0},
//			{1.0, 2.0, 3.0, 4.0, 5.0},
//			{1.0, 2.0, 3.0, 4.0, 5.0},
//			{1.0, 2.0, 3.0, 4.0, 5.0} };
//		vector<vector<double> > imageMatrix(5);
//		for(int i=0; i<5; i++) {
//			imageMatrix[i] = vector<double>(image[i], image[i]+5);
//	 	}
//		EnergyComputationAlgo computer(5, 5);
//		cout<<"Computing"<<endl;
//		computer.computeSignalStrengths(imageMatrix, 0, 0);
//	    return 0;
//	}
