package robot;

import javax.vecmath.*;
import simbad.sim.*;

public class Env extends EnvironmentDescription {
    public Env(){
        setupEnvironment();
    }
    public void setupEnvironment() {
        addObstacles();
        addLights();
    }

    private void addObstacles() {
        add(new Box(new Vector3d(-6,0,-6),new Vector3f(2f,1.5f,2f),this));
        
        add(new Arch(new Vector3d(0,0,0),this));
        add(new Wall(new Vector3d(0,0,-10),20,1.5f,this));
        add(new Wall(new Vector3d(0,0,10),20,1.5f,this));
        add(new Wall(new Vector3d(-7.5,0,0),5,1.5f,this));
        add(new Wall(new Vector3d(-3,0,5),10,1.5f,this));
        add(new Wall(new Vector3d(7.5,0,0),5,1.5f,this));
        Wall w1 =new Wall(new Vector3d(-10,0,0),20,1.5f,this);
        w1.rotate90(1);
        add(w1);
        Wall w2 =new Wall(new Vector3d(10,0,0),20,1.5f,this);
        w2.rotate90(1);
        add(w2);
        Wall w3 =new Wall(new Vector3d(1,0,1),10,1.5f,this);
        w3.rotate90(1);
        add(w3);
        Wall w4 =new Wall(new Vector3d(5,0,-1),5,1.5f,this);
        w4.rotate90(1);
        add(w4);
        
    }

    private void addLights() {
        light1IsOn=true;
        light2IsOn=true;
        light1Color= new Color3f(1,0,0);
        light1Position = new Vector3d(6,0,6);
        light2Position = new Vector3d(6,3,6);
    }
}

