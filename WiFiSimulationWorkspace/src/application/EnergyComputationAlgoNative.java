package application;

public class EnergyComputationAlgoNative {
	static {
		try {
			String workspacePath = System.getProperty("user.dir");
			System.load(workspacePath+"/jni/libEnergyComputationAlgo.dylib");
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
		
	public native double[][] computeWifiEnergy(double[][] imageMatrix, int routerXPos, int routerYPos, int materialType, double freq);
}
