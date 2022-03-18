/*
  2022 everybot code
  written by carson graf 
  don't email me, @ me on discord
*/ 

/*
  This is catastrophically poorly written code for the sake of being easy to follow
  If you know what the word "refactor" means, you should refactor this code
*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.intake;

public class Robot extends TimedRobot {
  
  //Definitions for the hardware. Change this if you change what stuff you have plugged in
  private final CANSparkMax driveLeftA = new CANSparkMax(Constants.Drivetrain.driveLeftAnumber, MotorType.kBrushless);
  private final CANSparkMax driveLeftB = new CANSparkMax(Constants.Drivetrain.driveLeftBnumber, MotorType.kBrushless);
  private final CANSparkMax driveRightA = new CANSparkMax(Constants.Drivetrain.driveRightAnumber, MotorType.kBrushless);
  private final CANSparkMax driveRightB = new CANSparkMax(Constants.Drivetrain.driveRightBnumber, MotorType.kBrushless);
  private final CANSparkMax arm = new CANSparkMax(Constants.arm.armnumber, MotorType.kBrushless);
  private final CANSparkMax intake = new CANSparkMax(Constants.intake.intakenumber, MotorType.kBrushed);

  DifferentialDrive drive;

  XboxController driverController = new XboxController(0);

  //auto Constants
  private boolean goForAuto = true;
  private double autoStart = 0;

  private String armMovingState = Constants.arm.notMoving;
  private double armMovingTimediff = 0;
  private double armStartTime = 0;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    //Configure motors to turn correct direction. You may have to invert some of your motors
    driveLeftA.setInverted(true);
    driveLeftA.setIdleMode(IdleMode.kCoast);
    driveLeftA.burnFlash();
    driveLeftB.setInverted(true);
    driveLeftB.setIdleMode(IdleMode.kCoast);
    driveLeftB.burnFlash();
    driveRightA.setInverted(false);
    driveRightA.setIdleMode(IdleMode.kCoast);
    driveRightA.burnFlash();
    driveRightB.setInverted(false);
    driveRightB.setIdleMode(IdleMode.kCoast);
    driveRightB.burnFlash();

    //set Follower Motors (You only have to call the Leader motor)
    driveRightB.follow(driveRightA);
    driveLeftB.follow(driveLeftA);

    drive = new DifferentialDrive(driveLeftA, driveRightA);
    
    intake.setSmartCurrentLimit(60);
    intake.setInverted(false);
    intake.burnFlash();  

    
    arm.setInverted(true);
    arm.setSmartCurrentLimit(35);
    arm.setIdleMode(IdleMode.kBrake);
    arm.getEncoder().setPosition(0);
    arm.burnFlash();

    //add a thing on the dashboard to turn off auto if needed
    SmartDashboard.putBoolean("Go For Auto", true);
    goForAuto = SmartDashboard.getBoolean("Go For Auto", true);
  }

  public void robotPeriodic(){
    SmartDashboard.putNumber("Arm Position", arm.getEncoder().getPosition());
  }

  @Override
  public void autonomousInit() {
    armMovingState = Constants.arm.notMoving;

    //get a time for auton start to do events based on time later
    autoStart = Timer.getFPGATimestamp();
    //check dashboard icon to ensure good to do auto
    goForAuto = SmartDashboard.getBoolean("Go For Auto", true);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

    
    //get time since start of auto
    double autoTimeElapsed = Timer.getFPGATimestamp() - autoStart;
    if(goForAuto){
      //series of timed events making up the flow of auto
      //2 ball auton right center

      if(autoTimeElapsed < 1){
        arm.set(0.2);
        //gets intake in middle position
      }
      else if(autoTimeElapsed < 2){
        arm.set(0.0);
        drive.arcadeDrive(-0.47, 0);
        //sets robot infront of a ball = to 45 in
      }
      else if(autoTimeElapsed < 3 && autoTimeElapsed > 2){
        arm.set(0.15);
        intake.set(0.72);
        drive.arcadeDrive(-0.4, 0);
      }
      else if(autoTimeElapsed < 4.7 && autoTimeElapsed > 2.7){
        arm.set(0);
        intake.set(0.5);
        //need to change values for room due to bad carpet
        drive.arcadeDrive(0.1, 0.4);
      }
      else if(autoTimeElapsed < 8.3 && autoTimeElapsed > 4.7){
        intake.set(0);
        arm.set(0);
        drive.arcadeDrive(-0.4, 0);
      }
      else if(autoTimeElapsed < 8.91 && autoTimeElapsed > 8.3){
        drive.arcadeDrive(0.1, 0.4);
        intake.set(0);
        arm.set(0);
      }
      else if(autoTimeElapsed < 11 && autoTimeElapsed > 8.91){
        arm.set(-0.4);
        drive.arcadeDrive(-0.4, 0);
      }
      else if(autoTimeElapsed < 12 && autoTimeElapsed > 11){
        arm.set(Constants.arm.armHoldUp);
        drive.arcadeDrive(0, 0);
        intake.set(-1);
      }
      else{
        arm.set(Constants.arm.armHoldUp);
        drive.arcadeDrive(0, 0);
        intake.set(0);
      }
    }
    /*if(goForAuto){
      //series of timed events making up the flow of auto
      //2 ball auton for left side of the field

      if(autoTimeElapsed < 1){
        arm.set(0.2);
        //gets intake in middle position
      }
      else if(autoTimeElapsed < 2){
        arm.set(0.0);
        drive.arcadeDrive(-0.47, 0);
        //sets robot infront of a ball = to 45 in
      }
      else if(autoTimeElapsed < 3 && autoTimeElapsed > 2){
        arm.set(0.15);
        intake.set(0.72);
        drive.arcadeDrive(-0.4, 0);
      }
      else if(autoTimeElapsed < 4.7 && autoTimeElapsed > 2.7){
        arm.set(0);
        intake.set(0);
        //need to change values for room due to bad carpet
        drive.arcadeDrive(0.1, -0.402);
      }
      else if(autoTimeElapsed < 8 && autoTimeElapsed > 4.7){
        arm.set(-0.4);
        drive.arcadeDrive(-0.445, 0);
      }
      else if(autoTimeElapsed < 11 && autoTimeElapsed > 9){
        arm.set(Constants.arm.armHoldUp);
        drive.arcadeDrive(0, 0);
        intake.set(-1);
      }
      else{
        arm.set(Constants.arm.armHoldUp);
        drive.arcadeDrive(0, 0);
        intake.set(0);
      }
    }*/
    /*if(goForAuto){
      //series of timed events making up the flow of auto
      
        
        arm.set(0.3);
      if(autoTimeElapsed < 4){
        driveLeftA.set(0);
        driveRightA.set(0);
      }
      if(autoTimeElapsed < 4 && autoTimeElapsed > 1){
        //spit out the ball for three seconds
        intake.set(-0.7);
      }else if( autoTimeElapsed < 6 && autoTimeElapsed > 4){
        //stop spitting out the ball and drive backwards *slowly* for three seconds
        intake.set( 0);  
        driveLeftA.set(0.3);
        driveRightA.set(0.3);
      }else if(autoTimeElapsed < 8 && autoTimeElapsed > 6){
        //Rebecca changed this part -> replace w the below if it doesn't work 
        //intake.set( 0);
        //driveLeftA.set(0);
        //driveRightA.set(0);
        driveRightA.set(0.3); //turns right for 3 seconds, objective should be 180 degress, altho i'm not sure about the number of seconds needed to achieve that
        driveLeftA.set(0);
      }else if(autoTimeElapsed < 10 && autoTimeElapsed > 8){
        arm.set(0.3); //should be completely down
        intake.set(0.6); //supposedly intakes the ball
      }else if(autoTimeElapsed < 12 && autoTimeElapsed > 10){
        driveLeftA.set(0.3); //supposedly turns 180 to the left back to the original position for three seconds
        driveRightA.set(0); 
        intake.set(0);
        arm.set(-0.3); //arm up
      }else if (autoTimeElapsed < 14 && autoTimeElapsed > 12){
        driveRightA.set(0.5); //drives forward faster
        driveLeftA.set(0.5);
      }else if (autoTimeElapsed < 16 && autoTimeElapsed > 14){
        intake.set(-0.7); //spits the ball out at the goal! 
      }else{ 

        
        //do nothing for the rest of auto
        intake.set( 0);
        driveLeftA.set(0);
        driveRightA.set(0);
      }
      
    }*/
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    armMovingState = Constants.arm.notMoving;
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //Set up Motor Speeds based on (GTA) Style Driving.
    //double driveRight = -(driverController.getRightTriggerAxis() - driverController.getLeftTriggerAxis() - driverController.getLeftX());
    //double driveLeft =  -(driverController.getRightTriggerAxis() - driverController.getLeftTriggerAxis() + driverController.getLeftX());
    //Set drive Motors
    /*if(Math.abs(driveRight) > 0.1 || Math.abs(driveLeft) > 0.1){
      driveRightA.set(driveRight);
      driveLeftA.set(driveLeft);
    }
    else{
      driveRightA.set(0);
      driveLeftA.set(0);
    }*/
    double throttle = driverController.getRightTriggerAxis() - driverController.getLeftTriggerAxis();
    throttle = Math.abs(throttle) * throttle;
    double steering = -1 * driverController.getLeftX();
    steering = Math.abs(steering) * steering;
    drive.arcadeDrive(-1 * throttle, steering);


    //Controls
    /*if(driverController.getAButton()){
      intake.set(Constants.intake.speed);
      armDown();
    }
    else armUp();
    
    if(driverController.getBButton()){
      intake.set(-Constants.intake.speed);
      armDown();
    }*/

    if(Math.abs(driverController.getRightY()) > 0.1) {
      arm.set(driverController.getRightY() * 0.3);
      armMovingState = Constants.arm.manual;
    }
    else if (driverController.getXButton()){ //Hold Down while intaking
      arm.set(Constants.arm.armHoldDown);
    }
    else {
      arm.set(-Constants.arm.armHoldUp);
      armMovingState = Constants.arm.notMoving;
    }
    


    if(driverController.getXButton()){
      intake.set(.6); 
    } else if(driverController.getYButton()){
      intake.set(-1);
    } else{
      intake.stopMotor();
    }

  }

/*
  private   void armUp() {
    if(armStartTime == 0){
      armStartTime = Timer.getFPGATimestamp();
    }
    armMovingTimediff =  Timer.getFPGATimestamp() - armStartTime;
    //Guard
    if(armMovingState.equals(Constants.arm.up)){
      if(armMovingTimediff >= Constants.arm.armTimeUp){
        arm.stopMotor(); //arm should hold on break mode, if brakemode doesnt hold change this to: arm.set(Constants.arm.armHoldup)
        armStartTime = 0;
      }
      return;  
    };
    
    arm.set(Constants.arm.armTravelup);
    armMovingState = Constants.arm.up;
  }

  
  private void armDown() {
    if(armStartTime == 0){
      armStartTime = Timer.getFPGATimestamp();
    }
    armMovingTimediff =  Timer.getFPGATimestamp() - armStartTime;
    //Guard
    if(armMovingState.equals(Constants.arm.down)){
      if(armMovingTimediff >= Constants.arm.armTimeDown){
        arm.stopMotor(); //arm should hold on break mode, if brakemode doesnt hold change this to: arm.set(Constants.arm.armHoldDown)
        armStartTime = 0;
      }
      return;
    };
    
    arm.set(Constants.arm.armTraveldown);
    armMovingState = Constants.arm.down;
  }
*/
  @Override
  public void disabledInit() {
    armMovingState = Constants.arm.notMoving;
    //On disable turn off everything
    //done to solve issue with motors "remembering" previous setpoints after reenable
    driveLeftA.setIdleMode(IdleMode.kBrake);
    driveRightA.setIdleMode(IdleMode.kBrake);
    driveLeftA.set(0);
    driveRightA.set(0);
    arm.set(0);
    intake.set(0);
  }
    
}