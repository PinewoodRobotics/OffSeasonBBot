package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.command.SwerveMoveTeleopController;
import frc.robot.hardware.AHRSGyro;
import frc.robot.subsystem.SwerveSubsystem;
import frc.robot.subsystem.background.GlobalPosition;
import frc.robot.subsystem.background.PublicationSubsystem;

public class RobotContainer {

  XboxController controller = new XboxController(3);

  public RobotContainer() {
    configureBindings();

    AHRSGyro.getInstance();
    GlobalPosition.GetInstance();
    SwerveSubsystem.getInstance();
    PublicationSubsystem.GetInstance();

    PublicationSubsystem.register(GlobalPosition.GetInstance(), AHRSGyro.getInstance());
  }

  private void configureBindings() {
    new JoystickButton(controller, XboxController.Button.kA.value)
        .onTrue(new SwerveMoveTeleopController(
            SwerveSubsystem.getInstance(), controller));

    new JoystickButton(controller, XboxController.Button.kB.value)
        .onTrue(
            new InstantCommand(
                () -> SwerveSubsystem.getInstance().resetGyro()));

    new JoystickButton(controller, XboxController.Button.kX.value)
        .onTrue(
            new InstantCommand(
                () -> SwerveSubsystem.getInstance().masterDriveRawSwitch(true)));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
