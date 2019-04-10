import socket
import time
import sys
import io
import numpy
import struct
import cv2


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

def listen():
    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Bind the socket to the port
    client_address = ('localhost', 10000)
    sock.bind(client_address)

    # Listen for incoming connections
    sock.listen(1)

    # Wait for a connection
    connection, server_address = sock.accept()
    return connection

if __name__ == "__main__":
    connection = listen()#waiting for reverse request
    #start show remote desktop
    run = True
    while  run :
        img = getFrame(connection)
        cv2.imshow("imm",img)
        if cv2.waitKey(30) & 0xFF == ord("q"):
            cv2.destroyAllWindows()
            exit(1)
        
        