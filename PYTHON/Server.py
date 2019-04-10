import socket, struct, time, sys, os, subprocess
from threading import Thread

sock = None
client_address = ('localhost', 9999)
              

def close():
    sock.close()
    exit(1)

def startRemotedesktop():
    pid = subprocess.Popen([sys.executable, "RemoteDesktopServer.py"])
    if pid:
        return "opened, press q on the windows to close"
    else:
        return "error"

def default():
    return "invalid command"


def commandPerform(command):
    switcher = {
        "close remote": close, #command sended by client , to close both(server and client)
        "remote desktop": startRemotedesktop
    }
    if(switcher.__contains__(command)):
        return switcher.get(command)()
    else:
        return default()

#asyncronous listener for command coming from console
def listenInputAsynchronous(arg):
    while True:
        command = input("remotroller> ")

        if(command == "close"):#command recived by server console, to close the server
            sock.close()
            os._exit(1)

#send byte response to client
def sendResponse(response):
    response = struct.pack('>I', len(response)) + response
    sock.sendall(response)

#recive n byte from the socket
def recvall(connection, n):
    data = b''
    while len(data) < n:
        packet = connection.recv(n - len(data))
        if not packet:
            raise AssertionError
        data += packet
    return data

#recive an entire message from the socket
def getCommand(connection):
    try:
        raw_msglen = recvall(connection, 4)
        msglen = struct.unpack('>I', raw_msglen)[0]
        return recvall(connection, msglen)
    except AssertionError :
        raise AssertionError
#try to revers connect to client, return true if connection is enstablished, false if not
def tryConnection():
    global sock
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(client_address)
        return True
    except:
        return False
#try every delay second to connect
def waitForConnection(delay):
    connect = False
    print("remotroller> tryng to establish connection to "+str(client_address))
    while not connect:
        connect = tryConnection()
        time.sleep(delay)
    print("remotroller> connection establisched")

#handle the server routin
if __name__ == "__main__":
    #starting thread to listend console input command
    thread = Thread(target = listenInputAsynchronous, args = (10, ))
    thread.daemon = True
    thread.start()

    #try every 1 second to connect
    waitForConnection(1)

    run = True
    while run:
        try:
            #intercetting request, that must be decoded ==> decode("utf-8")
            command = getCommand(sock).decode("utf-8")
            print("remotroller> recived "+command)
            #serving request, returned response that must be encoded ==> encode("utf-8")
            response = commandPerform(command).encode("utf-8")
            sendResponse(response)
        except AssertionError:
            #try every 1 second to reconnect
            print("remotroller> connection broken to "+str(client_address))
            sock.close()
            waitForConnection(1)

        