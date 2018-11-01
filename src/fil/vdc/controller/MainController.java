package fil.vdc.controller;
import fil.vdc.model.Result;
/**
 * @author Van Huynh Nguyen
 *
 */
public class MainController {
	private static int N; // number of experimental
	private static int stepPercent;
	private static int startLoadPercent;
	private static int endLoadPercent;
	private static int numberVMMax;
	
	public static Result logLoad;
	public static Result resultAcceptionRate;
	public static Result resultPowerPerAccept;
	
	public static Result resultAcceptionRateVDC;
	public static Result resultPowerPerAcceptVDC;
	
	public static Result resultPowerPerVDC;
	
//	public static Result logHEAE;
	public static Result logGreenHead;
	public static Result logSecondNet;
	public static Result logMigration;
	public static Result logJoinRemap;
	public static Result resultPowerConsumption;
	public static Result logAcceptCentralized;
	public static Result logAcceptDistributed;
//	public static Result logMappingDistributed;
//	public static Result logMapping;
	
	public static final boolean isEnableLog = false;
	
	public static void main(String[] args) { 
		
//		N = Integer.parseInt(args[0]);
//		startLoadPercent=Integer.parseInt(args[1]);
//		stepPercent=Integer.parseInt(args[2]);
//		endLoadPercent = Integer.parseInt(args[3]);
//		numberVMMax = Integer.parseInt(args[4]);
		
		N = 30;
		startLoadPercent= 10;
		stepPercent=10;
		endLoadPercent = 100;
		numberVMMax = 24;
		logLoad = new Result("logLoadUtil");
		
		resultAcceptionRate = new Result("resultAcceptionRate");
		resultPowerPerAccept = new Result("resultPowerPerAccept");
		resultAcceptionRateVDC = new Result("resultAcceptionRateVDC");
		resultPowerPerAcceptVDC = new Result("resultPowerPerAcceptVDC");
		resultPowerConsumption = new Result("resultPowerConsumption");
//		logMapping = new Result("MappingCentralized");
		logAcceptCentralized = new Result("logAcceptCentralied");
		resultPowerPerVDC = new Result("resultPowerPerVDC");
		logAcceptDistributed = new Result("logAcceptDistributed");
//		logMappingDistributed = new Result("MappingDistributed");
		
//		logHEAE = new Result("logHEAE");
		logGreenHead = new Result("logGreenHead");
		logSecondNet = new Result("logSecondNet");
		logMigration = new Result("logMigration");
		logJoinRemap = new Result("logJoinRemap");
		
		for(int percent = startLoadPercent; percent <=endLoadPercent; percent+=stepPercent) {
	
			for (int i = 0; i < N; i++) {
//				logHEAE.info("***********************************************************************");
//				logGreenHead.info("***********************************************************************");
//				logSecondNet.info("***********************************************************************");
//				logMigration.info("***********************************************************************");
//				logJoinRemap.info("***********************************************************************");
				System.out.println(i + "-"+percent);	
				Mapping map = new Mapping();
				// run mapping
				map.run(percent, numberVMMax);
			}			
		}
	}

}
