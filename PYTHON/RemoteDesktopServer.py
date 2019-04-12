import socket
import struct
import sys
import time
import pyautogui
import re
from ScreenRecorder import ScreenRecorder
from threading import Thread

clientAddress = ('192.168.1.8', 1999)
mouseClientAddress = ('192.168.1.8', 1998)

# recive n byte from socket


def recvall(connection, n):
    data = b''
    while len(data) < n:
        try:
            packet = connection.recv(n - len(data))
        except:
            connection.close()
            exit(1)
        data += packet
    return data
# get remote mouse command


def getInput(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)

# simulate mouse movement, following the remote mouse command


def inputService(arg):
    mouseSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(mouseSock, clientAddress, 1)

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

    # start catching and sending frame
    screen = ScreenRecorder()
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(sock, clientAddress, 1)

    # starting mouse service
    thread = Thread(target=inputService, args=(10, ))
    thread.daemon = True
    thread.start()

    run = True
    while run:
        # get a compressed desktop frame
        imgOut = screen.getCompressedFrame()
        # Send data
        imgOut = struct.pack('>I', len(imgOut)) + imgOut
        try:
            sock.sendall(imgOut)
        except:
            print(
                "remotroller> client have closed remote desktop, process will be stopped")
            exit(1)
