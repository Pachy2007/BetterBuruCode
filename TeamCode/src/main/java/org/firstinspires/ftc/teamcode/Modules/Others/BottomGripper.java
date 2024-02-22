package org.firstinspires.ftc.teamcode.Modules.Others;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Math.BetterMotionProfile;
import org.firstinspires.ftc.teamcode.Robot.Hardware;

public class BottomGripper {

    public static boolean ENABLE=true;

    public static double openPos=0 , closePos=0;

    public static double maxVelocity , acceleration , deceleration;
    public static BetterMotionProfile profile =new BetterMotionProfile(maxVelocity , acceleration , deceleration);

    Servo servo;
    enum State
    {
        OPEN(openPos) , CLOSE(closePos) , OPENING(openPos , OPEN) , CLOSING(closePos , CLOSE);
        double position;
        State nextState;
        State(double position)
        {
            this.position=position;
            nextState=this;
        }
        State(double position , State nextState)
        {
            this.position=position;
            this.nextState=nextState;
        }
    }
    State state;

    public void setState(State state)
    {
        switch(state)
        {
            case OPEN:
            case OPENING:
                if(this.state!=State.OPEN && this.state!=State.OPENING)
                    this.state=state;
                break;
            case CLOSE:
            case CLOSING:
                if(this.state!=State.CLOSE && this.state!=State.CLOSING)
                    this.state=state;
                break;
        }
    }
    public State getState()
    {
        return state;
    }
    public BottomGripper(State initialState)
    {
        if(!ENABLE)servo=null;
        else servo= Hardware.seh3;
        state=initialState;
        profile.setMotion(state.position , state.position , 0);
    }
    private void updateStateValues()
    {
        State.OPEN.position=openPos;
        State.OPENING.position=openPos;

        State.CLOSE.position=closePos;
        State.CLOSING.position=closePos;
    }
    private void updateState()
    {
        switch(state)
        {
            case CLOSING:
            case OPENING:
                if(profile.finalPosition==profile.getPosition())state=state.nextState;
                break;
        }
    }
    private void updateHardware()
    {
        servo.setPosition(profile.getPosition());
        if(state.position!=profile.finalPosition)profile.setMotion(profile.getPosition() , state.position , profile.getVelocity());
        profile.update();
    }
    public void update()
    {
        updateStateValues();
        updateState();
        updateHardware();
    }
}
