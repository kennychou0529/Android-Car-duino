#include "sensordata.hpp"
#include "maneuver.hpp"

namespace Autodrive {
    namespace Overtaking {
        int usFront = SensorData::ultrasound.front;
        int irFrontRight = SensorData::infrared.frontright;
        int irRearRight = SensorData::infrared.rearright;
        int objectDistance = 50;
        float overtaking = 0;
        bool obstacleMet = false;


        command run(command lastCommand, Mat* mat) {
            if (usFront > 0 && usFront < objectDistance) {
                if (overtaking < 1) overtaking = SensorData::encoderDistance();;
            }

            if (overtaking > 0) {
                if (! obstacleMet) {
                    cv::putText(*mat, "overtaking", POINT(50.f, mat->size().height / 3.f), cv::FONT_HERSHEY_PLAIN, 1, cv::Scalar(0, 255, 0), 2);
                }

                lastCommand.setSpeed(slowSpeed);

                if ((SensorData::encoderDistance() - overtaking < 40)) {
                    lastCommand.setAngle(-1);
                }

                if (irRearRight > 0 && irRearRight < 10) if (! obstacleMet) obstacleMet = true;

                if (obstacleMet) {
                    cv::putText(*mat, "obstacle met", POINT(50.f, mat->size().height / 3.f), cv::FONT_HERSHEY_PLAIN, 1, cv::Scalar(0, 255, 0), 2);

                    if (irFrontRight < 0 || irFrontRight > 20) {
                        lastCommand.setSpeed(0);
                    }
                }
            }

             return lastCommand;
        }
    }
}