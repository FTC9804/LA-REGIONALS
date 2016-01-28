package com.qualcomm.ftcrobotcontroller.opmodes.BombSquadOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class AutonomousTets extends LinearOpMode {
    DcMotor FR;
    DcMotor BR;
//    DcMotor FL;
//    DcMotor BL;

    int a;

    //pid commands
    double speed = 1;
    double targetHeading = 90;
    double gain = 0.1;
    double steeringError;
    double leftPower;
    double rightPower;
    int currentHeading = 0;
    double steeringAdjustment = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        FR = hardwareMap.dcMotor.get("FR");
        BR = hardwareMap.dcMotor.get("BR");
//        FL = hardwareMap.dcMotor.get("FL");
//        BL = hardwareMap.dcMotor.get("BL");
        ModernRoboticsI2cGyro gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");


        //reverse motors
        FR.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);
//        FL.setDirection(DcMotor.Direction.REVERSE);
//        BL.setDirection(DcMotor.Direction.REVERSE);

        gyro.calibrate();

        telemetry.addData("wait for start", a);
        waitForStart();

        FR.setPower(0);
        BR.setPower(0);
//        FL.setPower(0);
//        BL.setPower(0);

        //run forward full power for two seconds
        telemetry.addData("run forward full power for two seconds" , a);
        this.resetStartTime();
        do {
            FR.setPower(1);
            BR.setPower(1);
//            FL.setPower(1);
//            BL.setPower(1);
            telemetry.addData("Distance", FR.getCurrentPosition());
            Thread.sleep(50);
        } while (this.getRuntime() < 2);

        //sleep for two
        telemetry.addData("sleep for two seconds" , a);
        this.resetStartTime();
        do {
            FR.setPower(0);
            BR.setPower(0);

            Thread.sleep(50);
        } while (this.getRuntime() < 2);

        //turn 90 degrees
        telemetry.addData("turn 90 degrees" , a);

        currentHeading = 0;
        do {
            currentHeading = gyro.getIntegratedZValue();
            steeringError = currentHeading - targetHeading;
            steeringAdjustment = steeringError * gain;
            rightPower = (speed + steeringAdjustment);
            leftPower = (speed - steeringAdjustment);
            telemetry.addData("1. h", String.format("%03d", currentHeading));
            FR.setPower(rightPower);
            BR.setPower(rightPower);
//            FL.setPower(leftPower);
//            BL.setPower(leftPower);
        }while (currentHeading < targetHeading);

        //use encoders to go forward 1 rotation
        telemetry.addData("use encoders to go forward 1 rotation" , a);
        FR.setChannelMode(DcMotorController.RunMode .RESET_ENCODERS);
//        FL.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);

        do {
            FR.setPower(1);
            BR.setPower(1);
//            FL.setPower(1);
//            BL.setPower(1);
            telemetry.addData("Distance", FR.getCurrentPosition());
            Thread.sleep(50);
        } while (Math.abs(FR.getCurrentPosition()) < 1440);

        FR.setPower(0);
        BR.setPower(0);
//        FL.setPower(0);
//        BL.setPower(0);
    }
}