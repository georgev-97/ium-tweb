import socket
import struct
import time
import sys
import os
import subprocess
from threading import Thread

sock = None
address = 'localhost'
port = 2000
errorCode = "?!?£ab0rt£?!?"


def close():
    sock.close()
    os._exit(1)


def startRemotedesktop(option=None):
    c=""
    if "-c" in option:
        c="-c"
    pid = subprocess.Popen([sys.executable,'RemoteDesktopServer.py','-a',address,'-p','1999',c])
    time.sleep(1)
    if not None == pid.poll():
        return errorCode+"failed to launch remote service"
    else:
        return "remote service launced, press <pageUp> on the windows to close it"

def startShell(option=None):
    pid = subprocess.Popen("python ShellServer.py -a "+address+" -p "+str(port),shell=True)
    time.sleep(1)
    if not None == pid.poll():
        return errorCode+"failed to launch remote service"
    else:
        return "remote service started"

def startSender(option=None):
    pid = subprocess.Popen("python SenderServer.py -a "+address+" -p "+str(port),shell=True)
    time.sleep(1)
    if not None == pid.poll():
        return errorCode+"failed to launch remote service"
    else:
        return "remote service started"


def default():
    return "invalid command"


def commandPerform(command):
    cm = command.split(" ")
    if(len(cm) > 1):
        command = cm[0]+" "+cm[1]
        switcher = {
            # command sended by client , to close both(server and client)
            "-c remote": close,
            "-o desktop": startRemotedesktop,
            "-o shell":startShell,
            "-o sender":startSender
        }
        if(switcher.__contains__(command)):
            return switcher.get(command)(option=cm[2:])
        else:
            return default()
    else:
            return default()      

# asyncronous listener for command coming from console


def listenInputAsynchronous(arg):
    while True:
        command = input("remotroller> ")

        if(command == "close"): 
            sock.close()
            os._exit(1)

# send byte response to client


def sendResponse(response):
    response = struct.pack('>I', len(response)) + response
    sock.sendall(response)

# recive n byte from the socket


def recvall(connection, n):
    data = b''
    while len(data) < n:
        try:
            packet = connection.recv(n - len(data))
        except:
            raise AssertionError
        if not packet:
            raise AssertionError
        data += packet
    return data

# recive an entire message from the socket


def getCommand(connection):
    try:
        raw_msglen = recvall(connection, 4)
        msglen = struct.unpack('>I', raw_msglen)[0]
        return recvall(connection, msglen)
    except:
        raise AssertionError
# try to revers connect to client, return true if connection is enstablished, false if not


def tryConnection():
    global sock
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((address, port))
        return True
    except:
        return False
# try every delay second to connect


def waitForConnection(delay):
    connect = False
    print("remotroller> tryng to establish connection to "+str((address, port)))
    while not connect:
        connect = tryConnection()
        time.sleep(delay)
    print("remotroller> connection establisched")


# handle the server routin
if __name__ == "__main__":
    # starting thread to listend console input command
    thread = Thread(target=listenInputAsynchronous, args=(10, ))
    thread.daemon = True
    thread.start()

    # try every 1 second to connect
    waitForConnection(1)

    run = True
    while run:
        try:
            # intercetting request, that must be decoded ==> decode("utf-8")
            command = getCommand(sock).decode("utf-8")
            print("remotroller> recived "+command)
            # serving request, returned response that must be encoded ==> encode("utf-8")
            response = commandPerform(command).encode("utf-8")
            sendResponse(response)
        except AssertionError:
            # try every 1 second to reconnect
            print("remotroller> connection broken to "+str((address, port)))
            sock.close()
            waitForConnection(1)
