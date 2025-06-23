package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.command.SwerveMoveTeleopController;
import frc.robot.subsystem.SwerveSubsystem;

public class Robot extends TimedRobot {

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  final XboxController m_controller = new XboxController(3);

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    SwerveSubsystem
      .getInstance()
      .setDefaultCommand(
        new SwerveMoveTeleopController(
          SwerveSubsystem.getInstance(),
          m_controller
        )
      );

    setControllerBindings();
  }

  public void setControllerBindings() {
    new JoystickButton(m_controller, XboxController.Button.kA.value)
      .onTrue(
        SwerveSubsystem
          .getInstance()
          .runOnce(() -> {
            SwerveSubsystem.getInstance().resetGyro();
          })
      );

    new JoystickButton(m_controller, XboxController.Button.kB.value)
      .onTrue(
        SwerveSubsystem
          .getInstance()
          .runOnce(() -> {
            SwerveSubsystem.getInstance().masterDriveRawSwitch(true);
          })
      );
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
