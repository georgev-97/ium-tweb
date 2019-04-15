import socket
import struct
import sys
import time
import pyautogui
import re
from Recorder import Recorder
from threading import Thread

clientAddress = ('172.16.171.205', 1999)

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
    waitForConnection(mouseSock, clientAddress, 0.2)

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


def tryConnection(sock, address):
    try:
        sock.connect(address)
        return True
    except:
        return False

# try every delay second to connect


def waitForConnection(sock, address, delay):
    connect = False
    while not connect:
        connect = tryConnection(sock, address)
        time.sleep(delay)


if __name__ == "__main__":
    screen = Recorder()
    desktop = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(desktop, clientAddress, 0.2)

    camera = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(camera, clientAddress, 0.2)
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
