package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Made by the programmers of FTC Team 9804 Bomb Squad



public class TeleOpTest extends OpMode {
    DigitalChannel digital;

    DcMotor driveLeft1;
    DcMotor driveLeft2;
    DcMotor driveRight1;
    DcMotor driveRight2;
    DcMotor leftWinch;
    DcMotor rightWinch;
    double DOWN_POSITION = 0.0;
    double UP_POSITION = 1.0;
    DcMotor arms;
    DcMotor spin;
    Servo grab1;
    Servo grab2;
    Servo score;
    Servo drop;
    Servo hooks;

    @Override
    public void init(){
        digital = hardwareMap.digitalChannel.get("touch1");
        driveLeft1 = hardwareMap.dcMotor.get("d1");
        driveLeft2 = hardwareMap.dcMotor.get("d2");
        driveRight1 = hardwareMap.dcMotor.get("d3");
        driveRight2 = hardwareMap.dcMotor.get("d4");
        driveLeft1.setDirection(DcMotor.Direction.REVERSE);
        driveLeft2.setDirection(DcMotor.Direction.REVERSE);
        driveRight1.setDirection(DcMotor.Direction.FORWARD);
        driveRight2.setDirection(DcMotor.Direction.FORWARD);

        arms = hardwareMap.dcMotor.get("arms");
        spin  = hardwareMap.dcMotor.get("spin");
        rightWinch  = hardwareMap.dcMotor.get("rw");
        leftWinch  = hardwareMap.dcMotor.get("lw");
        grab1 = hardwareMap.servo.get("g1");
        grab2 = hardwareMap.servo.get("g2");
        score = hardwareMap.servo.get("score");
        drop = hardwareMap.servo.get("drop");
        hooks = hardwareMap.servo.get("hooks");
    }
    @Override
    public void loop() {
        boolean digVal = digital.getState();
        telemetry.addData("Digital1", String.format("%1d", (digVal ? 1 : 0)));
        driveLeft1.setPower(-gamepad1.left_stick_y);
        driveLeft2.setPower(-gamepad1.left_stick_y);
        driveRight1.setPower(gamepad1.right_stick_y);
        driveRight2.setPower(gamepad1.right_stick_y);

        rightWinch.setPower(gamepad2.left_stick_y);
        leftWinch.setPower(gamepad2.right_stick_y);
        if (gamepad1.x) {
            arms.setPower(1);
        }else {
            arms.setPower(0);
        }
        if (gamepad1.b) {
            spin.setPower(1);
        } else {
            spin.setPower(0);
        }
        if (gamepad2.x){
            grab1.setPosition(UP_POSITION);
        } else if (gamepad2.y){
            grab1.setPosition(DOWN_POSITION);
        } else {
            grab1.setPosition(0);
        }

        if (gamepad2.a){
            grab2.setPosition(UP_POSITION);
        } else if (gamepad2.b){
            grab2.setPosition(DOWN_POSITION);
        } else {
            grab2.setPosition(0);
        }
        if (gamepad1.a){
            score.setPosition(UP_POSITION);
        }
        else if (gamepad1.y){
            score.setPosition(DOWN_POSITION);

        }
        if (gamepad1.a){
            drop.setPosition(UP_POSITION);
        }
        else if (gamepad1.y){
            drop.setPosition(DOWN_POSITION);

        }
        if (gamepad1.a){
            hooks.setPosition(UP_POSITION);
        }
        else if (gamepad1.y){
            hooks.setPosition(DOWN_POSITION);

        }
    }
}
