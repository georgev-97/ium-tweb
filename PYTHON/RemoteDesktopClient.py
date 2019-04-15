import socket
import time
import sys
import io
import numpy
import struct
import cv2
import getopt
from pynput import keyboard
from threading import Thread

mouseSock = None
camera = False
port=-1
address=""
expectedArgument = len(('-a','-p')) #list of neded argument

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
            print("cliiiii")
            camera = True

    if noOptionalArg != expectedArgument:
        sys.exit(0)

def clickSend(x, y, code):
    cord = (code+"-"+str(x)+"-"+str(y)).encode("utf-8")
    try:
        # Send data
        cord = struct.pack('>I', len(cord)) + cord
        mouseSock.sendall(cord)
    except Exception as e:
        print("remotroller> "+str(e))


def convertKey(key):
    list = {13: "enter", 2490368: "up", 2621440: "down",
            2424832: "left", 2555904: "right"}
    if list.__contains__(key):
        return list.get(key)
    else:
        try:
            return chr(key)
        except:
            return ""


def keySend(key, code):
    k = (code+"-"+convertKey(key)).encode("utf-8")
    try:
        # Send data
        k = struct.pack('>I', len(k)) + k
        mouseSock.sendall(k)
    except Exception as e:
        print("remotroller> "+str(e))


def clickEvent(event, x, y, flags, param):
    if event == cv2.EVENT_LBUTTONDOWN:
        clickSend(x, y, "click")
    if event == cv2.EVENT_LBUTTONDBLCLK:
        clickSend(x, y, "click")


def recvall(connection, n):
    data = b''
    while len(data) < n:
        packet = connection.recv(n - len(data))
        if not packet:
            raise AssertionError
        data += packet
    return data


def recvFrame(connection):
    try:
        raw_msglen = recvall(connection, 4)
        msglen = struct.unpack('>I', raw_msglen)[0]
        return recvall(connection, msglen)
    except AssertionError:
        raise AssertionError

def getFrame(connection):
    try:
        compImg = io.BytesIO()
        compImg.write(recvFrame(connection))
        compImg.seek(0)
        return numpy.load(compImg)['img']
    except AssertionError:
        connection.close()
        sys.exit(1)

def cameraService(cameraSock):
    cv2.namedWindow("camera",cv2.WINDOW_FREERATIO)
    while True:
        frame = getFrame(cameraSock)
        cv2.imshow("camera",frame)
        cv2.waitKey(1)

def bindSock(sock, add):
    # Bind the socket to the port
    sock.bind(add)
    sock.listen(2)


def listen(sock):
    # Wait for a connection
    try:
        connection, serverAddress = sock.accept()
    except:
        sock.close()
        sys.exit(0)
    return connection


if __name__ == "__main__":
    getParam()
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # binding the sock
    bindSock(sock, (address,port))
    # 1° request coming from server is used to stream desktop
    streamSock = listen(sock)
    if camera:
        # 2° request coming from server is used to stream camera
        cameraSock = listen(sock)
        #starting camera service
        cameraThread = Thread(target=cameraService,args=[cameraSock])
        cameraThread.daemon = True
        cameraThread.start()
    # 3° request coming from server is used to send input signal
    mouseSock = listen(sock)

    # start show remote desktop
    run = True
    cv2.namedWindow('desktop',cv2.WINDOW_FREERATIO)
    while run:
        img = getFrame(streamSock)  # getting a frame coming from the sock
        cv2.imshow("desktop", img)
        cv2.setMouseCallback("desktop", clickEvent) # listening for mouse input, 
                                                #clickEvent() callback will send input to server
        key = cv2.waitKeyEx(1)                  # listening for keyboard input
        if (key != -1):
            if(key == 2162688):                 #if pageUp pressed close window
                cv2.destroyAllWindows()
                sys.exit(1)
            keySend(key, "key")                 #else send key to server
