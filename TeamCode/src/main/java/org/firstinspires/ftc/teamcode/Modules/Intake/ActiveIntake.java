package org.firstinspires.ftc.teamcode.Modules.Intake;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Robot.Hardware;

public class ActiveIntake {

    public static boolean ENABLE = true;
    public static boolean REVERSE = false;
    DcMotorEx motor;
    public static double runningPower = 0.5, reversePower = -0.5;

    enum State {
        RUNNING(runningPower), IDLE(0), REVERSE(reversePower);
        double power;

        State(double power) {
            this.power = power;
        }
    }

    State state;

    public ActiveIntake(State initialState) {
        if (!ENABLE) motor = null;
        else motor = Hardware.mch3;
        this.state = initialState;
        if (REVERSE) motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setState(State state) {
        if (state != this.state) {
            this.state = state;
        }
    }

    public State getState() {
        return state;
    }

    private void updateStateValues() {
        State.REVERSE.power = reversePower;
        State.RUNNING.power = runningPower;
    }

    private void updateHardware() {
        motor.setPower(state.power);
    }

    public void update() {
        if (!ENABLE) return;
        updateStateValues();
        updateHardware();
    }
}
