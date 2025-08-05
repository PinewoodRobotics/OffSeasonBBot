package frc.robot.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import autobahn.client.Address;

public class PiConstants {

  public static List<String> poseSubscribeTopics = new ArrayList<>(
      Arrays.asList(new String[] { "position-extrapolator/pose" }));
  public static Address mainPiAddr = new Address("10.47.65.7", 8080);
}
