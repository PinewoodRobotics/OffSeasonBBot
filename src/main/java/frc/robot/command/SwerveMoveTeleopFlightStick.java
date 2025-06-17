package frc.robot.command;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.SwerveConstants;
import frc.robot.subsystem.SwerveSubsystem;
import org.pwrup.util.Vec2;
import pwrup.frc.core.controller.FlightModule;
import pwrup.frc.core.controller.FlightStick;
import pwrup.frc.core.geometry.TranslationMath;

public class SwerveMoveTeleopFlightStick extends Command {

  private final SwerveSubsystem m_swerveSubsystem;
  private final FlightModule controller;

  public SwerveMoveTeleopFlightStick(
    SwerveSubsystem swerveSubsystem,
    FlightModule controller
  ) {
    this.m_swerveSubsystem = swerveSubsystem;
    this.controller = controller;

    addRequirements(m_swerveSubsystem);
  }

  @Override
  public void execute() {
    double joystickRotation = TranslationMath.deadband(
      controller.leftFlightStick.getRawAxis(
        FlightStick.AxisEnum.JOYSTICKROTATION.value
      ) *
      -1,
      SwerveConstants.kRotDeadband,
      SwerveConstants.kRotMinValue
    );

    m_swerveSubsystem.drive(
      new Vec2(
        TranslationMath.deadband(
          controller.rightFlightStick.getRawAxis(
            FlightStick.AxisEnum.JOYSTICKY.value
          ),
          SwerveConstants.kXSpeedDeadband,
          SwerveConstants.kXSpeedMinValue
        ),
        TranslationMath.deadband(
          controller.rightFlightStick.getRawAxis(
            FlightStick.AxisEnum.JOYSTICKX.value
          ) *
          -1,
          SwerveConstants.kYSpeedDeadband,
          SwerveConstants.kYSpeedMinValue
        )
      ),
      joystickRotation,
      1
    );
  }

  @Override
  public void end(boolean interrupted) {
    m_swerveSubsystem.drive(new Vec2(0, 0), 0, 0);
  }
}
