package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * This is a demo program showing the use of the navX MXP to implement a "rotate
 * to angle" feature.
 *
 * This example will automatically rotate the robot to one of four angles (0,
 * 90, 180 and 270 degrees).
 *
 * This rotation can occur when the robot is still, but can also occur when the
 * robot is driving. When using field-oriented control, this will cause the
 * robot to drive in a straight line, in whathever direction is selected.
 *
 * This example also includes a feature allowing the driver to "reset" the "yaw"
 * angle. When the reset occurs, the new gyro angle will be 0 degrees. This can
 * be useful in cases when the gyro drifts, which doesn't typically happen
 * during a FRC match, but can occur during long practice sessions.
 *
 * Note that the PID Controller coefficients defined below will need to be tuned
 * for your drive system.
 */

public class Robot extends TimedRobot {
  AHRS ahrs = new AHRS(SPI.Port.kMXP);
  public double currentAngle;
  private double goalAngle = 10.0;
  private boolean isDone = false;
  private double speed = 0.5;
  private double tolerance = 5;

  // // Channels for the wheels
  public static final int leftChannel = 3;
  public static final int leftBackChannel = 6;
  public static final int rightChannel = 11;
  public static final int rightBackChannel = 2;
  WPI_TalonSRX leftMotor = new WPI_TalonSRX(leftChannel);
  WPI_TalonSRX rightMotor = new WPI_TalonSRX(rightChannel);
  WPI_TalonSRX leftBackMotor = new WPI_TalonSRX(leftBackChannel);
  WPI_TalonSRX rightBackMotor = new WPI_TalonSRX(rightBackChannel);
  DifferentialDrive drivetrain;
  CommandBase auto;

  public Robot() {

  }

  @Override
  public void robotInit() {
    // auto = new TurnWithoutPID();
    // auto.initialize();
    ahrs.reset();
    leftBackMotor.follow(leftMotor);
    rightBackMotor.follow(rightMotor);
    drivetrain = new DifferentialDrive(leftMotor, rightMotor);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
    // Robot.drivingSubsystem.stop();

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

    CommandScheduler.getInstance().run();

    // auto.schedule();
    // if (auto != null)
    //   auto.schedule();
    // if (auto.isFinished() == true) {
    //   auto.cancel();
    // }


  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    currentAngle = ahrs.getAngle();
    System.out.println("Gyro: " + currentAngle);
    CommandScheduler.getInstance().run();
    if((Math.abs(goalAngle - currentAngle) < tolerance)){  
      //if within tolerance, the robot will stop
      drivetrain.arcadeDrive(0, 0);
      isDone = true;
    } else if(currentAngle < goalAngle) {  //If left of target angle turn clockwise
      drivetrain.arcadeDrive(0, -speed);  
    } else if(currentAngle > goalAngle){  //If right of target angle turn counterclockwise
      drivetrain.arcadeDrive(0, speed);  
    }
    // if (auto.isFinished() == true) {
    //   auto.cancel();
    // }

  
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopInit() {

    // TODO Auto-generated method stub
    super.teleopInit();
    auto.cancel();

  }

  @Override
  public void teleopPeriodic() {

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}