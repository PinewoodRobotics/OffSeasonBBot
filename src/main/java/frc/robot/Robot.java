package frc.robot;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import autobahn.client.AutobahnClient;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.constants.BotConstants;
import frc.robot.constants.PiConstants;
import frc.robot.subsystem.background.GlobalPosition;

public class Robot extends LoggedRobot {

  private Command m_autonomousCommand;
  public static AutobahnClient commuincation = new AutobahnClient(PiConstants.mainPiAddr);

  private RobotContainer m_robotContainer;
  // final XboxController m_controller = new XboxController(3);

  public Robot() {
    Logger.addDataReceiver(new NT4Publisher());
    switch (BotConstants.currentMode) {
      case REAL:
        Logger.addDataReceiver(new WPILOGWriter());
        Logger.addDataReceiver(new NT4Publisher());
        break;

      case SIM:
        Logger.addDataReceiver(new NT4Publisher());
        break;

      case REPLAY:
        setUseTiming(false); // Run as fast as possible
        String logPath = LogFileUtil.findReplayLog();
        Logger.setReplaySource(new WPILOGReader(logPath));
        Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
        break;
    }

    Logger.start();
    commuincation.begin().join();
  }

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    Logger.recordOutput("RobotPose", GlobalPosition.Get());
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    /*
     * SwerveSubsystem
     * .getInstance()
     * .setDefaultCommand(
     * new SwerveMoveTeleopController(
     * SwerveSubsystem.getInstance(),
     * m_controller));
     * 
     * setControllerBindings();
     */
  }

  public void setControllerBindings() {
    /*
     * new JoystickButton(m_controller, XboxController.Button.kA.value)
     * .onTrue(
     * SwerveSubsystem
     * .getInstance()
     * .runOnce(() -> {
     * SwerveSubsystem.getInstance().resetGyro();
     * }));
     * 
     * new JoystickButton(m_controller, XboxController.Button.kB.value)
     * .onTrue(
     * SwerveSubsystem
     * .getInstance()
     * .runOnce(() -> {
     * SwerveSubsystem.getInstance().masterDriveRawSwitch(true);
     * }));
     */
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
