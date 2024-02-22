package org.firstinspires.ftc.teamcode.Modules.Intake;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Math.BetterMotionProfile;
import org.firstinspires.ftc.teamcode.Robot.Hardware;

public class Ramp {

    Servo servo;
    public static boolean ENABLE = true;
    public static double upPosition = 0, downPosition = 0;
    public static int index;
    public static double pos0 = 0, pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    public static double maxVelocity = 0, acceleration = 0, deceleration = 0;
    public static BetterMotionProfile profile = new BetterMotionProfile(maxVelocity, acceleration, deceleration);

    enum State {
        UP(upPosition), DOWN(downPosition), GOING_UP(upPosition, UP), GOING_DOWN(downPosition, DOWN);

        double position;
        State nextState;

        State(double position) {
            this.position = position;
            nextState = this;
        }

        State(double position, State nextState) {
            this.position = position;
            this.nextState = nextState;
        }
    }

    State state;

    public Ramp(State initialState) {
        if (!ENABLE) {
            servo = null;
        } else servo = Hardware.sch0;
        state = initialState;
        profile.setMotion(state.position , state.position , 0);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        switch (state) {
            case GOING_DOWN:
            case DOWN:
                if (this.state != State.GOING_DOWN && this.state != State.DOWN)
                    this.state = state;
                break;
            case GOING_UP:
            case UP:
                if (this.state != State.UP && this.state != State.GOING_UP)
                    this.state = state;
                break;
        }
    }

    private void updateStateValues() {
        double[] position = {pos0, pos1, pos2, pos3, pos4};

        State.UP.position = upPosition;
        State.GOING_UP.position = upPosition;
        State.DOWN.position = position[index];
        State.GOING_DOWN.position = position[index];
    }

    private void updateState() {
        switch (state) {
            case GOING_DOWN:
            case GOING_UP:
                if (profile.getPosition() == state.position) state = state.nextState;
                break;
        }
    }

    private void updateHardware() {
        servo.setPosition(profile.getPosition());
        if (profile.finalPosition != state.position)
            profile.setMotion(profile.getPosition(), state.position, profile.getVelocity());
        profile.update();
    }

    public void update() {
        if (!ENABLE) return;
        updateStateValues();
        updateState();
        updateHardware();
    }
}
