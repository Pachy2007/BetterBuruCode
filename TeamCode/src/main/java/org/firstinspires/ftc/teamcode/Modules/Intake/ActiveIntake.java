package org.firstinspires.ftc.teamcode.Modules.Intake;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Robot.Hardware;

public class ActiveIntake {

    public static boolean ENABLE=true;
    public static double intakePower=.8 , reversePower=-.8 , idlePower=0;
    DcMotorEx motor;
    enum State{
        INTAKE(intakePower),
        REVERSE(reversePower),
        IDLE(idlePower);
        double power;
        State(double power)
        {
            this.power=power;
        }
    }
    State state;
    public ActiveIntake(State initialState , boolean REVERSE)
    {
        if(!ENABLE)motor=null;
        else motor= Hardware.mch0;
        if(REVERSE)motor.setDirection(DcMotorSimple.Direction.REVERSE);
        state=initialState;

    }
    private void updatePower()
    {
        motor.setPower(state.power);
    }
    public void updateStateValues()
    {
        State.INTAKE.power=intakePower;
        State.REVERSE.power=reversePower;
        State.IDLE.power=idlePower;
    }
    public void update()
    {
        if(!ENABLE)return;
        updateStateValues();
        updatePower();
    }
}
