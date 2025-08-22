package frc.robot.command;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.SwerveConstants;
import frc.robot.subsystem.SwerveSubsystem;
import org.pwrup.util.Vec2;
import pwrup.frc.core.geometry.TranslationMath;

public class SwerveMoveTeleopController extends Command {

  private final SwerveSubsystem m_swerveSubsystem;
  private final XboxController controller;

  public SwerveMoveTeleopController(
      SwerveSubsystem swerveSubsystem,
      XboxController controller) {
    this.m_swerveSubsystem = swerveSubsystem;
    this.controller = controller;

    addRequirements(m_swerveSubsystem);
  }

  @Override
  public void execute() {
    double joystickRotation = Math.min(
        controller.getRawAxis(XboxController.Axis.kRightX.value),
        SwerveConstants.kRotMinValue);

    m_swerveSubsystem.drive(
        new Vec2(
            Math.min(
                controller.getRawAxis(XboxController.Axis.kLeftY.value) * -1,
                SwerveConstants.kXSpeedMinValue),
            Math.min(
                controller.getRawAxis(XboxController.Axis.kLeftX.value),
                SwerveConstants.kYSpeedMinValue)),
        joystickRotation,
        0.2);
  }

  @Override
  public void end(boolean interrupted) {
    m_swerveSubsystem.drive(new Vec2(0, 0), 0, 0);
  }
}
