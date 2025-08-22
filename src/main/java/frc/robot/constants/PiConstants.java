package frc.robot.constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import autobahn.client.Address;
import edu.wpi.first.wpilibj.Filesystem;
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

  public static List<String> poseSubscribeTopics = new ArrayList<>(
      Arrays.asList(new String[] { "position-extrapolator/pose" }));
  public static Address mainPiAddr = new Address("10.47.65.7", 8080);
  public static String piTechnicalLogTopic = "pi-technical-log";
  public static File configFilePath = new File(
      Filesystem.getDeployDirectory().getAbsolutePath() + "/config");

  public static RaspberryPi<ProcessType> mainPi = new RaspberryPi<ProcessType>(
      "pi-main",
      "http://10.47.65.7:5000",
      Arrays.asList(ProcessType.POSE_EXTRAPOLATOR));
}
