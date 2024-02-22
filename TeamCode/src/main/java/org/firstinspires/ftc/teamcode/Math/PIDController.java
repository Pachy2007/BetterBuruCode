package org.firstinspires.ftc.teamcode.Math;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayDeque;
import java.util.Deque;

public class PIDController {


    public double kp,ki,kd;
    ElapsedTime timer=new ElapsedTime();

    public PIDController(double kp , double ki , double kd){
        this.kp=kp;
        this.ki=ki;
        this.kd=kd;
        timer.startTime();
        timer.reset();
    }
    private static class IntegralPart{
        public ElapsedTime timer =new ElapsedTime();
        public double value=0;
        public IntegralPart(double value)
        {
            this.value=value;
            timer.startTime();
            timer.reset();
        }
    }

    private final Deque<IntegralPart> integralParts=new ArrayDeque<>();
    private double lastError=0;
    public double integralBound=0.1;
    private double integralSum=0;
    public double calculate(double reference , double state){
        double error=reference-state;
        integralSum+=timer.seconds()*error;

        integralParts.addLast(new IntegralPart(error));
        while(integralParts.getFirst().timer.seconds()>integralBound)
        {
            IntegralPart temp=integralParts.getFirst();
            integralParts.removeFirst();
            integralSum-=temp.value*(temp.timer.seconds()-integralParts.getFirst().timer.seconds());
        }
        double derivative=(error-lastError)/timer.seconds();
        double output=kp*error+ki*integralSum+kd*derivative;
        timer.reset();
        lastError=error;
        return output;
    }

}
