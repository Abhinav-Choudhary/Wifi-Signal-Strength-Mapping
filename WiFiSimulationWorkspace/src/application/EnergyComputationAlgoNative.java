package application;

public class EnergyComputationAlgoNative {
	static {
		try {
			System.loadLibrary("libEnergyComputationAlgo");
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
		
	public native double[][] computeWifiEnergy(double[][] imageMatrix, int routerXPos, int routerYPos, int materialType, double freq);
}
