package subsumption;
import javax.vecmath.Vector3d;
import simbad.sim.*;

public class BehaviorBasedAgent extends Agent {

   private Behavior[] behaviors;
   private boolean suppresses[][];
   private int currentBehaviorIndex;
   private int previousBehavior = 0;
   private Sensors sensors;

   public BehaviorBasedAgent(Vector3d position, String name, int sensorCount) {
      super(position, name);
      // Add sensors
      RangeSensorBelt sonars = RobotFactory.addSonarBeltSensor(this,
            sensorCount);
      sensors = new Sensors(sonars);
         
      CameraSensor camera = RobotFactory.addCameraSensor(this);
      camera.rotateZ(-Math.PI/4);
   }

   public void initBehaviors(Behavior[] behaviors, boolean subsumptionRelationships[][]) {
      this.behaviors = behaviors;
      this.suppresses = subsumptionRelationships;
   }

   protected void initBehavior() {
      currentBehaviorIndex = 0;
   }

   protected void performBehavior() {
      boolean isActive[] = new boolean[behaviors.length];
      for (int i = 0; i < isActive.length; i++) {
         isActive[i] = behaviors[i].isActive();
      }
      boolean ranABehavior = false;
      while (!ranABehavior) {
         boolean runCurrentBehavior = isActive[currentBehaviorIndex];
         if (runCurrentBehavior) {
            for (int i = 0; i < suppresses.length; i++) {
               if (isActive[i] && suppresses[i][currentBehaviorIndex]) {
                  runCurrentBehavior = false;

                  break;
               }
            }
         }

         if (runCurrentBehavior) {
            if (currentBehaviorIndex < behaviors.length) {
                if (currentBehaviorIndex != previousBehavior) {
                    previousBehavior = currentBehaviorIndex;
                    System.out.println("Running " + behaviors[currentBehaviorIndex].toString());
                }
                Velocities newVelocities = behaviors[currentBehaviorIndex].act();
                this.setTranslationalVelocity(newVelocities.getTranslationalVelocity());
                this.setRotationalVelocity(newVelocities.getRotationalVelocity());
            }
            ranABehavior = true;
         }

         if (behaviors.length > 0) {
            currentBehaviorIndex = (currentBehaviorIndex + 1)
                  % behaviors.length;
         }
      }
   }

   public Sensors getSensors() {
      return sensors;
   }

   public String toString() {
      return "[BehaviorBasedAgent: behaviors=" + behaviors + ", suppresses="
            + suppresses + ", currentBehaviorIndex=" + currentBehaviorIndex
            + ", sensors=" + sensors + ", " + super.toString() + "]";
   }
}
