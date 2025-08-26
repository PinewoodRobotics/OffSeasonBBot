package frc.robot;

import java.io.IOException;
import java.nio.file.Files;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import autobahn.client.AutobahnClient;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.constants.BotConstants;
import frc.robot.constants.PiConstants;

public class Robot extends LoggedRobot {

  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;
  private String configContents;

  public static AutobahnClient communication = new AutobahnClient(PiConstants.network.getMainPi());
  // final XboxController m_controller = new XboxController(3);

  public Robot() {
    Logger.addDataReceiver(new NT4Publisher());
    switch (BotConstants.currentMode) {
      case REAL:
        Logger.addDataReceiver(new WPILOGWriter());
        Logger.addDataReceiver(new NT4Publisher());
        break;

      case SIM:
        Logger.addDataReceiver(new NT4Publisher());
        break;

      case REPLAY:
        setUseTiming(false);
        String logPath = LogFileUtil.findReplayLog();
        Logger.setReplaySource(new WPILOGReader(logPath));
        Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
        break;
    }

    Logger.start();

    try {
      configContents = new String(
          Files.readAllBytes(PiConstants.configFilePath.toPath()));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public void robotInit() {
    communication.begin().join();
    PiConstants.network.setConfig(configContents);
    PiConstants.network.restartAllPis();

    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    Logger.recordOutput("autobahn/connection", communication.isConnected());

    // TODO: make this nonblockable
    if (!communication.isConnected()) {
      System.err.println("Not connected to Autobahn server! Erroring out.");
      System.exit(1);
    }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  public void setControllerBindings() {
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
