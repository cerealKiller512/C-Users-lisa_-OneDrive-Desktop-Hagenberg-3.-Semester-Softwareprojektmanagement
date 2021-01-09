/**
 * Robo_6.java
 * 
 * @author Michael Eder & Lisa Moser
 * @lastChanged December 21, 2020
 * 
 * This class contains the method 'autonomousMode' which is necessary
 * for the autonomous driving of the Lego Mindstorms robot.
 */
package Robo_6;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robo_6 {

  public static void autonomousMode(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor) {
    
    //declare sensors
    EV3TouchSensor touchLeft = new EV3TouchSensor(SensorPort.S1);
    EV3TouchSensor touchRight = new EV3TouchSensor(SensorPort.S2);
    EV3UltrasonicSensor ultraSonicFront = new EV3UltrasonicSensor (SensorPort.S3);
    
    //print status on LCD
    LCD.drawString("autonom Start!", 0, 0);
    Delay.msDelay(1000);
    Sound.beepSequenceUp();
    
    //as long as no table edge was detected, drive forward
    while (robotIsOnTable(touchLeft, touchRight))
    {
      moveForward(leftMotor, rightMotor);
      if (!ObstacleAhead(ultraSonicFront))
      {
        stopMotors(leftMotor, rightMotor);
        moveBackward(leftMotor, rightMotor, 2500);
        turn(leftMotor, 2000, 1000);
      }
    }
   
    stopMotorsAndCloseSensors(leftMotor, rightMotor, touchLeft, touchRight, ultraSonicFront);
    
    //print status on LCD
    Sound.twoBeeps();
    LCD.drawString("autonom Ende!",0,1);
    Delay.msDelay(1500);
  }

  /**
   * stopMotorsAndCloseSensors
   * This function stop all motors and closes all sensors ports
   * @param leftMotor - the left motor
   * @param rightMotor - the right motor
   * @param touchLeft - the left touch sensor
   * @param touchRight - the right touch sensor
   * @param ultraSonic - the ultrasonic sensor
   */
  private static void stopMotorsAndCloseSensors(RegulatedMotor leftMotor, RegulatedMotor rightMotor,
      EV3TouchSensor touchLeft, EV3TouchSensor touchRight, EV3UltrasonicSensor ultraSonic) {
    stopMotors(leftMotor, rightMotor);
    touchLeft.close();
    touchRight.close();
    ultraSonic.close();
    
  }

  /**
   * turn()
   * This method enables the robot to turn left or right, depending on the given motor
   * @param motor - the motor on the side, the robot should turn to
   * @param duration - the time how long the robot should turn
   * @param acceleration - the acceleration speed
   */
  private static void turn(RegulatedMotor motor, int duration, int acceleration) {
    motor.forward();
    motor.setAcceleration(acceleration);
    Delay.msDelay(duration);
    motor.stop();
    
  }

  /**
   * moveBackward
   * This function enables the robot to drive backward
   * @param leftMotor
   * @param rightMotor
   */
  private static void moveBackward(RegulatedMotor leftMotor, RegulatedMotor rightMotor, int duration) {
    leftMotor.setAcceleration(1000);
    rightMotor.setAcceleration(1000);
    leftMotor.synchronizeWith(new RegulatedMotor[] { rightMotor });
    leftMotor.backward();
    rightMotor.backward();
    Delay.msDelay(duration);
  }

  /**
   * stopMotors
   * This method stops the motors of the robot and desynchronize them
   * @param leftMotor - the left motor
   * @param rightMotor - the right motor
   */
  private static void stopMotors(RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
    leftMotor.startSynchronization();
    leftMotor.stop();
    rightMotor.stop();
    leftMotor.endSynchronization();    
  }

  /**
   * ObstacleAhead
   * This method checks, if their is a obstacle ahead of the robot by using the ultraSonic Sensor.
   * @param ultraSonic - the ultraSonicSensor
   * @return true, if there is a obstacle within a 30 cm range infront of the robot
   */
  private static boolean ObstacleAhead(EV3UltrasonicSensor ultraSonic) {
    ultraSonic.enable();
    int range = ultraSonic.sampleSize();
    float [] sampleRange = new float[range];
    ultraSonic.fetchSample(sampleRange, 0);
    return sampleRange[0]>0.3;
  }

  /**
   * robotIsOnTable
   * This method checks, if the robot is on a table or not.
   * Therefore it checks if both of the touchSensors are pressed.
   * @param touchLeft -  the left touch sensor on the robot
   * @param touchRight - the right touch sensor on the robot
   * @return true, if both touchSensors are pressed, else false
   */
  private static boolean robotIsOnTable(EV3TouchSensor touchLeft, EV3TouchSensor touchRight)
  {
    int sampleSizeLeft = touchLeft.sampleSize();
    int sampleSizeRight = touchRight.sampleSize();
    float[] sampleLeft = new float[sampleSizeLeft];
    float[] sampleRight = new float[sampleSizeRight];
    touchLeft.fetchSample(sampleLeft, 0);
    touchRight.fetchSample(sampleRight, 0);
    return (sampleLeft[0]==1 && sampleRight[0]==1);
  }
  
  /**
   * moveForward
   * This function enables the robot to move forward
   * @param leftMotor - the left motor of the robot
   * @param rightMotor - the right motor of the robot
   */
  private static void moveForward(RegulatedMotor leftMotor, RegulatedMotor rightMotor)
  {
    leftMotor.setAcceleration(1000);
    rightMotor.setAcceleration(1000);
    leftMotor.synchronizeWith(new RegulatedMotor[] { rightMotor });
    leftMotor.forward();
    rightMotor.forward();
  }
}