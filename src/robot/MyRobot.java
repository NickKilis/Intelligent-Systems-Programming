package robot;
import simbad.sim.*;
import subsumption.Behavior;
import subsumption.BehaviorBasedAgent;
import subsumption.Sensors;
import subsumption.Velocities;
import javax.vecmath.Vector3d;
//===========================================================================
public class MyRobot extends BehaviorBasedAgent {
    private LightSensor lightL;
    private LightSensor lightR;
    public MyRobot (Vector3d position, String name, int sensorCount) {
        super(position,name, sensorCount);
        lightL = RobotFactory.addLightSensorLeft(this);
        lightR = RobotFactory.addLightSensorRight(this);
        Sensors sensors = this.getSensors();
        Behavior[] behaviors = { new ReachGoal(sensors, this), 
        						 new CircumNavigate(sensors, this, false),
                                 new FollowLight(sensors, this),
                                 new FollowLine(sensors, this)};
        boolean subsumes[][] = { { false, true, true, true },
                                 { false, false, true, true },
                                 { false, false, false, true },
                                 { false, false, false, false }};
        this.initBehaviors(behaviors, subsumes);
    }
    public LightSensor getLightLeftSensor() {
        return lightL;
    }
    public LightSensor getLightRightSensor() {
        return lightR;
    }
}
//===========================================================================
class FollowLine extends Behavior {
    LightSensor lightR;
    LightSensor lightL;
    public FollowLine(Sensors sensors, MyRobot robot) {
        super(sensors);
        lightL = robot.getLightLeftSensor();
        lightR = robot.getLightRightSensor();
    } 
    public Velocities act() {
        float left = (float)Math.pow(lightL.getLux(),0.1);
		float right = (float)Math.pow(lightR.getLux(),0.1);
        float translationalV = (float)(3*(1-left+right));
        float rotationalV= 0.0f;
        return new Velocities(translationalV,rotationalV);
    }
    public boolean isActive() {
        return true;
    }
}
//===========================================================================
class FollowLight extends Behavior {
    float offset = 0.001f;
    LightSensor lightR;
    LightSensor lightL;
    public FollowLight(Sensors sensors, MyRobot robot) {
        super(sensors);
        lightL = robot.getLightLeftSensor();
        lightR = robot.getLightRightSensor();
    }
    public Velocities act() {
    	//float luxL = (float)lightL.getLux();
    	//float luxR = (float)lightR.getLux();
    	float left = (float)Math.pow(lightL.getLux(),0.1);
		float right = (float)Math.pow(lightR.getLux(),0.1);
		//return new Velocities (0.0, 50 * (left-right));
		//float difference=Math.abs(left);
		//make the robot always turn to the side which has the higher sensor value
			if (right < left) {
	        	float translationalV = 0.0f;
	        	float rotationalV = (float)(5*(1-left));
	        	return new Velocities(translationalV,rotationalV);
	        }
	        else {
	        	float translationalV = 0.0f;
	        	float rotationalV = -(float)(5*(1-left));
	        	return new Velocities(translationalV,rotationalV);
	        }
    }
    public boolean isActive() {
    	float left = (float)Math.pow(lightL.getLux(),0.1);
		float right = (float)Math.pow(lightR.getLux(),0.1);
		if (Math.abs(left-right)<offset){
			return false;
			}
		else {
			return true;
		}
    }
}
//===========================================================================
class ReachGoal extends Behavior {
    LightSensor lightR;
    LightSensor lightL;
    public ReachGoal(Sensors sensors, MyRobot robot) {
        super(sensors);
        lightL = robot.getLightLeftSensor();
        lightR = robot.getLightRightSensor();
    }
    public Velocities act() {
        return new Velocities(0.0, 0.0);
    }
    public boolean isActive() {
        float left=(float)lightL.getLux();
        float right=(float)lightR.getLux();
        if (left >= 0.5136 || right >= 0.5136 ) {
            return true;
        }
        /*float luxL = (float)Math.pow(lightL.getLux(),0.1);
        //float luxR = (float)Math.pow(lightR.getLux(),0.1);
        //if (1-luxL <= 0.01f || 1-luxR <= 0.01f ) {
        //    return true;
        }*/
        else {
        	return false;
        }
    }
}
//===========================================================================
class CircumNavigate extends Behavior{
    boolean CLOCKWISE;
    static double SAFETY = 1.2f;
    boolean completed;
    //double luxL = 0;
    //double luxR = 0;
    LightSensor lightR;
    LightSensor lightL;
    public CircumNavigate(Sensors sensors, MyRobot robot, boolean CLOCKWISE) {
        super(sensors);
        completed=true;
        this.CLOCKWISE = CLOCKWISE;
        
        lightL = robot.getLightLeftSensor();
        lightR = robot.getLightRightSensor();
    }
    public Velocities act() {
        RangeSensorBelt sonars = getSensors().getSonars();
        
        float translationalV = 0.2f;
        float rotationalV = 0.2f;
        if (sonars.getFrontQuadrantHits() > 0 && sonars.getMeasurement(minFrontMeasurement(sonars)) < SAFETY) {
            // Turn until you no longer see the obstacle
            translationalV = 0;
            if (CLOCKWISE) {
                rotationalV = 0.2f;
            }
            return new Velocities(translationalV,rotationalV);
        }
        if (CLOCKWISE) {
            if (sonars.getRightQuadrantHits() > 0) {
                if (sonars.getRightQuadrantHits() == 3) {
                    return new Velocities(translationalV,0);
                }
                if (sonars.getMeasurement(minRightMeasurement(sonars)) < SAFETY) {
                    translationalV = 0;
                    return new Velocities(translationalV, rotationalV);
                }
            }
        }
        else {
            if (sonars.getLeftQuadrantHits() > 0) {
                if (sonars.getLeftQuadrantHits() == 3) {
                    return new Velocities(translationalV,0);
                }
                if (sonars.getMeasurement(minLeftMeasurement(sonars)) < SAFETY) {
                    translationalV = 0;
                    return new Velocities(translationalV,-rotationalV);
                }
            }
        }
        int min = minMeasurement(sonars);
        if (sonars.getFrontQuadrantHits() > 0) {
            rotationalV = -Math.min(1, (float)Math.abs(2f - sonars.getMeasurement(min)));
            if (CLOCKWISE) {
                rotationalV = -rotationalV;
            }
        }
        return new Velocities(translationalV,rotationalV);
    }
    public boolean isActive() {
    	float luxL = (float)Math.pow(lightL.getLux(),0.1);
        float luxR = (float)Math.pow(lightR.getLux(),0.1);
        RangeSensorBelt sonars = getSensors().getSonars();
        if (!completed){
            if (sonars.getFrontQuadrantHits() == 0) {
                if (CLOCKWISE) {
                    if (!rightHasHit()) {
                        completed = true;
                        System.out.println("Right sensors detect nothing");
                        return false;
                    }
                }
                else if (!leftHasHit()) {
                    completed = true;
                    System.out.println("Left sensors detect nothing");
                    return false;
                }
                if (1-luxL <= 0.001f || 1-luxR <= 0.001f ) {
                    completed = true;
                    System.out.println("Greater lum in both light sensors");
                    return false;
                }
            }
            return true;
        }
        if (sonars.getFrontQuadrantHits() > 0) {
            completed=false;
            float left =(float)lightL.getLux();
            float right =(float)lightR.getLux();
            this.CLOCKWISE = left < right;
            return true;
        }
        return false;
    }
    private int minFrontMeasurement(RangeSensorBelt sonars) {
        int min = 0;
        for (int i=0; i < 2; i++) {
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min)) min = i;
        }
        if (sonars.getMeasurement(11)<sonars.getMeasurement(min)) min = 11;

        return min;
    }
    private int minLeftMeasurement(RangeSensorBelt sonars) {
        int min = 2;
        for (int i=3; i < 5; i++) {
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min)) min = i;
        }
        return min;
    }
    private int minRightMeasurement(RangeSensorBelt sonars) {
        int min = 8;
        for (int i=8; i < 11; i++) {
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min)) min = i;
        }
        return min;
    }
    private int minMeasurement(RangeSensorBelt sonars) {
        int min = minFrontMeasurement(sonars);
        int offset = 8;
        if (CLOCKWISE) {
            offset = 2;
        }
        for (int i = offset; i<offset+3; i++) {
            if (sonars.getMeasurement(i)<sonars.getMeasurement(min)) min = i;
        }
        return min;
    }
    private boolean leftHasHit() {
        if (getSensors().getSonars().hasHit(2)) {
            return true;
        }
        if (getSensors().getSonars().hasHit(3)) {
            return true;
        }
        return false;
    }
    private boolean rightHasHit() {
        if (getSensors().getSonars().hasHit(9)) {
            return true;
        }
        if (getSensors().getSonars().hasHit(10)) {
            return true;
        }
        return false;
    }
}
