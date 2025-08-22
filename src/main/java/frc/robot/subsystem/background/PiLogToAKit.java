package frc.robot.subsystem.background;

import java.util.List;

import org.littletonrobotics.junction.Logger;

import autobahn.client.NamedCallback;
import frc.robot.Robot;
import frc.robot.constants.PiConstants;
import proto.status.StateLoggingOuterClass.StateLogging;
import proto.status.StateLoggingOuterClass.DataEntry;
import proto.status.StateLoggingOuterClass.DataType;
import com.google.protobuf.InvalidProtocolBufferException;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PiLogToAKit extends SubsystemBase {

    private static PiLogToAKit self;

    public static PiLogToAKit GetInstance() {
        if (self == null) {
            self = new PiLogToAKit();
        }
        return self;
    }

    public PiLogToAKit() {
        super();
        Robot.communication.subscribe(PiConstants.AutobahnConfig.piTechnicalLogTopic,
                NamedCallback.FromConsumer(this::subscription));
    }

    private void subscription(byte[] data) {
        try {
            StateLogging stateLogging = StateLogging.parseFrom(data);
            for (DataEntry entry : stateLogging.getEntriesList()) {
                String name = stateLogging.getName();
                DataType type = entry.getType();
                switch (type) {
                    case BOOL:
                        mskrBool(name, entry.getBoolValuesList());
                        break;
                    case INT:
                        mskrInt(name, entry.getIntValuesList());
                        break;
                    case FLOAT:
                        mskrFloat(name, entry.getFloatValuesList());
                        break;
                    case STRING:
                        mskrString(name, entry.getStringValuesList());
                        break;
                    default:
                        // Unknown or unsupported type, do nothing
                        break;
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    // Utility methods for handling single vs array values
    private void mskrBool(String name, List<Boolean> bools) {
        if (bools.size() == 1) {
            Logger.recordOutput(name, bools.get(0));
        } else if (bools.size() > 1) {
            boolean[] arr = new boolean[bools.size()];
            for (int i = 0; i < bools.size(); i++)
                arr[i] = bools.get(i);
            Logger.recordOutput(name, arr);
        }
    }

    private void mskrInt(String name, List<Integer> ints) {
        if (ints.size() == 1) {
            Logger.recordOutput(name, ints.get(0));
        } else if (ints.size() > 1) {
            int[] arr = new int[ints.size()];
            for (int i = 0; i < ints.size(); i++)
                arr[i] = ints.get(i);
            Logger.recordOutput(name, arr);
        }
    }

    private void mskrFloat(String name, List<Float> floats) {
        if (floats.size() == 1) {
            Logger.recordOutput(name, floats.get(0));
        } else if (floats.size() > 1) {
            double[] arr = new double[floats.size()];
            for (int i = 0; i < floats.size(); i++)
                arr[i] = floats.get(i);
            Logger.recordOutput(name, arr);
        }
    }

    private void mskrString(String name, List<String> strings) {
        if (strings.size() == 1) {
            Logger.recordOutput(name, strings.get(0));
        } else if (strings.size() > 1) {
            Logger.recordOutput(name, strings.toArray(new String[0]));
        }
    }
}
