import socket, struct, sys, time, pyautogui, re
from ScreenRecorder import ScreenRecorder
from threading import Thread

streamClientAddress = ('169.254.244.174', 2000)
mouseClientAddress = ('169.254.244.174', 1998)

#recive n byte from socket
def recvall(connection, n):
    data = b''
    while len(data) < n:
        packet = connection.recv(n - len(data))
        if not packet:
            print("remotroller> connection broken") 
            connection.close() 
            exit(0)
        data += packet
    return data
#get remote mouse command
def getMouse(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)

#simulate mouse movement, following the remote mouse command
def mouseService(arg):
    mouseSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(mouseSock, mouseClientAddress, 1)

    while True:
        cord = getMouse(mouseSock)
        c = cord.decode("utf-8")
        xy = re.split("-",c)
        print(int(xy[0]),int(xy[1]))
        pyautogui.moveTo(int(xy[0]),int(xy[1]))
        pyautogui.click()

#try to revers connect to client, return true if connection is enstablished, false if not
def tryConnection(sock, address):
    try:
        sock.connect(address)
        return True
    except:
        return False

#try every delay second to connect
def waitForConnection(sock, address, delay):
    connect = False
    while not connect:
        connect = tryConnection(sock, address)
        time.sleep(delay)

if __name__ == "__main__":
    #starting mouse service
    thread = Thread(target = mouseService, args = (10, ))
    thread.daemon = True
    thread.start()

    #start catching and sending frame
    screen = ScreenRecorder()
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    waitForConnection(sock, streamClientAddress, 1)
    run = True
    while run:
        # get a compressed desktop frame
        imgOut = screen.getCompressedFrame()
        # Send data
        imgOut = struct.pack('>I', len(imgOut)) + imgOut
        try:
            sock.sendall(imgOut)
        except:
            print("remotroller> client have closed remote desktop, process will be stopped")
            exit(1)