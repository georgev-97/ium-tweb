import socket, time, sys, io, numpy, struct, cv2
from pynput import keyboard
from threading import Thread

mouseSock = None
mouseClientAddress = ('192.168.1.8', 1998)
clientAddress = ('192.168.1.8', 2000)

def clickSend(x,y,code):
    cord = (code+"-"+str(x)+"-"+str(y)).encode("utf-8")
    try:
        # Send data
        cord = struct.pack('>I', len(cord)) + cord
        mouseSock.sendall(cord)
    except Exception as e:
        print("remotroller> "+str(e))

def convertKey(key):
    list = {13:"enter",2490368:"up",2621440:"down",2424832:"left",2555904:"right"}
    if list.__contains__(key):
        return list.get(key)
    else:
        try:
            return chr(key)
        except:
            return ""    

def keySend(key,code):
    k = (code+"-"+convertKey(key)).encode("utf-8")
    try:
        # Send data
        k = struct.pack('>I', len(k)) + k
        mouseSock.sendall(k)
    except Exception as e:
        print("remotroller> "+str(e))

def click_event(event, x, y, flags, param):
    if event == cv2.EVENT_LBUTTONDOWN:
        clickSend(x,y,"click")
    if event == cv2.EVENT_LBUTTONDBLCLK:
        clickSend(x,y,"click")

    


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

def getFrame (connection):
    try:
        compImg = io.BytesIO()
        compImg.write(recvFrame(connection))
        compImg.seek(0)
        return numpy.load(compImg)['img']
    except AssertionError:
        connection.close()
        exit(1)

def listen(sock, address):
    # Bind the socket to the port
    sock.bind(address)
    # Listen for incoming connections
    sock.listen(1)
    # Wait for a connection
    connection, serverAddress = sock.accept()
    return connection

if __name__ == "__main__":
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connection = listen(sock, clientAddress)#waiting for reverse request
    mSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    mouseSock = listen(mSock, mouseClientAddress)#waiting for reverse request
    
    #start show remote desktop
    run = True
    while  run :
        img = getFrame(connection)
        cv2.imshow("imm",img)
        cv2.setMouseCallback("imm", click_event)
        key = cv2.waitKeyEx(1)
        if (key != -1):
            if(key == 2162688):
                cv2.destroyAllWindows()
                exit(1)
            keySend(key, "key")
            
        
        