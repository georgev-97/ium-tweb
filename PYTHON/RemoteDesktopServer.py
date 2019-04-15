import socket
import struct
import sys
import time
import pyautogui
import re
import getopt
from Recorder import Recorder
from threading import Thread

port=-1
address=""
expectedArgument = len(('-a','-p')) #list of neded argument
camera = False

#parse input param
def getParam():
    global address
    global port
    global camera

    if not len(sys.argv[1:]):
        sys.exit(0)
    try:
        op,ar = getopt.getopt(sys.argv[1:],"a:p:c",["address=","port=","cam"])
    except:
        sys.exit(0)

    noOptionalArg=0
    for o,a in op:
        if o in ('-a','--address'):
            noOptionalArg+=1
            address = a
        elif o in ('-p','--port'):
            noOptionalArg+=1
            port = int(a)
        elif o in ('-c','--cam'):
            camera = True


    if noOptionalArg != expectedArgument:
        sys.exit(0)

# recive n byte from socket
def recvall(connection, n):
    data = b''
    while len(data) < n:
        try:
            packet = connection.recv(n - len(data))
        except:
            connection.close()
            sys.exit(1)
        data += packet
    return data
# get remote mouse command


def getInput(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)

# simulate mouse movement, following the remote mouse command


def inputService():
    mouseSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(mouseSock, (address,port), 0.2)

    while True:
        i = getInput(mouseSock).decode("utf-8")
        input = re.split("-", i)
        type = str(input[0])
        if(type == "click"):
            pyautogui.moveTo(int(input[1]), int(input[2]))
            pyautogui.click()
        elif(type == "key"):
            key = input[1]
            pyautogui.press(key)

def cameraService(cameraSock, camera):
    while True:
        frame = camera.getCompressedCameraFrame()
        frame = struct.pack('>I', len(frame)) + frame
        try:
            cameraSock.sendall(frame)
        except:
            cameraSock.close()
            sys.exit(0)

# try to revers connect to client, return true if connection is enstablished, false if not


def tryConnection(sock, add):
    try:
        sock.connect(add)
        return True
    except:
        return False

# try every delay second to connect


def waitForConnection(sock, add, delay):
    connect = False
    while not connect:
        connect = tryConnection(sock, add)
        time.sleep(delay)


if __name__ == "__main__":
    getParam()
    screen = Recorder()
    desktop = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(desktop, (address,port), 0.2)

    if camera:# if -c option
        camera = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        waitForConnection(camera, (address,port), 0.2)
        #starting camera service
        cameraThread = Thread(target=cameraService,args=[camera, screen])
        cameraThread.daemon = True
        cameraThread.start()

    # starting mouse service
    mouseThread = Thread(target=inputService,)
    mouseThread.daemon = True
    mouseThread.start()

    run = True
    while run:
        # get a compressed desktop frame
        imgOut = screen.getCompressedScreenFrame()
        # Send data
        imgOut = struct.pack('>I', len(imgOut)) + imgOut
        try:
            desktop.sendall(imgOut)
        except:
            print(
                "remotroller> client have closed remote desktop, process will be stopped")
            sys.exit(1)
