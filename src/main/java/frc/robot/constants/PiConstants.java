package frc.robot.constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import autobahn.client.Address;
import edu.wpi.first.wpilibj.Filesystem;
import pwrup.frc.core.online.raspberrypi.PiNetwork;
import pwrup.frc.core.online.raspberrypi.RaspberryPi;

public class PiConstants {

  public static enum ProcessType {
    POSE_EXTRAPOLATOR("position-extrapolator");

    private String name;

    private ProcessType(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static int defaultCommandListenPort = 5000;
  public static int defaultAutobahnPort = 8080;

  public static class AutobahnConfig {
    public static String poseSubscribeTopic = "position-extrapolator/pose";
    public static Address autobahnServerAddr = new Address("10.47.65.7", defaultAutobahnPort);
    public static String piTechnicalLogTopic = "pi-technical-log";
  }

  public static File configFilePath = new File(
      Filesystem.getDeployDirectory().getAbsolutePath() + "/config");

  public static final PiNetwork<ProcessType> network = new PiNetwork<ProcessType>();

  static {
    network.add(new Address("10.47.65.7", defaultCommandListenPort), ProcessType.POSE_EXTRAPOLATOR);
  }
}
