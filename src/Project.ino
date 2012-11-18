#include <Servo.h>
Servo myservo;

void setup(){
  Serial.begin(9600);
  myservo.attach(3);
  stopservo();
}

void loop(){
  while(Serial.read() != 0)
  {
    if(Serial.read()==1)
    {
      Serial.println("right");
      right();
    }
    if(Serial.read()==2)
    {
      Serial.println("left");
      left();
    }
    if(Serial.read()==3)
    {
      stopservo();
    }
  }
}
void right()
{
  myservo.write(94);
}
void left()
{
  myservo.write(96);
}
void stopservo()
{
  myservo.write(95);
}

