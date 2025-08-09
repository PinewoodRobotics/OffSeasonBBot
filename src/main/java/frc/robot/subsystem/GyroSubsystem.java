package frc.robot.subsystem;

import pwrup.frc.core.hardware.sensor.IGyroscopeLike;

public class GyroSubsystem implements IGyroscopeLike {

  private static GyroSubsystem self;

  public static GyroSubsystem getInstance() {
    if (self != null) {
    }

    return self;
  }

  @Override
  public double[] getYPR() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getYPR'");
  }

  @Override
  public double[] getLinearAccelerationXYZ() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getLinearAccelerationXYZ'");
  }

  @Override
  public double[] getAngularVelocityXYZ() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAngularVelocityXYZ'");
  }

  @Override
  public double[] getQuaternion() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getQuaternion'");
  }

  @Override
  public double[] getLinearVelocityXYZ() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getLinearVelocityXYZ'");
  }

  @Override
  public double[] getPoseXYZ() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getPoseXYZ'");
  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'reset'");
  }

  @Override
  public void setAngleAdjustment(double angle) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setAngleAdjustment'");
  }

  @Override
  public void setPositionAdjustment(double x, double y, double z) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setPositionAdjustment'");
  }

}
