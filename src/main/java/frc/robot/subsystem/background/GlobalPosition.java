package frc.robot.subsystem.background;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.Logger;

import com.google.protobuf.InvalidProtocolBufferException;

import autobahn.client.Address;
import autobahn.client.AutobahnClient;
import autobahn.client.NamedCallback;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.BotConstants.Mode;
import frc.robot.constants.PiConstants;
import frc.robot.hardware.AHRSGyro;
import frc.robot.subsystem.SwerveSubsystem;
import proto.sensor.GeneralSensorDataOuterClass.GeneralSensorData;
import proto.sensor.Odometry.OdometryData;
import proto.util.Position.Position2d;
import proto.util.Position.RobotPosition;
import proto.util.Vector.Vector2;
import pwrup.frc.core.hardware.sensor.IGyroscopeLike;
import pwrup.frc.core.proto.IDataClass;

@AutoLog()
public class GlobalPosition extends SubsystemBase implements IDataClass {
  private static Pose2d lastEstimatedRobotPose = new Pose2d();
  private static long lastTimeMs;
  private static double confidence;
  private static GlobalPosition instance;

  private SwerveDriveOdometry odometry;
  private IGyroscopeLike gyro;
  private Pose2d lastPose;

  public static GlobalPosition GetInstance(Mode botMode) {
    if (instance == null) {
      if (botMode != null) {
        switch (botMode) {
          case SIM:
            GlobalPosition.SetClient(new AutobahnClient(new Address("localhost", 8080)));
            break;
          default:
            break;
        }
      }

      instance = new GlobalPosition(AHRSGyro.GetInstance());
    }

    return instance;
  }

  public static GlobalPosition GetInstance() {
    return GetInstance(null);
  }

  public GlobalPosition(IGyroscopeLike gyro) {
    this.gyro = gyro;
    this.odometry = new SwerveDriveOdometry(SwerveSubsystem.GetInstance().getKinematics(),
        new Rotation2d(Math.toRadians(gyro.getYaw())), SwerveSubsystem.GetInstance().getSwerveModulePositions());
  }

  public static void SetClient(AutobahnClient client) {
    Robot.communication = client;
  }

  public static void Initialize() {
    Robot.communication.subscribe(PiConstants.AutobahnConfig.poseSubscribeTopic,
        NamedCallback.FromConsumer(GlobalPosition::subscription)).join();
  }

  private static void subscription(byte[] data) {
    RobotPosition position = null;

    try {
      position = RobotPosition.parseFrom(data);
    } catch (InvalidProtocolBufferException e) {
      System.err.println("Invalid protocol buffer exception GlobalPosition.subscription()");
      e.printStackTrace();
      return;
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
    var positions = SwerveSubsystem.GetInstance().getSwerveModulePositions();
    var rawRotation = Rotation2d.fromDegrees(-gyro.getYaw());
    odometry.update(rawRotation, positions);
    lastPose = odometry.getPoseMeters();

    Logger.recordOutput("odometry/pose", lastPose);
    Logger.recordOutput("global/position", lastEstimatedRobotPose);
  }

  @Override
  public byte[] getRawConstructedProtoData() {
    var rawRotation = Rotation2d.fromDegrees(-gyro.getYaw());
    ChassisSpeeds speeds = SwerveSubsystem.GetInstance().getGlobalChassisSpeeds(rawRotation);
    Logger.recordOutput("odometry/speeds", speeds);

    return GeneralSensorData.newBuilder().setOdometry(OdometryData.newBuilder()
        .setVelocity(Vector2
            .newBuilder()
            .setX((float) speeds.vxMetersPerSecond)
            .setY((float) speeds.vyMetersPerSecond)
            .build())
        .setPosition(
            Position2d
                .newBuilder()
                .setPosition(
                    Vector2
                        .newBuilder()
                        .setX((float) lastPose.getX())
                        .setY((float) lastPose.getY())
                        .build())
                .setDirection(
                    Vector2
                        .newBuilder()
                        .setX((float) lastPose.getRotation().getCos())
                        .setY((float) lastPose.getRotation().getSin())
                        .build())
                .build())
        .build())
        .setSensorId("odom")
        .setTimestamp(System.currentTimeMillis()).build().toByteArray();
  }

  @Override
  public String getPublishTopic() {
    return "robot/odometry";
  }
}
