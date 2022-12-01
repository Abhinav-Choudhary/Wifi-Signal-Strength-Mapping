package application;

public class EnergyComputationAlgoNative {
	static {
		try {
			System.load("/Users/hasundaram/Documents/WiFiSimulationCSYE/WifiSimulationWorkspace/jni/libEnergyComputationAlgo.dylib");
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
		
	public native double[][] computeWifiEnergy(double[][] imageMatrix, int routerXPos, int routerYPos);
}
