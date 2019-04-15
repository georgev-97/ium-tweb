import socket
import time
import sys
import io
import numpy
import struct
import cv2
import subprocess
import re
import os
from ShellClient import ShellClient
from SenderClient import SenderClient

run = True
address='172.16.171.205'
port=2000

def getCommand():
    cm = input("remotroller> ")
    if(cm == "-c local"):
        connection.close()
        sys.exit(1)
    return cm

def performLocal(cm):
    global run
    if(cm == "-c remote"):
        run = False
    elif(cm == "-o desktop"):
        pid = subprocess.Popen([sys.executable, "RemoteDesktopClient.py"],shell=True)
    elif(cm == "-o shell"):
        s = listen(sock)
        shell = ShellClient(s)
        shell.start()
        shell.join()
    elif(cm == "-o sender"):
        s = listen(sock)
        shell = SenderClient(s)
        shell.start()
        shell.join()

def sendCommand(command, connection):
    try:
        command = struct.pack('>I', len(command)) + command
        connection.sendall(command)
    except:
        print("remotroller> connection broken")
        sys.exit(0)


def recvall(connection, n):
    data = b''
    while len(data) < n:
        packet = connection.recv(n - len(data))
        if not packet:
            print("remotroller> connection broken")
            connection.close()
            sys.exit(0)
        data += packet
    return data


def getResponse(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)

def bind():
    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Bind the socket to the port
    print('remotroller> starting up on %s port %s' % (address,port))
    sock.bind((address,port))
    sock.listen(0)
    return sock

def listen(sock):
    # Wait for a connection
    print('remotroller> waiting for connection ...')
    connection, serverAddress = sock.accept()
    print('remotroller> connection from', serverAddress)
    return connection


if __name__ == "__main__":
    global sock
    sock = bind()
    connection = listen(sock)  # waiting for reverse request from server

    while run:

        command = getCommand()
        if command:
            sendCommand(command.encode("utf-8"), connection)
            if run:
                response = getResponse(connection).decode("utf-8")
            if -1 == response.find("?!?£ab0rt£?!?"):#if service start fail on server, don't start it on client
                print("remotroller> "+response)
                performLocal(command)
            else:
                response = response.replace("?!?£ab0rt£?!?","")# ?!?£ab0rt£?!? is the abort code
                print("remotroller> "+response)