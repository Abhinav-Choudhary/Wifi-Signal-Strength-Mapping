package application;

public class EnergyComputationAlgoNative {
	static {
		try {
			System.load("D:\\Northeastern\\CSYE6200\\Source\\wifi-simulator-csye6200\\WiFiSimulationWorkspace\\jni\\libEnergyComputationAlgo.dll");
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
		
	public native double[][] computeWifiEnergy(double[][] imageMatrix, double routerXPos, double routerYPos);
}
