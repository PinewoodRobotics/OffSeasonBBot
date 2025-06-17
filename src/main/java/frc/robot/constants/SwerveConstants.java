package frc.robot.constants;

import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.geometry.Translation2d;

public class SwerveConstants {

  public static final Translation2d rearLeftTranslation = new Translation2d(
    0.38,
    0.38
  );

  public static final Translation2d rearRightTranslation = new Translation2d(
    0.38,
    -0.38
  );

  public static final Translation2d frontRightTranslation = new Translation2d(
    -0.38,
    -0.38
  );

  public static final Translation2d frontLeftTranslation = new Translation2d(
    -0.38,
    0.38
  );

  public static final SensorDirectionValue kFrontLeftCANcoderDirection =
    SensorDirectionValue.Clockwise_Positive;
  public static final SensorDirectionValue kFrontRightCANcoderDirection =
    SensorDirectionValue.Clockwise_Positive;
  public static final SensorDirectionValue kRearLeftCANcoderDirection =
    SensorDirectionValue.Clockwise_Positive;
  public static final SensorDirectionValue kRearRightCANcoderDirection =
    SensorDirectionValue.Clockwise_Positive;

  // magnetic offset for the CANCoders
  // you can find these by connecting to the RoboRIO by USB on the drive station,
  // opening the Phoenix Tuner app, and taking snapshots of
  // the rotational values of the CANCoders while in they are in the forward state
  public static final double kFrontLeftCANcoderMagnetOffset = 0.328;
  public static final double kFrontRightCANcoderMagnetOffset = 0.347;
  public static final double kRearLeftCANcoderMagnetOffset = -0.192;
  public static final double kRearRightCANcoderMagnetOffset = 0.499;

  // the driving motor ports
  public static final int kFrontLeftDriveMotorPort = 25;
  public static final int kFrontRightDriveMotorPort = 10;
  public static final int kRearLeftDriveMotorPort = 12;
  public static final int kRearRightDriveMotorPort = 21;

  // whether the driving encoders are flipped
  public static final boolean kFrontLeftDriveMotorReversed = true;
  public static final boolean kRearLeftDriveMotorReversed = true;
  public static final boolean kFrontRightDriveMotorReversed = true;
  public static final boolean kRearRightDriveMotorReversed = true;

  // the turning motor ports
  public static final int kFrontLeftTurningMotorPort = 20;
  public static final int kFrontRightTurningMotorPort = 23;
  public static final int kRearLeftTurningMotorPort = 13;
  public static final int kRearRightTurningMotorPort = 22;

  // whether the turning enoders are flipped
  public static final boolean kFrontLeftTurningMotorReversed = false;
  public static final boolean kFrontRightTurningMotorReversed = false;
  public static final boolean kRearLeftTurningMotorReversed = false;
  public static final boolean kRearRightTurningMotorReversed = false;

  // the CANCoder turning encoder ports - updated 2/12/24
  public static final int kFrontLeftCANcoderPort = 2;
  public static final int kFrontRightCANcoderPort = 3;
  public static final int kRearLeftCANcoderPort = 1;
  public static final int kRearRightCANcoderPort = 4;

  // stats used by SwerveSubsystem for math
  public static final double kWheelDiameterMeters = 0.09;
  public static final double kDriveBaseWidth = 0.76;
  public static final double kDriveBaseLength = 0.76;

  // stats used by SwerveSubsystem for deadbanding
  public static final double kXSpeedDeadband = 0.05;
  public static final double kXSpeedMinValue = 0;
  public static final double kYSpeedDeadband = 0.05;
  public static final double kYSpeedMinValue = 0;
  public static final double kRotDeadband = 0.05;
  public static final double kRotMinValue = 0;

  public static final int kDriveCurrentLimit = 30;

  // PID values for the turning
  public static final double kTurnP = 1.5;
  public static final double kTurnI = 0.0015;
  public static final double kTurnD = 0.12;
  public static final double kTurnIZ = 0;
  public static final double kTurnFF = 0;
  public static final double kTurnMinOutput = -1;
  public static final double kTurnMaxOutput = 1;
  public static final int kTurnCurrentLimit = 10;

  // because the turn gearing ratio is not 1:1, we need to spin the motor many
  // times to equal one spin of the module
  // this constant is used for the position conversion factor. (every 150 turns of
  // motors is 7 rotations of the module)
  public static final double kTurnConversionFactor = 7.0 / 150.0;

  // because the drive gearing ratio is not 1:1, we need to spin the motor many
  // times to equal one spin of the module
  public static final double kDriveGearRatio = 6.75;
}
