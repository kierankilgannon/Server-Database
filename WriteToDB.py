import serial

port = serial.Serial("/dev/ttyUSB0", baudrate=10412, timeout=3.0)

f = open("myData.txt","a+")

while True:
    #port.write(t)
    rcv = port.readline()
    print(rcv)
    f.write(str(rcv))
#f.close()
    #port.write("\r\nYou Sent:" + repr(rcv))