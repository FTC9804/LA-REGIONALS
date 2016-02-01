package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;


// Made by the programmers of FTC Team 9804 Bomb Squad on January 29, 2016 4pm
// Steve C.
public class DistanceTest extends LinearOpMode {

    //drive motors
    DcMotor driveLeftBack;
    DcMotor driveLeftFront;
    DcMotor driveRightBack;
    DcMotor driveRightFront;

    double midPower;
    int targetHeading;
    double driveGain = 0.05;
    double leftPower;
    double rightPower;
    int currentHeading = 0;                     //This is a signed value
    double steeringAdjustment;
    int headingError;
    double driveSteering;
    double currentDistance;
    int currentEncCountLeft;
    int currentEncCountRight;


    double targetDistance;
    double encoderCountsPerRotation = 1440;
    double diameter = 2.5;                      //2.5 inches for competition bot,  3.75 for test bot
    double circumference = diameter * 3.14159;
    double rotations;
    double targetEncoderCounts;
    double EncErrorLeft;
    int telemetryVariable;
    int initialEncCountLeft;
    int initialEncCountRight;

    @Override
    public void runOpMode() throws InterruptedException {
        //gives name of drive motors
        driveLeftBack = hardwareMap.dcMotor.get("m5");      // 1 on red controller SN VUTK
        driveLeftFront = hardwareMap.dcMotor.get("m6");     // 2 on red
        driveRightBack = hardwareMap.dcMotor.get("m1");     // 1 on purple controller SN UVQF
        driveRightFront = hardwareMap.dcMotor.get("m2");    // 2 on purple


//        driveLeftBack.setMode(RunMode.RESET_ENCODERS);      // left back has encoder 1 ("enc1")
//        driveRightBack.setMode(RunMode.RESET_ENCODERS);     //right back has encoder 2 ("enc2")


        ModernRoboticsI2cGyro gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        hardwareMap.logDevices();

        gyro.calibrate();

        waitForStart();

        // make sure the gyro is calibrated.
        while (gyro.isCalibrating()) {
            Thread.sleep(50);
        }
        driveLeftBack.setDirection(DcMotor.Direction.FORWARD);
        driveLeftFront.setDirection(DcMotor.Direction.FORWARD);
        driveRightBack.setDirection(DcMotor.Direction.REVERSE);
        driveRightFront.setDirection(DcMotor.Direction.REVERSE);


        //DRIVE FORWARD 12 INCHES


        midPower = 0.4;
        targetHeading = 0;              //drive straight ahead

        targetDistance = 60.0;
        rotations = targetDistance / circumference;
        targetEncoderCounts = encoderCountsPerRotation * rotations;

        this.resetStartTime();

        initialEncCountLeft = Math.abs(driveLeftBack.getCurrentPosition());
        initialEncCountRight = Math.abs(driveRightBack.getCurrentPosition());


        do {

            currentEncCountLeft = Math.abs(driveLeftBack.getCurrentPosition()) - initialEncCountLeft;
            currentEncCountRight = Math.abs(driveRightBack.getCurrentPosition()) - initialEncCountRight;

            EncErrorLeft = targetEncoderCounts - currentEncCountLeft;

            telemetry.addData("Left Encoder:", currentEncCountLeft);
            telemetry.addData("Right Encoder:", currentEncCountRight);

            currentDistance = (currentEncCountLeft * circumference) / encoderCountsPerRotation;
            telemetry.addData("Calculated current distance: ", currentDistance);
            // get the Z-axis heading info.
            //this is a signed heading not a basic heading
            currentHeading = gyro.getIntegratedZValue();

            headingError = targetHeading - currentHeading;

            driveSteering = headingError * driveGain;

            leftPower = midPower - driveSteering;
            if (leftPower > 1.0) {                            //cuts ourselves off at 1, the maximum motor power
                leftPower = 1.0;
            }
            if (leftPower < 0.0) {                            //don't drive backwards
                leftPower = 0.0;
            }
            rightPower = midPower + driveSteering;
            if (rightPower > 1.0) {
                rightPower = 1.0;
            }
            if (rightPower < 0.0) {
                rightPower = 0.0;
            }
            driveLeftBack.setPower(leftPower);
            driveLeftFront.setPower(.95 * leftPower);       //creates belt tension between the drive pulleys
            driveRightBack.setPower(rightPower);
            driveRightFront.setPower(.95 * rightPower);

            waitOneFullHardwareCycle();


        } while (EncErrorLeft > 0
                && this.getRuntime() < 100);

        driveLeftBack.setPower(0.0);
        driveLeftFront.setPower(0.0);
        driveRightBack.setPower(0.0);
        driveRightFront.setPower(0.0);

        telemetry.addData("confirm distance travelled", telemetryVariable);
        resetStartTime();
        while (this.getRuntime() < 20) {
            waitOneFullHardwareCycle();
        }

    }
}
