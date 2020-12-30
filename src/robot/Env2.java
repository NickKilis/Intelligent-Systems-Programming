package robot;

import javax.vecmath.*;
import simbad.sim.*;

public class Env2 extends EnvironmentDescription {
    public Env2(){
        setupEnvironment();
    }
    public void setupEnvironment() {
        //addObstacles();
        addLights();
    }

    private void addLights() {
        light1IsOn=true;
        light2IsOn=true;
        light1Color= new Color3f(1,0,0);
        light1Position = new Vector3d(6,0,6);
// Far from the robot
        light2Position = new Vector3d(6,3,6);
// In front of the robot
//        light2Position = new Vector3d(-6,2,-6);
// Above the robot for getting the max lux values
//        light2Position = new Vector3d(-9,2,-6);
        
    }
}
