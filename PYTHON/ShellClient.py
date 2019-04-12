import socket,time,sys,subprocess,getopt
from threading import Thread

def getCommand(path):
    cm = input(path)
    if cm=="-c shell":
        exit(0)
    return cm

def sendCommand(command, connection):
    try:
        connection.sendall(command)
    except:
        print("remotroller> connection broken")
        exit(0)
   
def getResponse(connection):
    packet = connection.recv(4096)
    if not packet:
        print("remotroller> connection broken")
        connection.close()
        exit(0)
    return packet

class ShellClient(Thread):

    def __init__(self,sock):
        Thread.__init__(self)
        self.connection = sock

    def run(self):
        connection = self.connection
        jump = False
        while True:
            if not jump: 
                re = str(getResponse(connection), "utf-8")
            cm = getCommand(re).encode("utf-8")
            if cm:
                sendCommand(cm, connection)
                jump = False
            else:
                jump = True

