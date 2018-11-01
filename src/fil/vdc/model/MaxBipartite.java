// A Java program to find maximal Bipartite matching.
package fil.vdc.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MaxBipartite
{
	private int numVM;
	private int numVMS;
	private int numPhy;
	private LinkedList<VirtualMachine> listVM;
	private LinkedList<PhysicalServer> listPhy;
	private boolean bpGraph[][];
    private int match[];
    private Map<VirtualMachine, PhysicalServer> result;
    private boolean isSuccess;
    private Map<Integer, VirtualMachine> mapVir;
    private Map<Integer, PhysicalServer> mapPhy;
    private int numSuccess;
    public MaxBipartite(LinkedList<VirtualMachine> listVM, LinkedList<PhysicalServer> listPhy)
    {
    	int minIDVir =Integer.MAX_VALUE;
    	int minIDPhy= Integer.MAX_VALUE;
    	for(VirtualMachine vir: listVM)
    		if(Integer.parseInt(vir.getNameVM())<minIDVir)
    			minIDVir = Integer.parseInt(vir.getNameVM());
    	for(PhysicalServer phy: listPhy)
    		if(Integer.parseInt(phy.getName()) < minIDPhy)
    			minIDPhy = Integer.parseInt(phy.getName());
    	this.numVM = listVM.size()+ minIDVir+1000; // stupid, make sure that bpGraph is big enough for storage id of VM
    	this.numPhy = listPhy.size()+minIDPhy+1000;// stupid, make sure that bpGraph is big enough for storage id of Physical machine
    	this.listVM = listVM;
    	this.listPhy = listPhy;
    	bpGraph = new boolean[this.numVM][this.numPhy]; // all element is false
    	match = new int[this.numPhy];
    	result = new HashMap<>();
    	isSuccess = false;
    	mapVir = new HashMap<>();
    	mapPhy = new HashMap<>();
    	for(VirtualMachine vir: listVM)
    		mapVir.put(Integer.parseInt(vir.getNameVM()), vir);
    	for(PhysicalServer phy: listPhy)
    		mapPhy.put(Integer.parseInt(phy.getName()), phy);
    	numSuccess =0;
    	numVMS =listVM.size();
 
    }
    // A DFS based recursive function that returns true if a
    // matching for vertex u is possible
    public boolean bpm(boolean bpGraph[][], int u, boolean seen[],
                int matchR[])
    {
        // Try every Phy one by one
        for (PhysicalServer phy: listPhy)
        {
            // If VM u can be matched with PM v and v
            // is not visited
            if (bpGraph[u][Integer.parseInt(phy.getName())] && !seen[Integer.parseInt(phy.getName())])
            {
                seen[Integer.parseInt(phy.getName())] = true; // Mark v as visited
 
                // If PM 'v' is not matched with a VM OR
                // previously matched VM for PM v (which
                // is matchR[v]) has an alternate PM available.
                // Since v is marked as visited in the above line,
                // matchR[v] in the following recursive call will
                // not get PM 'v' again
                if (matchR[Integer.parseInt(phy.getName())] < 0 || bpm(bpGraph, matchR[Integer.parseInt(phy.getName())],
                                         seen, matchR))
                {
                    matchR[Integer.parseInt(phy.getName())] = u;
                    return true;
                }
            }
        }
        return false;
    }
 
    // Returns maximum number of matching from M to N
    public int maxBPM(boolean bpGraph[][])
    {
        // An array to keep track of the applicants assigned to
        // jobs. The value of matchR[i] is the applicant number
        // assigned to job i, the value -1 indicates nobody is
        // assigned.
        int matchR[] = new int[this.numPhy];
 
        // Initially all jobs are available
        for(PhysicalServer phy: listPhy)
            matchR[Integer.parseInt(phy.getName())] = -1;
 
        int result = 0; // Count of jobs assigned to applicants
        for (VirtualMachine vir: listVM)
        {
            // Mark all jobs as not seen for next applicant.
            boolean seen[] =new boolean[this.numPhy] ;
            for(PhysicalServer phy: listPhy )
                seen[Integer.parseInt(phy.getName())] = false;
 
            // Find if the applicant 'u' can get a job
            if (bpm(bpGraph, Integer.parseInt(vir.getNameVM()), seen, matchR))
                result++;
        }
        for(PhysicalServer phy: listPhy) {
        	match[Integer.parseInt(phy.getName())]=matchR[Integer.parseInt(phy.getName())];
        }
        return result;
    }
    
    public void GenerateGraph() {
    	for(VirtualMachine vir: listVM) {
    		for(PhysicalServer phy: listPhy) {
    			if(vir.getMemory() <= phy.getRam() && vir.getCPU() <= phy.getCpu() ) {
    				bpGraph[Integer.parseInt(vir.getNameVM())][Integer.parseInt(phy.getName())]=true;
    			}
    			else bpGraph[Integer.parseInt(vir.getNameVM())][Integer.parseInt(phy.getName())] = false;
    		}
    	}
    	
    }

    public void run()
    {
    	GenerateGraph();
    	maxBPM(bpGraph);
//    	System.out.println( "Maximum number of VMs that can"+
//                " match PM is "+maxBPM(bpGraph));
        for(PhysicalServer phy: listPhy) {
        	if(match[Integer.parseInt(phy.getName())] == -1)
        		continue;
        	else
        	{
        		result.put(mapVir.get(match[Integer.parseInt(phy.getName())]), mapPhy.get(Integer.parseInt(phy.getName())));
        		numSuccess++;
        	}
        }
        if(numSuccess == numVMS)
        	isSuccess = true;
      
    }
    
	public Map<VirtualMachine, PhysicalServer> getResult() {
		return result;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
}