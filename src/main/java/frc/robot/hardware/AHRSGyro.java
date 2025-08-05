package frc.robot.hardware;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.I2C;
import proto.sensor.GeneralSensorDataOuterClass.GeneralSensorData;
import proto.sensor.Imu.ImuData;
import proto.util.Position.Position3d;
import proto.util.Vector.Vector3;
import pwrup.frc.core.hardware.sensor.IGyroscopeLike;
import pwrup.frc.core.online.raspberrypi.coms.IDataSubsystem;

public class AHRSGyro implements IGyroscopeLike, IDataSubsystem {

  private final AHRS m_gyro;
  private double xOffset = 0;
  private double yOffset = 0;
  private double zOffset = 0;

  private static AHRSGyro instance;

  public static AHRSGyro getInstance() {
    if (instance == null) {
      instance = new AHRSGyro(I2C.Port.kMXP);
    }

    return instance;
  }

  public AHRSGyro(I2C.Port i2c_port_id) {
    this.m_gyro = new AHRS(i2c_port_id);
  }

  public AHRS getGyro() {
    return m_gyro;
  }

  @Override
  public double[] getYPR() {
    return new double[] {
        m_gyro.getYaw(),
        m_gyro.getPitch(),
        m_gyro.getRoll(),
    };
  }

  @Override
  public void setPositionAdjustment(double x, double y, double z) {
    xOffset = x;
    yOffset = y;
    zOffset = z;
    m_gyro.resetDisplacement();
  }

  @Override
  public double[] getLinearAccelerationXYZ() {
    return new double[] {
        m_gyro.getWorldLinearAccelX(),
        m_gyro.getWorldLinearAccelY(),
        m_gyro.getWorldLinearAccelZ(),
    };
  }

  /**
   * @note not available on NavX
   */
  @Override
  public double[] getAngularVelocityXYZ() {
    return new double[] { 0, 0, 0 };
  }

  @Override
  public double[] getQuaternion() {
    return new double[] {
        m_gyro.getQuaternionW(),
        m_gyro.getQuaternionX(),
        m_gyro.getQuaternionY(),
        m_gyro.getQuaternionZ(),
    };
  }

  @Override
  public double[] getLinearVelocityXYZ() {
    return new double[] {
        m_gyro.getVelocityX(),
        m_gyro.getVelocityY(),
        m_gyro.getVelocityZ(),
    };
  }

  @Override
  public double[] getPoseXYZ() {
    return new double[] {
        m_gyro.getDisplacementX() + xOffset,
        m_gyro.getDisplacementY() + yOffset,
        m_gyro.getDisplacementZ() + zOffset,
    };
  }

  @Override
  public void reset() {
    m_gyro.reset();
  }

  @Override
  public void setAngleAdjustment(double angle) {
    m_gyro.setAngleAdjustment(angle);
  }

  @Override
  public byte[] getRawConstructedProtoData() {
    return GeneralSensorData.newBuilder().setImu(ImuData.newBuilder()
        .setVelocity(Vector3
            .newBuilder()
            .setX(m_gyro.getVelocityX())
            .setY(m_gyro.getVelocityY())
            .setZ(m_gyro.getVelocityZ())
            .build())
        .setPosition(
            Position3d
                .newBuilder()
                .setPosition(
                    Vector3
                        .newBuilder()
                        .setX(m_gyro.getDisplacementX())
                        .setY(m_gyro.getDisplacementY())
                        .setZ(m_gyro.getDisplacementZ())
                        .build())
                .setDirection(
                    Vector3
                        .newBuilder()
                        .setX(m_gyro.getYaw())
                        .setY(m_gyro.getPitch())
                        .setZ(m_gyro.getRoll())
                        .build())
                .build())
        .setAcceleration(
            Vector3
                .newBuilder()
                .setX(m_gyro.getWorldLinearAccelX())
                .setY(m_gyro.getWorldLinearAccelY())
                .setZ(m_gyro.getWorldLinearAccelZ())
                .build()))
        .setTimestamp(System.currentTimeMillis()).build().toByteArray();
  }

  @Override
  public String getPublishTopic() {
    return "robot/imu";
  }
}
