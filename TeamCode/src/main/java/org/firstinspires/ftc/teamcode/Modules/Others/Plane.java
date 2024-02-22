package org.firstinspires.ftc.teamcode.Modules.Others;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Plane {

    enum State{
        INITIAL,
        RELEASE;
    }
    State state=State.INITIAL;
    public static boolean ENABLE=true;
    public static double initialPos , releasePos;
    Servo servo;
    public Plane(HardwareMap hardware)
    {
        if(!ENABLE)servo=null;
        else {servo=hardware.get(Servo.class , "");
        servo.setPosition(initialPos);}

    }
    private void updatePos()
    {
        if(!ENABLE)return;
            switch(state) {
                case INITIAL:
                    servo.setPosition(initialPos);
                    break;
                case RELEASE:
                    servo.setPosition(releasePos);
            }
    }
    public void update()
    {
        updatePos();
    }
}
