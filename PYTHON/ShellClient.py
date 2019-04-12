import socket, time, sys, io, numpy, struct, cv2, subprocess

client_address = ('192.168.1.8', 1997)

connection = None
run = True

def getCommand(path):
    cm = input(path)
    return cm

def sendCommand(command,connection):
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


def listen():
    global connection
    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Bind the socket to the port
    print('remotroller> starting up on %s port %s' % client_address)
    sock.bind(client_address)
    # Listen for one incoming connections
    sock.listen(1)
    # Wait for a connection
    print ('remotroller> waiting for connection ...')
    connection, server_address = sock.accept()
    print('remotroller> connection from', server_address)
    return connection

if __name__ == "__main__":
    connection = listen()#waiting for reverse request from server
    while run:
        re = str(getResponse(connection),"utf-8")
        cm = getCommand(re).encode("utf-8")
        sendCommand(cm, connection)


    
        
        
        