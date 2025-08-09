package frc.robot.subsystem.background;

import org.littletonrobotics.junction.Logger;

import com.google.protobuf.InvalidProtocolBufferException;

import autobahn.client.Address;
import autobahn.client.AutobahnClient;
import autobahn.client.NamedCallback;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.BotConstants.Mode;
import frc.robot.constants.PiConstants;
import proto.util.Position.Position2d;
import proto.util.Position.RobotPosition;
import proto.util.Vector.Vector2;

public class GlobalPosition extends SubsystemBase {
  private static Pose2d lastEstimatedRobotPose = new Pose2d();
  private static long lastTimeMs;
  private static double confidence;
  private static GlobalPosition instance;

  public static GlobalPosition GetInstance(Mode botMode) {
    if (instance == null) {
      switch (botMode) {
        case SIM:
          GlobalPosition.SetClient(new AutobahnClient(new Address("localhost", 8080)));
          break;
        default:
          break;
      }

      instance = new GlobalPosition();
    }

    return instance;
  }

  public static void SetClient(AutobahnClient client) {
    Robot.commuincation = client;
  }

  public static void Initialize() {
    Robot.commuincation.subscribe(PiConstants.poseSubscribeTopics,
        NamedCallback.FromConsumer(GlobalPosition::subscription));
  }

  private static void subscription(byte[] data) {
    RobotPosition position = null;
    try {
      position = RobotPosition.parseFrom(data);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }

    assert position != null;

    lastTimeMs = (long) position.getTimestamp();

    Position2d pose2 = position.getPosition2D();
    Vector2 translation = pose2.getPosition();
    Vector2 direction = pose2.getDirection();
    lastEstimatedRobotPose = new Pose2d(new Translation2d(translation.getX(),
        translation.getY()), new Rotation2d(direction.getX(), direction.getY()));
    confidence = position.getConfidence();
  }

  public static Pose2d Get() {
    return lastEstimatedRobotPose;
  }

  public static long Time() {
    return lastTimeMs;
  }

  public static double Confidence() {
    return confidence;
  }

  @Override
  public void periodic() {
    Logger.recordOutput("GlobalPosition", GlobalPosition.Get());
  }
}
