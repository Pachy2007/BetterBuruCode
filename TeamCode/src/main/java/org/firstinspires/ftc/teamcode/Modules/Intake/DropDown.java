package org.firstinspires.ftc.teamcode.Modules.Intake;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Math.BetterMotionProfile;
import org.firstinspires.ftc.teamcode.Robot.Hardware;


public class DropDown {

    public static double maxVelocity, acceleration, deceleration;
    public static BetterMotionProfile profile = new BetterMotionProfile(maxVelocity, acceleration, deceleration);
    Servo servo;
    public static boolean ENABLE = true;
    public static double upPos = 0, downPos = 0;
    public static int index = 0;
    public static double pos0, pos1, pos2, pos3, pos4;

    enum State {
        UP(upPos), GOING_UP(upPos, UP), DOWN(downPos), GOING_DOWN(downPos, DOWN);
        double position;
        State nextState;

        State(double position) {
            this.position = position;
            this.nextState = this;
        }

        State(double position, State nextState) {
            this.position = position;
            this.nextState = nextState;
        }
    }

    State state;

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

    public State getState() {
        return state;
    }

    public double getCurrentPosition() {
        return profile.getPosition();
    }

    public DropDown(State initialState) {
        if (!ENABLE) {
            servo = null;
        } else servo = Hardware.sch0;
        state = initialState;
        profile.setMotion(state.position, state.position, 0);
    }

    private void updateStateValues() {
        double[] pos = {pos0, pos1, pos2, pos3, pos4};
        State.UP.position = upPos;
        State.GOING_UP.position = upPos;
        State.DOWN.position = pos[index];
        State.GOING_DOWN.position = pos[index];
    }

    public void updateState() {
        if (state == State.GOING_DOWN && profile.getPosition() == state.position) {
            state = state.nextState;
        }
        if (state == State.GOING_UP && profile.getPosition() == state.position) {
            state = state.nextState;
        }
    }

    public void updateHardware() {
        profile.update();
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
