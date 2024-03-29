# Wi-Fi Signal Strength Mapping Application Using Java and C++ with Java Native Interface

### Description
Wi-Fi signal strength mapping application is a project that aims to provide the most ideal spot to place the Wi-Fi router, ensuring the entirety of the house receives an adequate reception. The system enables users to enter their floor plan as a PNG image, modify certain parameters like frequency and material of the walls and then pick a spot to place the router, the system then takes those inputs and generates a heatmap image which displays the flow and reach of the signals. 

### Project Structure
Two separate branches have been created to maintain code for MAC and Windows operating Systems listed as the following 
- [main-mac-systems](https://github.com/Abhinav-Choudhary/Wifi-Signal-Strength-Mapping/tree/main-mac-systems)
- [main-windows-systems](https://github.com/Abhinav-Choudhary/Wifi-Signal-Strength-Mapping)

### Implementation
**A. C++ Compiler and Eclipse IDE Setup**
A C++ compiler such as MinGW is required to be installed in Windows operating systems. This compiler needs to be made available throughout the system by adding it to System Environment. MAC has GCC toolchains installed by default. Eclipse C/C++ IDE CDT needs to be installed to support development of C++ project.

**B. Eigen C++ library**
Eigen C++ template library for linear algebra(version 3.4.0) has to be downloaded, unzipped and added to an accessible location. The path to the Eigen headers folder in the downloaded package needs to be added to C++ compiler includes. Also, the includes headers path to JDK needs to be added to the C++ compiler includes.

**C. Signal Strength Computation Algorithm**
The core logic for Wi-Fi signal strength computation has been implemented using the Eigen Linear Algebra library. The complex sparse matrices use the SparseMatrix<double> class from SparseCore package. An array of triplets having sparse matrix coordinates and values is built. This is pushed into the sparse matrix in one operation using setFromTriplets function. This accelerated the sparse matrix construction process which was very slow otherwise with regular iteration. The sparse matrix linear equation was solved using SparseLU<SparseMatrix<complex<double>>> Decomposition solver and the resulting vector was reshaped into a two-dimensional image matrix using the reshaped function which is a column major vector to matrix converter.

**D. Java Native Interface (JNI)**
A class having the interfacing methods, EnergyComputationAlgoNative was created on the Java project. The interfacing function has the keyword ‘native’ to help java compiler understand that the function needs to be added to JNI bridging headers. External tools configuration needs to be added specifying the path to Java compiler, the destination of the bridging header file and the source file having the bridging functions with native keywords in the Java project. On running the external tools configuration, JNI bridging header file is generated. Implementation is written for this header on C++ project where the input parameter objects are mapped to C++ data structures. In our case, a two-dimensional double matrix, double values and integers were the parameters passed. This bridging function should also map the C++ data structures back to JNI objects while returning from the function.
Header files were also written on the C++ project for code segregation. The actual algorithm was written inside another C++ class called EnergyComputationAlgo. An instance object of this class was created and the function for computing was invoked from the JNI bridging function.
The C++ compiler needs to be configured to generate dll or dylib shared dynamic libraries (based on the OS) instead of the standard executable file. Also, NDEBUG flag has to be added to C++ pre-processor and -O3 optimisation needs to be added to the compiler flags in the C++ compiler settings. Upon building the C++ project, the generated dynamic library is saved to the configured destination.

**E. Java JNI Integration**
The generated C++ dynamic library is copied and added to the Java project workspace. This library is dynamically loaded using the System.load function. Whenever a call to the function with native keyword (in our case computeWifiEnergy) is made, Java dynamically loads this library and calls the matching function on the JNI bridging header. This in turn invokes the C++ function and we retrieve the results. The resulting matrix values are rescaled to a range of 1-100 in order to map them to different color values. A color map of hundred colors using a base color specified is created and final matrix is built by mapping this to the matrix. This matrix is converted back to an image by PixelWriter.

**F. Multithreading**
The energy computation algorithm is a heavy process as there is a matrix decomposition operation involved. This makes it a slow process and running it on main thread causes the UI to freeze. In order to make UX better, the heavy computation was switched to a background thread using the CalculationThread class which implements Runnable interface. Once the calculation operation is complete and a response is received from the C++ dynamic library, we have to continue the UI operation through a callback function. For this, a Callback interface is written and the class calling this thread operation(RouterController) implements the callback interface function. The callback function is invoked from inside Platform.runLater block to run the UI operations again on the main thread.

**G. JavaFX UI**
The UI is created using .fxml files and .java controller files. The fxml files are updated using Scene Builder by dragging and dropping various elements. These elements have been assigned an Id, and linked to certain controller methods at various events like mouse click. Saperate controller files are created corresponding to each page and each .fxml file. The .fxml files are linked to their respective controller files and a .css file to implement design of the application. A "Properties" class file is created which contains static data fields and methods, the different controller use the static getter and setter methods to update or retrieve the values present in the data fields. Finally, everything is wrapped using a scene and stage. The movement between different screens is achieved by updating the scenes with the root of different .fxml files and setting the stage with the new scene.
