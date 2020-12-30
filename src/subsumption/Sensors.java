package subsumption;
import simbad.sim.RangeSensorBelt;

public class Sensors {
   private RangeSensorBelt sonars;
   
   /*private LightSensor lightSensorLeft;
   private LightSensor lightSensorRight;
   public Sensors(RangeSensorBelt sonars,
	         LightSensor lightSensorLeft, LightSensor rightSensorRight) {
	      this(sonars);
	      this.lightSensorLeft = lightSensorLeft;
	      this.lightSensorRight = rightSensorRight;
	   }
   public String toString() {
	   return "[Sensors: sonars=" + sonars + ", lightSensorLeft=" + lightSensorLeft + ", lightSensorRight="+ lightSensorRight + "]";
   }
   */
   public Sensors(RangeSensorBelt sonars) {
      this.sonars = sonars;
   }

   public RangeSensorBelt getSonars() {
      return sonars;
   }

   public String toString() {
      String str = "[Sensors: sonars=" + sonars + "]";
      return str ;
   }
}
