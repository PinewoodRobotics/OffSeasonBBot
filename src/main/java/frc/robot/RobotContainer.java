package frc.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.BotConstants;
import frc.robot.subsystem.GlobalPosition;

public class RobotContainer {

  List<Pose2d> testingWaypoints = new ArrayList<>(Arrays.asList(new Pose2d()));
  GlobalPosition globalPositionSubsystem;

  public RobotContainer() {
    configureBindings();
    globalPositionSubsystem = GlobalPosition.Build(BotConstants.currentMode);
  }

  private void configureBindings() {
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
