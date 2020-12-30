package subsumption;

public abstract class Behavior {

   private Sensors sensors;
   private static final int ROTATION_COUNT = 20;
   protected static final double TRANSLATIONAL_VELOCITY = 0.4;

   public Behavior(Sensors sensor) {
      this.sensors = sensor;
   }

   public abstract Velocities act();
   public abstract boolean isActive();

   protected Sensors getSensors() {
      return sensors;
   }

   public String toString() {
      return "[Behavior: " + super.toString() + "]";
   }
   public static int getRotationCount() {
	  return ROTATION_COUNT;
   }
}
