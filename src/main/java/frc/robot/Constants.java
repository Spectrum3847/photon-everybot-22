package frc.robot;

public class Constants {

  public static class Drivetrain{
    public final static int driveLeftAnumber = 10;
    public final static int driveLeftBnumber = 11;
    public final static int driveRightAnumber = 20;
    public final static int driveRightBnumber = 21; 
  }
  //Varibles needed for the code
  public boolean armUp = true; //Arm initialized to up because that's how it would start a match

  //Motor Constants
  public static class intake{
    public final static int intakenumber = 31;
    public final static double speed = 1;
    public final static double slowerSpeed = 0.5;
  }
  public static class arm{
    //Constants for controlling the arm. consider tuning these for your particular robot
    public final static int armnumber = 30;
    public final static double armHoldUp = -0.03;
    public final static double armHoldDown = 0.22;
    public final static double armTravel = 0.5;
    public final static double armTravelup = -1;
    public final static double armTraveldown = 0.25;
    public final static double armTimeUp = 0.5;
    public final static double armTimeDown = 0.35;
    public final static double speed = 1;
    public final static double slowerSpeed = 0.3;


    public final static String up = "up";
    public final static String down = "down";
    public final static String notMoving = "notMoving";
    public final static String manual = "manual";
    }
}
