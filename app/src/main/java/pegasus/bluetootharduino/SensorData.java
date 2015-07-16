package pegasus.bluetootharduino;

//TODO: We might want to remove this class completely and just use something like Autodrive.SensorData instead
public class SensorData {
    public static int ultrasonicFront = 0,
        ultrasonicFrontRight = 0,
        ultrasonicRear = 0,
        infraredSideFront = 0,
        infraredSideRear = 0,
        infraredRear = 0,
        gyroHeading = 0,
        razorHeading = 0,
        encoderPulses = 0;

    public static boolean lineLeftFound = false;
    public static boolean lineRightFound = false;

    static void setUltrasound(int sensor, int value){
        Autodrive.setUltrasound(sensor, value);
        new DataPoster("ultrasonic", "sensor" + sensor, value);

        if (sensor == 0) {
            ultrasonicFront = value;
        } else if (sensor == 1) {
            ultrasonicFrontRight = value;
        } else if (sensor == 2) {
            ultrasonicRear = value;
        }
    }

    static void setInfrared(int sensor, int value){
        Autodrive.setInfrared(sensor, value);
        new DataPoster("infrared", "sensor" + sensor, value);

        if (sensor == 0) {
            infraredSideFront = value;
        } else if (sensor == 1) {
            infraredSideRear = value;
        } else if (sensor == 2) {
            infraredRear = value;
        }
    }

    static void setEncoderPulses(int value){
        Autodrive.setEncoderPulses(value);

        encoderPulses = value;
    }

    static void setGyroHeading(int value){
        Autodrive.setGyroHeading(value);

        gyroHeading = value;
    }

    static void setRazorHeading(int value){
        Autodrive.setRazorHeading(value);
        String heading = "WEST";
        if (value >= -45 && value < 45){
            heading = "NORTH";
        }else if (value >= 45 && value < 135){
            heading = "EAST";
        }else if (value >= 135 && value < -135){
            heading = "SOUTH";
        }
        new DataPoster("heading",heading);
        razorHeading = value;
    }

    static void lineLeftFound() {
        Autodrive.lineLeftFound();

        lineLeftFound = true;
    }

    static void lineRightFound() {
        Autodrive.lineRightFound();

        lineRightFound = true;
    }

    static void handleInput(String input){
        input = input.replaceAll("\\r|\\n", "");

        if (input.startsWith("EN")){
            setEncoderPulses(Integer.parseInt(input.substring(3)));
        }else if (input.startsWith("HE")){
            setGyroHeading(Integer.parseInt(input.substring(3)));
        }else if (input.startsWith("RZR")){
            setRazorHeading(Integer.parseInt(input.substring(4)));
        }else if (input.startsWith("US")){
            int sensorNum = Integer.parseInt(input.substring(2,3));
            setUltrasound(sensorNum - 1, Integer.parseInt(input.substring(4)));
        } else if (input.startsWith("IR")){
            int sensorNum = Integer.parseInt(input.substring(2,3));
            setInfrared(sensorNum - 1,Integer.parseInt(input.substring(4)));
        } else if (input.startsWith("lineL")){
            lineLeftFound();
        } else if (input.startsWith("lineR")){
            lineRightFound();
        }else if (input.startsWith("RI")){
            setRightLights(Integer.parseInt(input.substring(3)));
        }else if (input.startsWith("LE")){
            setLeftLights(Integer.parseInt(input.substring(3)));
        }else if (input.startsWith("ST")){
            setStopLights(Integer.parseInt(input.substring(3)));
        }else if (input.startsWith("SP")){
            setSpeed(Integer.parseInt(input.substring(3)));
        }

//        CameraActivity.updateDebuggingConsole();
    }

    private static void setRightLights(int status) {
        new DataPoster("lights", "signal-right", status);
    }

    private static void setLeftLights(int status){
        new DataPoster("lights", "signal-left", status);
    }

    private static void setStopLights(int status){
        new DataPoster("lights", "brake", status);
    }

    private static void setSpeed(int speed) {
        new DataPoster("speed",String.valueOf(speed));
    }
}
