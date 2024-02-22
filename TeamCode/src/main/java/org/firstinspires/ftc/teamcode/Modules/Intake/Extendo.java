package org.firstinspires.ftc.teamcode.Modules.Intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.ChUpdaterCommManager;
import org.firstinspires.ftc.teamcode.Math.BetterMotionProfile;
import org.firstinspires.ftc.teamcode.Math.PIDController;
import org.firstinspires.ftc.teamcode.Robot.Hardware;

public class Extendo {

    public static DcMotorEx encoder;
    DcMotorEx motor;
    public static int extendPos;

    public boolean ENABLE=true;
    public static double positionThreshold=6;
    public static double velocityThreshold=5;

    public static double maxVelocity=0 , acceleration=0 , deceleration=0;
    public static double kp=0 , ki=0 , kd=0;
    public static int zeroPos=0;
    public static BetterMotionProfile profile=new BetterMotionProfile(maxVelocity , acceleration , deceleration);
    public static PIDController pid=new PIDController(kp , ki, kd);
    enum State{
        IN(0 , encoder.getCurrentPosition()) , OUT(extendPos) , RESETTING(0 , IN), GOING_IN(0 , RESETTING) ,GOING_OUT(extendPos , OUT);
        int position;
        State nextState;

        State(int position)
        {
            this.position=position;
            this.nextState=this;
        }
        State(int position , State nextState)
        {
            this.position=position;
            this.nextState=nextState;
        }
        State(int position , int realZeroPos)
        {
            position=realZeroPos;
            this.position=position;
            zeroPos=realZeroPos;
            this.nextState=this;
        }
    }
    private State state;

    public State getState(){
        return state;
    }

    public void setState(State newState)
    {
        state=newState;
    }

    public Extendo(State initialState)
    {
        if(!ENABLE){motor=null;
                   encoder=null;}
        else{
            motor=Hardware.mch0;
            encoder=Hardware.mch0;
        }
         state=initialState;
        profile.setMotion(0 , 0 ,0);
    }
    private void updateStateValues()
    {
        State.OUT.position=extendPos;
        State.GOING_OUT.position=extendPos;
    }
    private void updateState()
    {
        switch(state){
            case IN:
            case OUT:
            case GOING_IN:
            case GOING_OUT:
                if(Math.abs(state.position - encoder.getCurrentPosition())<=positionThreshold)
                    state=state.nextState;
                break;
            case RESETTING:
                if(Math.abs(encoder.getVelocity())<=velocityThreshold)
                    state=state.nextState;
                break;
        }
    }
    private void updateHardware()
    {
        if(state==State.RESETTING)
        {motor.setPower(-0.5);}
        else
        {motor.setPower(pid.calculate(state.position+zeroPos , encoder.getCurrentPosition()));}
    }
    public void update()
    {
        if(!ENABLE)return;
        updateStateValues();
        updateState();
        updateHardware();
    }
}
