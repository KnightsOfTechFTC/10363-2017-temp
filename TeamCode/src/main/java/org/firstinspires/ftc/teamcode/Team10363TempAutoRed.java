package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Objects;

/**
 * Created by Lego5 on 12/21/2016.
 */
@Autonomous(name = "10363 Auto on Red")
public class Team10363TempAutoRed extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    //ints
    int left_encoder;
    int right_encoder;
    int left_beacon;
    int right_beacon;
    //motors
    DcMotor v_motor_left_drive;
    DcMotor v_motor_right_drive;
    DcMotor v_motor_intake;
    DcMotor v_motor_ball_shooter;
    DcMotor v_motor_lift;
    //servos
    Servo v_servo_lift;
    CRServo v_servo_left_beacon;
    CRServo v_servo_right_beacon;
    //sensors
    GyroSensor SensorGyro;
    ColorSensor GroundColor;
    ColorSensor SideColor;
    ColorSensor FrontColor;
    OpticalDistanceSensor ODS;

    @Override

    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AWThhw//////AAAAGYgme8IEP0VXvW2eMc9GLHcCZt2HTjBY2BEZ7DmxzEgLDypsGvRgR2xr2douQ6h3nAzHpg7/HFpa4/DOlekbygKLhWdBAH2AhAu2r6nAn4ejWfQq32k4JVGOTbAMkx7H2fuHDYZduZQJiW/1pFJt0SdcqvClYOFtbdb+OaKHOTkLgmI3zWDBtjfM6Pc+FRchtsK3ITl1MxVtsVNsfZNC2UQREHd23ABZsQ0jrFcXaDwmR3Q1s3tOSRs3lMdJXk+riKmk2yLyat+pIRzHoUuvTQURKcvqgK00LVqWiOaarRlnOnccxzf2lO5jv4v2gohQXAxu6KpAQxsDyj1JrKYv91mWssJKeTbeXchIeLvqyCpn";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        try {
            v_motor_left_drive = hardwareMap.dcMotor.get("right_drive");
            v_motor_left_drive.setDirection(DcMotor.Direction.REVERSE);
            v_motor_left_drive.setPower(0);
            v_motor_left_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            v_motor_left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        //If it doesn't work, set the motor to null and add record the problem in the Debug log.
        catch (Exception p_exception) {
            v_motor_left_drive = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        //Same as above for the right motor, but reversed
        try {
            v_motor_right_drive = hardwareMap.dcMotor.get("left_drive");
            v_motor_right_drive.setDirection(DcMotor.Direction.FORWARD);
            v_motor_right_drive.setPower(0);
            v_motor_right_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            v_motor_right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception p_exception) {
            v_motor_right_drive = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        try {
            v_motor_intake = hardwareMap.dcMotor.get("intake");
            v_motor_intake.setDirection(DcMotor.Direction.FORWARD);
            v_motor_intake.setPower(0);
            v_motor_intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            v_motor_intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        //If it doesn't work, set the motor to null and add record the problem in the Debug log.
        catch (Exception p_exception) {
            v_motor_intake = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }

        //Try to add the beacon pushing servos. The left one is in reverse.
        try {
            v_servo_left_beacon = hardwareMap.crservo.get("left_beacon");
            v_servo_left_beacon.setDirection(CRServo.Direction.FORWARD);
            v_servo_left_beacon.setPower(0);

        } catch (Exception p_exception) {
            v_servo_left_beacon = null;
            DbgLog.msg(p_exception.getLocalizedMessage());

        }
        try {
            v_servo_right_beacon = hardwareMap.crservo.get("right_beacon");
            v_servo_right_beacon.setDirection(CRServo.Direction.FORWARD);
            v_servo_right_beacon.setPower(0);
        } catch (Exception p_exception) {
            v_servo_right_beacon = null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        try {
            v_servo_lift=hardwareMap.servo.get("lift");
            v_servo_lift.setDirection(Servo.Direction.FORWARD);
            v_servo_lift.setPosition(.4);
        }catch (Exception p_exception){
            v_servo_lift=null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        //Try to add the gyro
        try {
            SensorGyro = hardwareMap.gyroSensor.get("gyro");
        } catch (Exception p_exception) {
            DbgLog.msg(p_exception.getLocalizedMessage());
            SensorGyro = null;
        }
        //Calibrate the gyro. Drive team- don't hit start until the light starts blinking!
        if (SensorGyro != null) {
            SensorGyro.calibrate();
            while (SensorGyro.isCalibrating()) {
                Thread.sleep(50);
            }
        }
        //Try to add the color sensors
        try {
            GroundColor = hardwareMap.colorSensor.get("ground");
            GroundColor.setI2cAddress(I2cAddr.create8bit(0x3C));
            GroundColor.enableLed(true);
        } catch (Exception p_exception) {
            DbgLog.msg(p_exception.getLocalizedMessage());
            GroundColor = null;
        }
        try{
            v_motor_ball_shooter=hardwareMap.dcMotor.get("ball_shooter");
            v_motor_ball_shooter.setDirection(DcMotor.Direction.FORWARD);
            v_motor_ball_shooter.setPower(0);
            v_motor_ball_shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            v_motor_ball_shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //If it doesn't work, set the motor to null and add record the problem in the Debug log.
        catch (Exception p_exception){
            v_motor_ball_shooter=null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        try{
            v_motor_lift=hardwareMap.dcMotor.get("lift");
            v_motor_lift.setDirection(DcMotor.Direction.FORWARD);
            v_motor_lift.setPower(0);
            v_motor_lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            v_motor_lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        //If it doesn't work, set the motor to null and add record the problem in the Debug log.
        catch (Exception p_exception){
            v_motor_lift=null;
            DbgLog.msg(p_exception.getLocalizedMessage());
        }
        try {
            SideColor = hardwareMap.colorSensor.get("side-color");
            SideColor.setI2cAddress(I2cAddr.create8bit(0x42));
            SideColor.enableLed(false);
        } catch (Exception p_exception) {
            DbgLog.msg(p_exception.getLocalizedMessage());
            SideColor = null;
        }
        try {
            FrontColor = hardwareMap.colorSensor.get("front-color");
            FrontColor.setI2cAddress(I2cAddr.create8bit(0x46));
            FrontColor.enableLed(false);
        } catch (Exception p_exception) {
            DbgLog.msg(p_exception.getLocalizedMessage());
            FrontColor = null;
        }

        left_encoder = a_left_encoder_pos();
        right_encoder = a_right_encoder_pos();

        while (!isStarted()) {
            telemetry.addData("4: Heading", a_gyro_heading());
            telemetry.addData("6: Ground Color (Alpha)", a_ground_alpha());
            if (FrontColor!=null){
                telemetry.addData("8: beacon color",FrontColor.green());
            }else{
                telemetry.addData("front color not inited",0);
            }
            telemetry.addData("3",3);

            telemetry.update();
            idle();
        }
        v_motor_right_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        v_motor_left_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // drive forward away from wall
        timedrive(1000, .3f, .3f, -5);
        // turn towards beacon
        gyroturn(315, 11);
        // correct for any error in the turn to point at 45 degrees
        gyrohold(1000,315,2.5);
        setDrivePower(0, 0);
        // extend the plastic to read the beacon color and drive to the white line
        // if white line not detected then stop after 5 secs
        boolean extend=true;
        runtime.reset();
        while (a_ground_alpha() < 5 && opModeIsActive() && runtime.seconds() < 5) {
            if (v_servo_right_beacon!=null&&v_servo_left_beacon!=null){
                if (extend){
                    v_servo_left_beacon.setPower(1);
                    v_servo_right_beacon.setPower(1);
                }else {
                    v_servo_right_beacon.setPower(0);
                    v_servo_left_beacon.setPower(0);
                }
                if (runtime.milliseconds()>1080){
                    extend=false;
                }
            } else {
                telemetry.addData("CR servos don't work",1);
            }
            telemetry.addData("-1: time driving", runtime.milliseconds());
            double veryTempGyro= a_gyro_heading();
            double adjspeed = (.5 + .5) * Math.sin(((2 * Math.PI) / 360) * (veryTempGyro - 315));
            telemetry.addData("2: adjspeed: ", adjspeed);
            v_motor_left_drive.setPower(Range.clip(.4 - adjspeed, -1, 1));
            v_motor_right_drive.setPower(Range.clip(.4 + adjspeed, -1, 1));
            telemetry.addData("5: Heading ", veryTempGyro);
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.update();
            idle();

        }
        setDrivePower(0,0);
        // drive straight another 500 ms to center robot with the white line
        timedrive(500,.3f,.3f,315);
        // start tank turn towards white line
        setDrivePower(-.55f,.55f);
        // stop turning once white line is detected or after 5 secs
        runtime.reset();
        while (a_ground_alpha() < 5 && opModeIsActive() && runtime.seconds() < 5) {
            telemetry.addData("-1: time driving", runtime.milliseconds());
            telemetry.addData("5: Heading ", a_gyro_heading());
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.addData("12: actual left power ", actual_left_power());
            telemetry.update();
            idle();}
        runtime.reset();
        // drive straight towards the beacon after detecting the white line
        timedrive(375,.3f,.3f,270);
        // adjust angle of robot to face the wall if needed
        gyrohold(800,270,2.5);
        // finish drive to beacon until the color sensor sees red or blue
        runtime.reset();
        while (runtime.milliseconds()<=1000&&FrontColor.red()<2&&FrontColor.blue()<2&& opModeIsActive()){
            setDrivePower(.2f,.6f);
        }
        // adjustments to align to the beacon
        timedrive(300,.4,.4,270);
        timedrive(400,0,.4,270);
        // if the left beacon color is blue, extend the right beacon presser
        if(FrontColor.red()<2&&FrontColor.blue()>2){
            runtime.reset();
            v_servo_right_beacon.setPower(1);
            while (runtime.milliseconds()<2000){
                if (runtime.milliseconds()<1300){
                    v_servo_left_beacon.setPower(-1);
                }else {v_servo_left_beacon.setPower(0);}
                telemetry.update();
                telemetry.addData("1:beacon red ",FrontColor.red());
                telemetry.addData("2:beacon blue ",FrontColor.blue());
            }
            runtime.reset();
            left_beacon=-1;
            right_beacon=1;
            v_servo_left_beacon.setPower(0);
            v_servo_right_beacon.setPower(0);
            // else if the left beacon color is red, extend the left beacon presser
        }else if(FrontColor.red()>2&&FrontColor.blue()<2) {
            runtime.reset();
            v_servo_left_beacon.setPower(1);
            while (runtime.milliseconds() < 2000) {
                if (runtime.milliseconds() < 1300) {
                    v_servo_right_beacon.setPower(-1);
                }else {v_servo_left_beacon.setPower(0);}
                telemetry.update();
                telemetry.addData("1:beacon red ", FrontColor.red());
                telemetry.addData("2:beacon blue ", FrontColor.blue());
            }
            runtime.reset();
            left_beacon=1;
            right_beacon=-1;
            v_servo_left_beacon.setPower(0);
            v_servo_right_beacon.setPower(0);
            // otherwise do nothing
        }else {
            right_beacon=0;
            left_beacon=0;
        }
        setDrivePower(0,0);
        gyrohold(1000,90,0);
        //timedrive(1500,-.3f,-.3f,90);
        // back up and retract the beacon presser
        runtime.reset();
        while (opModeIsActive() && runtime.milliseconds()<1500) {
            telemetry.addData("-1: time driving",runtime.milliseconds());
            double adjspeed=(-.3+-.3)*Math.sin(((2*Math.PI)/360)*(a_gyro_heading()-270));
            telemetry.addData("2: adjspeed: " ,adjspeed);
            v_motor_left_drive.setPower(-.4-adjspeed);
            v_motor_right_drive.setPower(-.4+adjspeed);
            telemetry.addData("5: Heading ", a_gyro_heading());
            telemetry.addData("6: Ground Color (Blue) ", a_ground_blue());
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.addData("8: Beacon Red ", a_left_red());
            telemetry.addData("9: Beacon Blue ", a_left_blue());
            telemetry.addData("10: last state left ", left_encoder);
            telemetry.addData("11: last state right ", right_encoder);
            telemetry.addData("12: actual left power ", actual_left_power());
            telemetry.update();
            if (right_beacon==1){
                v_servo_right_beacon.setPower(-1);
            }
            if (left_beacon==1){
                v_servo_left_beacon.setPower(-1);
            }
            if (right_beacon==0){
                if (runtime.milliseconds()<1080){
                    v_servo_left_beacon.setPower(-1);
                    v_servo_right_beacon.setPower(-1);
                }else{
                    v_servo_right_beacon.setPower(0);
                    v_servo_left_beacon.setPower(0);
                }
            }
            // Allow time for other processes to run.
            idle();
        }
        setDrivePower(0,0);
        // turn towards the second beacon
        gyroturnLogistic(0,20,2.8);
        v_servo_left_beacon.setPower(0);
        v_servo_right_beacon.setPower(0);
        gyroholdLogistic(800,0,2);
        double tempColor=0;
        // put the color sensor in range and drive until the white line is detected
        extend=true;
        runtime.reset();
        while (tempColor < 5 && opModeIsActive() && runtime.seconds() < 5) {
            if (v_servo_right_beacon!=null&&v_servo_left_beacon!=null){
                if (extend){
                    v_servo_left_beacon.setPower(1);
                    v_servo_right_beacon.setPower(1);
                }else {
                    v_servo_right_beacon.setPower(0);
                    v_servo_left_beacon.setPower(0);
                }
                if (runtime.milliseconds()>1080){
                    extend=false;
                }
            } else {
                telemetry.addData("CR servos don't work",1);
            }
            telemetry.addData("-1: time driving", runtime.milliseconds());
            tempColor=GroundColor.alpha();
            double veryTempGyro= a_gyro_heading();
            double adjspeed = (.5 + .5) * Math.sin(((2 * Math.PI) / 360) * (veryTempGyro-350));
            telemetry.addData("2: adjspeed: ", adjspeed);
            v_motor_left_drive.setPower(Range.clip(.3 - adjspeed, -1, 1));
            v_motor_right_drive.setPower(Range.clip(.3 + adjspeed, -1, 1));
            telemetry.addData("5: Heading ", veryTempGyro);
            telemetry.addData("7: Ground Color (Alpha) ", tempColor);
            telemetry.update();
            idle();

        }
        setDrivePower(0,0);
        // drive past the white line to center the robot
        timedrive(500,.3f,.3f,315);
        // turn towards the beacon until the white line is detected or 5 secs
        setDrivePower(-.65f,.65f);
        runtime.reset();
        beacons.activate();
        boolean seeslego=false;
        while (a_ground_alpha() < 5 && opModeIsActive() && runtime.seconds() < 5) {
            telemetry.addData("-1: time driving", runtime.milliseconds());
            telemetry.addData("5: Heading ", a_gyro_heading());
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.addData("12: actual left power ", actual_left_power());
            telemetry.update();
            idle();}
        runtime.reset();
        if (seeslego){
            while (opModeIsActive()&&FrontColor.blue()<2&&FrontColor.red()<2){
                double deg=0;
                for(VuforiaTrackable beac : beacons){
                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                    if(pose != null){
                        VectorF translation = pose.getTranslation();

                        telemetry.addData(beac.getName() + "-Translation", translation);

                        double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));
                        if (degreesToTurn<0){
                            degreesToTurn+=180;
                        }else{
                            degreesToTurn-=180;
                        }
                        degreesToTurn*=-1;
                        telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                        if (Objects.equals(beac.getName(), "Wheels")){
                            deg=degreesToTurn;
                        }
                    }
                }
                telemetry.update();
                double adjspeed=(1)*Math.sin(((2*Math.PI)/360)*(deg));
                if (v_motor_left_drive!=null&&v_motor_right_drive!=null){
                    v_motor_left_drive.setPower(.4-adjspeed);
                    v_motor_right_drive.setPower(.4+adjspeed);
                }
            }
            setDrivePower(0,0);
            timedrive(300, .4, .4, 270);
            timedrive(400, 0, .4, 270);
        }else {
            // drive towards the beacon
            timedrive(500, .3f, .3f, 270);
            // adjust the angle of the robot and continue driving
            gyrohold(1000, 275, 2.5);
            runtime.reset();
            while (runtime.milliseconds() <= 500) {
                setDrivePower(.4f, 1f);
            }
            setDrivePower(0, 0);
        }
        // if red is detected extend the right beacon presser
        if(FrontColor.red()>2&&FrontColor.blue()<2){
            runtime.reset();
            while (runtime.milliseconds()<2000){
                v_servo_right_beacon.setPower(1);
                if (runtime.milliseconds()<1080){
                    v_servo_left_beacon.setPower(-1);
                }else {v_servo_left_beacon.setPower(0);}
                telemetry.update();
                telemetry.addData("1:beacon red ",FrontColor.red());
                telemetry.addData("2:beacon blue ",FrontColor.blue());
            }
            runtime.reset();
            v_servo_left_beacon.setPower(0);
/*            while (runtime.milliseconds()<2000) {
                v_servo_right_beacon.setPower(-1);
            } */
            left_beacon=-1;
            right_beacon=1;
            v_servo_left_beacon.setPower(0);
            v_servo_right_beacon.setPower(0);

            // else if blue is detected extend the left beacon presser
        }else if(FrontColor.red()<2&&FrontColor.blue()>2) {
            runtime.reset();
            while (runtime.milliseconds() < 2000) {
                v_servo_left_beacon.setPower(1);
                if (runtime.milliseconds() < 1080) {
                    v_servo_right_beacon.setPower(-1);
                }else {v_servo_left_beacon.setPower(0);}
                telemetry.update();
                telemetry.addData("1:beacon red ", FrontColor.red());
                telemetry.addData("2:beacon blue ", FrontColor.blue());
            }
            v_servo_right_beacon.setPower(0);
            runtime.reset();
/*            while (runtime.milliseconds() < 2000) {
                v_servo_left_beacon.setPower(-1);

            } */
            left_beacon=1;
            right_beacon=-1;
            v_servo_left_beacon.setPower(0);
            v_servo_right_beacon.setPower(0);
        }


//        gyrohold(1000,90,0);

        //move away from the wall
        timedrive(500,-.5f,-.5f,270);
        // turn towards the vortex base
        gyroturn(310, 11);
        // drive towards the vortex base
//        timedrive(2725,-1.0f,-1.0f,-5);
        runtime.reset();
        while (opModeIsActive() && runtime.milliseconds()<2725) {
            telemetry.addData("-1: time driving",runtime.milliseconds());
            double adjspeed=(-.3+-.3)*Math.sin(((2*Math.PI)/360)*(a_gyro_heading()-315));
            telemetry.addData("2: adjspeed: " ,adjspeed);
            v_motor_left_drive.setPower(-1 + adjspeed);
            v_motor_right_drive.setPower(-1 - adjspeed);
            telemetry.addData("5: Heading ", a_gyro_heading());
            telemetry.addData("6: Ground Color (Blue) ", a_ground_blue());
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.addData("8: Beacon Red ", a_left_red());
            telemetry.addData("9: Beacon Blue ", a_left_blue());
            telemetry.addData("10: last state left ", left_encoder);
            telemetry.addData("11: last state right ", right_encoder);
            telemetry.addData("12: actual left power ", actual_left_power());
            telemetry.update();
            if (right_beacon==1){
                if (runtime.milliseconds()<1500) {
                    v_servo_right_beacon.setPower(-1);
                }
            }
            if (left_beacon==1){
                if (runtime.milliseconds()<1500) {
                    v_servo_left_beacon.setPower(-1);
                }
            }
            if (right_beacon==0){
                if (runtime.milliseconds()<1080){
                    v_servo_left_beacon.setPower(-1);
                    v_servo_right_beacon.setPower(-1);
                }else{
                    v_servo_right_beacon.setPower(0);
                    v_servo_left_beacon.setPower(0);
                }
            }
            if (runtime.milliseconds()>1730&&runtime.milliseconds()<1800){
                v_motor_ball_shooter.setPower(1);
            }else {
                v_motor_ball_shooter.setPower(0);
            }

            // Allow time for other processes to run.
            idle();
        }
        // stop on the vortex base
        setDrivePower(0,0);
        v_motor_ball_shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        /*runtime.reset();
        while (a_ground_alpha() < 7 && opModeIsActive() && runtime.seconds() < 2.5) {
            telemetry.addData("-1: time driving", runtime.milliseconds());
            double adjspeed = (.5 + .5) * Math.sin(((2 * Math.PI) / 360) * (a_gyro_heading() - 45));
            telemetry.addData("2: adjspeed: ", adjspeed);
            v_motor_left_drive.setPower(Range.clip(.3 - adjspeed, -1, 1));
            v_motor_right_drive.setPower(Range.clip(.3 + adjspeed, -1, 1));
            telemetry.addData("5: Heading ", a_gyro_heading());
            telemetry.addData("6: Ground Color (Blue) ", a_ground_blue());
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.addData("8: Beacon Red ", a_left_red());
            telemetry.addData("9: Beacon Blue ", a_left_blue());
            telemetry.addData("10: last state left ", left_encoder);
            telemetry.addData("11: last state right ", right_encoder);
            telemetry.addData("12: actual left power ", actual_left_power());
            telemetry.update();
            idle();

        }
        timedrive(1000,.5,.5,15);
        gyroturn(270, 20);
        timedrive(400,.5,.5,270);
        gyroturn(270,5);
        gyrohold(1000,270);
        setDrivePower(0, 0);
        if (FrontColor!=null) {
            timedrive(2700, -.2, -.2, 90);
            setDrivePower(0, 0);
            runtime.reset();
            while (runtime.seconds() < 1) {
            }
            if (FrontColor.red() > 2) {
                timedrive(2000, .2, .2, 90);
                runtime.reset();
                while (runtime.seconds() < 1.5) {
                    telemetry.addData("red", 2);
                }
                timedrive(2000, -.2, -.2, 90);
                timedrive(2000, .2, .2, 90);
            } else {
                timedrive(2000, .2, .2, 90);
                while (runtime.seconds() < 1.5) {
                    telemetry.addData("blue", 2);
                }
            }
            gyroturn(15, 10);

            runtime.reset();
        }
  /*          while (a_ground_alpha() < 7 && opModeIsActive() && runtime.seconds() < 2.5) {
                telemetry.addData("-1: time driving", runtime.milliseconds());
                double adjspeed = (.5 + .5) * Math.sin(((2 * Math.PI) / 360) * (a_gyro_heading()));
                telemetry.addData("2: adjspeed: ", adjspeed);
                v_motor_left_drive.setPower(Range.clip(.3 - adjspeed, -1, 1));
                v_motor_right_drive.setPower(Range.clip(.3 + adjspeed, -1, 1));
                telemetry.addData("5: Heading ", a_gyro_heading());
                telemetry.addData("6: Ground Color (Blue) ", a_ground_blue());
                telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
                telemetry.addData("8: Beacon Red ", a_left_red());
                telemetry.addData("9: Beacon Blue ", a_left_blue());
                telemetry.addData("10: last state left ", left_encoder);
                telemetry.addData("11: last state right ", right_encoder);
                telemetry.addData("12: actual left power ", actual_left_power());
                telemetry.update();
                idle();

            }
            gyroturn(270,5);
            gyrohold(1000,270);
            runtime.reset();

            while ((!(FrontColor.blue() > 2 && FrontColor.red() < 2) || !(FrontColor.blue() > 2 && FrontColor.red() < 2)) && opModeIsActive()) {
                setDrivePower(-.3f, -.3f);
                telemetry.addData("5: Heading ", a_gyro_heading());
                telemetry.addData("6: Ground Color (Blue) ", a_ground_blue());
                telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
                telemetry.addData("8: Beacon Red ", a_left_red());
                telemetry.addData("9: Beacon Blue ", a_left_blue());
                telemetry.addData("10: last state left ", left_encoder);
                telemetry.addData("11: last state right ", right_encoder);
                telemetry.addData("12: actual left power ", actual_left_power());
                idle();
            }
            if (FrontColor.blue() > 2 && FrontColor.red() < 2) {
                gyroturn(60, 5);
                timedrive(1000, -.3f, -.3f, 60);
                timedrive(1000, 0, 0, 60);
                timedrive(1500, .3f, .3f, 60);
                gyroturn(0, 5);
            } else {
                gyroturn(120, 5);
                timedrive(1000, -.3f, -.3f, 120);
                timedrive(1000, 0, 0, 120);
                timedrive(1500, .3f, .3f, 120);
                gyroturn(0, 5);
            }
            gyroturn(45, 5);
            gyrohold(1000, 45);
            if (v_motor_ball_shooter != null && v_motor_lift != null) {
                v_motor_ball_shooter.setPower(1);
                timedrive(2000, -.5f, -.5f, 45);
                gyrohold(2000, 45);
                v_motor_lift.setPower(1);
                gyrohold(100, 45);
                v_motor_lift.setPower(0);
                v_motor_ball_shooter.setPower(1);
            }
            timedrive(500, -.3f, -.3f, 315);
    */

        /*


        double millel=0;
        while (millel<3000){
            runtime.reset();
            boolean addTime=true;
           telemetry.addData("0: target heading",0);
            telemetry.addData("1: actual heading",a_gyro_heading());
            double adjspeed=(1.75)*Math.sin(((2*Math.PI)/360)*(a_gyro_heading()-0));
            telemetry.addData("2: adjspeed: " ,adjspeed);
            if (v_motor_left_drive!=null&&v_motor_right_drive!=null){
                v_motor_left_drive.setPower(Range.clip(.3-adjspeed,-1,1));
                v_motor_right_drive.setPower(Range.clip(.3+adjspeed,-1,1));
            }
            if (sees blue){
                setDrivePower(0,0);
                addTime=false;
                press beacons;

            }
            if(addTime){
                millel=millel+runtime.milliseconds();
            }

        }
        gyroturn(315,5);
        gyrohold(1000,315);
        if (v_motor_ball_shooter!=null&&v_motor_lift!=null){
            v_motor_ball_shooter.setPower(1);
            timedrive(2000, -.5,-.5,315)
            gyrohold(2000,315);
            v_motor_lift.setPower(1);
            gyrohold(100,315);
            v_motor_lift.setPower(0);
            v_motor_ball_shooter.setPower(1);
        }
        timedrive(500,-.3,-.3,315);
         */


    }

































    public void gyrohold(int mills, int targetheading,double spmod)throws InterruptedException{
        runtime.reset();
        while (runtime.milliseconds()<mills&&opModeIsActive()){
            telemetry.addData("0: target heading",targetheading);
            telemetry.addData("1: actual heading",a_gyro_heading());
            double adjspeed=(spmod)*Math.sin(((2*Math.PI)/360)*(a_gyro_heading()-targetheading));
            telemetry.addData("2: adjspeed: " ,adjspeed);
            if (v_motor_left_drive!=null&&v_motor_right_drive!=null){
                v_motor_left_drive.setPower(Range.clip(-adjspeed,-1,1));
                v_motor_right_drive.setPower(Range.clip(adjspeed,-1,1));
            }
            telemetry.addData("3: time passed (ms)", runtime.milliseconds());
            telemetry.update();
            // Allow time for other processes to run.

            idle();
        }
    }
    public void gyroturn(int targetheading, int error)throws InterruptedException{
        runtime.reset();
        double tempGyro=999999;
        while((tempGyro<targetheading-error||tempGyro>targetheading+error)&&opModeIsActive()){
            tempGyro=a_gyro_heading();
            double adjspeed=(2.2)*Math.sin(((2*Math.PI)/360)*(tempGyro-targetheading));
            telemetry.addData("2: adjspeed: " ,adjspeed);
            if (v_motor_left_drive!=null&&v_motor_right_drive!=null){
                v_motor_left_drive.setPower(Range.clip(-adjspeed,-1,1));
                v_motor_right_drive.setPower(Range.clip(adjspeed,-1,1));
            }
            telemetry.addData("0: target heading",targetheading);
            telemetry.addData("1: actual heading",tempGyro);
            telemetry.addData("3: time passed (ms)", runtime.milliseconds());
            telemetry.addData("4: error range", error);
            telemetry.update();
            // Allow time for other processes to run.

            idle();
        }
        setDrivePower(0,0);
    }
    public void gyroholdLogistic(int mills, int targetheading,double spmod)throws InterruptedException{
        runtime.reset();
        while (runtime.milliseconds()<mills&&opModeIsActive()){
            telemetry.addData("0: target heading",targetheading);
            telemetry.addData("1: actual heading",a_gyro_heading());
            double adjspeed=(spmod)*SigAdjspeed(targetheading,a_gyro_heading());
            telemetry.addData("2: adjspeed: " ,adjspeed);
            if (v_motor_left_drive!=null&&v_motor_right_drive!=null){
                v_motor_left_drive.setPower(Range.clip(-adjspeed,-1,1));
                v_motor_right_drive.setPower(Range.clip(adjspeed,-1,1));
            }
            telemetry.addData("3: time passed (ms)", runtime.milliseconds());
            telemetry.update();
            // Allow time for other processes to run.

            idle();
        }
    }
    public void gyroturnLogistic(int targetheading, int error,double spmod)throws InterruptedException{
        runtime.reset();
        double tempGyro=999999;
        while((tempGyro<targetheading-error||tempGyro>targetheading+error)&&opModeIsActive()){
            tempGyro=a_gyro_heading();
            double adjspeed=spmod*SigAdjspeed(targetheading,tempGyro);
            telemetry.addData("2: adjspeed: " ,adjspeed);
            if (v_motor_left_drive!=null&&v_motor_right_drive!=null){
                v_motor_left_drive.setPower(Range.clip(-adjspeed,-1,1));
                v_motor_right_drive.setPower(Range.clip(adjspeed,-1,1));
            }
            telemetry.addData("0: target heading",targetheading);
            telemetry.addData("1: actual heading",tempGyro);
            telemetry.addData("3: time passed (ms)", runtime.milliseconds());
            telemetry.addData("4: error range", error);
            telemetry.update();
            // Allow time for other processes to run.

            idle();
        }
        setDrivePower(0,0);
    }

    public void timedrive(int mills, double speedleft, double speedright, int targetheading) throws InterruptedException{
        runtime.reset();
        while (opModeIsActive() && runtime.milliseconds()<mills) {
            telemetry.addData("-1: time driving",runtime.milliseconds());
            if (targetheading>=0&&targetheading<=360){
                double adjspeed=(speedleft+speedright)*Math.sin(((2*Math.PI)/360)*(a_gyro_heading()-targetheading));
                telemetry.addData("2: adjspeed: " ,adjspeed);
                v_motor_left_drive.setPower(speedleft-adjspeed);
                v_motor_right_drive.setPower(speedright+adjspeed);
            }
            else{
                v_motor_left_drive.setPower(speedleft);
                v_motor_right_drive.setPower(speedright);
            }

            telemetry.addData("5: Heading ", a_gyro_heading());
            telemetry.addData("6: Ground Color (Blue) ", a_ground_blue());
            telemetry.addData("7: Ground Color (Alpha) ", a_ground_alpha());
            telemetry.addData("8: Beacon Red ", a_left_red());
            telemetry.addData("9: Beacon Blue ", a_left_blue());
            telemetry.addData("10: last state left ", left_encoder);
            telemetry.addData("11: last state right ", right_encoder);
            telemetry.addData("12: actual left power ", actual_left_power());
            telemetry.update();

            // Allow time for other processes to run.
            idle();
        }
        setDrivePower(0,0);
    }

    public double SigAdjspeed(double correct, double current){
        double adjspeed;
        double deltaX;
        int dirmod;
        //constants to play with
        double L=1; //max value
        double k=.05; //how fast it grows
        double mid=45; //at here, adjspeed will be .5L.
        double cuttoff=.1; //if less than this, no correction will be made.
        if ((current-correct)%360<=180){
            deltaX=((current-correct)%360)-mid;
            dirmod=1;
        }else{
            dirmod=-1;
            deltaX=360-mid-((current-correct)%360);
        }
        adjspeed=L/(1+Math.exp(-k*deltaX))*dirmod;
        if (Math.abs(adjspeed)<cuttoff){
            adjspeed=0;
        }
        return adjspeed;
    }
    //methods
    //Modifies the left drive motor's power
    public void m_left_drive_power(float power){
        if (v_motor_left_drive!=null){
            float sendpower= Range.clip(power,-.6f,.6f);
            v_motor_left_drive.setPower(sendpower);
        }
    }
    public void m_intake_power(float power){
        if (v_motor_intake!=null){
            float sendpower= Range.clip(power,-1,1);
            v_motor_intake.setPower(sendpower);
        }
    }

    //Same as above, but for the right drive motor
    public void m_right_drive_power(float power){
        if (v_motor_right_drive!=null){
            float sendpower=Range.clip(power,-.6f,.6f);
            v_motor_right_drive.setPower(sendpower);
        }
    }

    // Sets both drive motors at once. In teleOp, will have correction if close.
    public void setDrivePower(float left_power, float right_power){
        m_left_drive_power(left_power);
        m_right_drive_power(right_power);

    }
    //calculates adjspeed, the speed correction factor in straightDrive.
    public double adjspeed(double speedModifier, int deltaAngle){
        return speedModifier*Math.sin(Math.toRadians(deltaAngle));
    }
    /* accesses the gyro's heading. If we can't find it, return an incorrect value that is divisible
    *  by 360. Remember that the gyro returns a clockwise heading. */
    public int a_gyro_heading(){
        if (SensorGyro!=null){
            return SensorGyro.getHeading();
        }
        else {return -36000;}
    }
    public double actual_left_power(){
        if (v_motor_left_drive!=null){
            return v_motor_left_drive.getPower();
        } else return 0;
    }
    public double actual_right_power(){
        if (v_motor_right_drive!=null){
            return v_motor_right_drive.getPower();
        } else return 0;
    }

    //Resets the left drive wheel encoder.
    public void reset_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setMode
                    ( DcMotor.RunMode.STOP_AND_RESET_ENCODER
                    );
        }

    }
    //Resets the right drive wheel encoder.
    public void reset_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setMode
                    ( DcMotor.RunMode.STOP_AND_RESET_ENCODER
                    );
        }

    }
    //Resets both encoders
    public void reset_drive_encoders(){
        reset_left_drive_encoder();
        reset_right_drive_encoder();
    }
    //Gets the left drive motor's encoder pos
    public int a_left_encoder_pos(){
        if (v_motor_left_drive!=null){
            return v_motor_left_drive.getCurrentPosition();
        }
        else {
            return 0;
        }
    }
    //Same as above, but for the right drive motor
    public int a_right_encoder_pos(){
        if (v_motor_right_drive!=null){
            return v_motor_right_drive.getCurrentPosition();
        }
        else {
            return 0;
        }
    }
    //Have the drive encoders reached a certain value?
    public boolean have_drive_encoders_reached(int left, int right, boolean forwards){
        if (forwards){
            return a_left_encoder_pos()>=left&&a_right_encoder_pos()>=right;
        }
        else {
            return a_left_encoder_pos()<=left&&a_right_encoder_pos()<=right;
        }
    }

    public double a_ground_blue(){
        if (GroundColor!=null) {
            return GroundColor.blue();
        }
        else return -1;
    }
    public double a_ground_alpha(){
        return GroundColor.alpha();

    }
    public double a_left_blue(){
        double returnthis = -1;
        if (SideColor != null) {
            returnthis = SideColor.blue();
        }
        return returnthis;
    }
    public double a_left_red(){
        double returnthis = -1;
        if (SideColor != null) {
            returnthis = SideColor.red();
        }
        return returnthis;
    }
}
