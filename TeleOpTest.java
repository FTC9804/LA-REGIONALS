package com.qualcomm.ftcrobotcontroller.opmodes;

//import OpModes
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Made by the programmers of FTC Team 9804 Bomb Squad on January 27, 2016
//V02 6 pm 1-28-16


public class TeleOpTest extends OpMode {

    //defining the motors, servos, and variables in this program

    //magnetics sensor
    DigitalChannel sensorExtend;
    DigitalChannel sensorRetract;
    DigitalChannel extendLED;
    DigitalChannel retractLED;
    //drive motors
    DcMotor driveLeftBack;
    DcMotor driveLeftFront;
    DcMotor driveRightBack;
    DcMotor driveRightFront;
    //winch motors
    DcMotor leftWinch;
    DcMotor rightWinch;
    //motors for arms and spinner
    DcMotor arms;
    DcMotor spin;
    //servos to lock in place on ramp
    Servo grabLeft;
    Servo grabRight;
    //servo to release debris
    Servo score;
    //hits climbers with drop thing
    Servo drop;
    //sets hooks up for final climb
    Servo hooks;
    //variables for driving
    float trailingValueRight;
    double leadingValueRight;
    float trailingValueLeft;
    double leadingValueLeft;
    int telemetryVariable;
    double grabLeftUp = 0;
    double grabLeftDown = 0.5;
    double grabRightUp = 0.5;
    double grabRightDown = 0;
    double scoreUp = 0;
    double scoreDown = 1.0;
    double dropUp = 0;
    double dropDown = 1.0;
    double hooksUp = 0;
    double hooksDown = 1.0;


    @Override
    public void init(){
        //gives name of magnetic sensor
        sensorExtend = hardwareMap.digitalChannel.get("extend");
        sensorRetract  = hardwareMap.digitalChannel.get("retract");
        extendLED = hardwareMap.digitalChannel.get("extendLED");
        retractLED = hardwareMap.digitalChannel.get("retractLED");
        extendLED.setMode(DigitalChannelController.Mode.OUTPUT);
        retractLED.setMode(DigitalChannelController.Mode.OUTPUT);
        retractLED.setState(false);
        extendLED.setState(false);
        //gives name of drive motors
        driveLeftBack = hardwareMap.dcMotor.get("dlback");
        driveLeftFront = hardwareMap.dcMotor.get("dlfront");
        driveRightBack = hardwareMap.dcMotor.get("drback");
        driveRightFront = hardwareMap.dcMotor.get("drfront");
        //gives the motor values forward and reverse directions to follow
        driveLeftBack.setDirection(DcMotor.Direction.REVERSE);
        driveLeftFront.setDirection(DcMotor.Direction.REVERSE);
        driveRightFront.setDirection(DcMotor.Direction.FORWARD);
        driveRightBack.setDirection(DcMotor.Direction.FORWARD);
        //gives motor names for the other motors
        arms = hardwareMap.dcMotor.get("arms");
        spin  = hardwareMap.dcMotor.get("spin");
        rightWinch  = hardwareMap.dcMotor.get("rw");
        leftWinch  = hardwareMap.dcMotor.get("lw");
        //give the servo names for the servos
        grabLeft = hardwareMap.servo.get("gl");
        grabRight = hardwareMap.servo.get("gr");
        score = hardwareMap.servo.get("score");
        drop = hardwareMap.servo.get("drop");
        hooks = hardwareMap.servo.get("hooks");
        //resets start time for match4
        this.resetStartTime();
    }
    @Override
    public void loop() {
        //creates boolean value for magnetic sensor
        boolean extendArms = sensorExtend.getState();
        boolean retractArms = sensorRetract.getState();
        //telemetry for magnetic sensor
        telemetry.addData("Extended Sensor", String.format("%1d", (extendArms ? 1 : 0))); //maybe add the word key after the string
        telemetry.addData("Retracted Sensor", String.format("%1d", (retractArms ? 1:0)));
        //takes input from joysticks for motor values; sets the back wheel at a greater power to ensure the tension is perfect
        trailingValueLeft = gamepad1.left_stick_y;
        leadingValueLeft = .95*trailingValueLeft;
        trailingValueRight = gamepad1.right_stick_y;
        leadingValueRight = .95*trailingValueRight;
        if (leadingValueLeft > 0) {
            driveLeftBack.setPower(trailingValueLeft);
            driveLeftFront.setPower(leadingValueLeft);
        }
        else if (leadingValueLeft < 0) {
            driveLeftBack.setPower(leadingValueLeft);
            driveLeftFront.setPower(trailingValueLeft);
        }
        if (leadingValueRight > 0) {
            driveRightBack.setPower(trailingValueRight);
            driveRightFront.setPower(leadingValueRight);
        }
        else if (leadingValueRight < 0) {
            driveRightBack.setPower(leadingValueRight);
            driveRightFront.setPower(trailingValueRight);
        }
        //only allows winch to be run in the last 30 seconds of the match
        if (this.getRuntime() > 90) {
            rightWinch.setPower(gamepad2.left_stick_y);
            leftWinch.setPower(gamepad2.right_stick_y);
        }
        else {
            rightWinch.setPower(0);
            leftWinch.setPower(0);
        }
        //only allows arms to go while the boolean for the magnetic sensor is false
        if (gamepad2.dpad_up && !extendArms) {
            arms.setPower(1);
            retractLED.setState(false);
            extendLED.setState(false);
            telemetry.clearData();

        }
        else if (extendArms){
            telemetry.addData("EXTENDED ALL THE WAY", telemetryVariable);
            arms.setPower(0);
            retractLED.setState(false);
            extendLED.setState(true);
        }
        else if (gamepad2.dpad_down && !retractArms) {
            arms.setPower(-1);
            retractLED.setState(false);
            extendLED.setState(false);
            telemetry.clearData();
        }
        else if (retractArms){
            telemetry.addData("RETRACTED ALL THE WAY", telemetryVariable);
            arms.setPower(0);
            retractLED.setState(true);
            extendLED.setState(false);
        }
        else {
            arms.setPower(0);
            retractLED.setState(false);
            extendLED.setState(false);
        }
        //takes input from buttons for spin motors
        if (gamepad2.a) {
            spin.setPower(1);
        }
        else if (gamepad2.y) {
            spin.setPower(-1);
        }
        else {
            spin.setPower(0);
        }
        //takes input from bumpers and triggers for the locking grab motors set individually
        if (gamepad1.right_bumper){
            grabRight.setPosition(grabRightUp);
        }
        else if (gamepad1.right_trigger > 10){
            grabRight.setPosition(grabRightDown);
        }
        else {
            grabRight.setPosition(0);
        }
        if (gamepad1.left_bumper){
            grabLeft.setPosition(grabLeftUp);
        } else if (gamepad1.left_trigger > 10){
            grabLeft.setPosition(grabLeftDown);
        } else {
            grabLeft.setPosition(0);
        }
        //sets score to a value because a button is pressed
        if (gamepad1.a){
            score.setPosition(scoreUp);
        }
        else if (gamepad1.y){
            score.setPosition(scoreDown);
        }
        //sets drop to a value because a button is pressed
        if (gamepad2.b){
            drop.setPosition(dropUp);
        }
        else if (gamepad2.x){
            drop.setPosition(dropDown);

        }
        //takes input from bumpers and triggers for the hook motors set individually
        if (gamepad2.left_trigger > 10 || gamepad2.right_trigger > 10){
            hooks.setPosition(hooksUp);
        }
        else if (gamepad2.left_bumper || gamepad2.right_bumper){
            hooks.setPosition(hooksDown);
        }
    }
}
