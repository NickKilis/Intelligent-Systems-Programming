package robot;
import simbad.gui.Simbad;
import simbad.sim.EnvironmentDescription;
import subsumption.BehaviorBasedAgent;

import javax.vecmath.Vector3d;
/* Select between two worlds for the robot :
	1. Env : enclosed space with many obstacles,one arch and the light source far away.
	2. Env2: free space with no obstacles, with only the light source far away.
*/
public class Main {
    public static void main(String[] args) {	
    //EnvironmentDescription environment = new Env2();
    EnvironmentDescription environment = new Env();
    BehaviorBasedAgent robot = new MyRobot(new Vector3d(-9,0,-6), "Robot", 12);
    environment.add(robot);
    Simbad frame = new Simbad(environment, false);
    }
}
