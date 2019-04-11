import socket, time, sys, io, numpy, struct, cv2, subprocess

connection = None
run = True

def getCommand():
    global run
    cm = input("remotroller> ")
    
    if(cm=="close remote"): 
        run = False
    elif(cm=="close local"):
        connection.close()
        exit(1)
    elif(cm=="remote desktop"):
        pid = subprocess.Popen([sys.executable, "RemoteDesktopClient.py"])
    return cm

def sendCommand(command,connection):
    try:
        command = struct.pack('>I', len(command)) + command
        connection.sendall(command)
    except:
        print("remotroller> connection broken")
        exit(0)

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

def getResponse(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)


def listen():
    global connection
    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Bind the socket to the port
    client_address = ('169.254.244.174', 1999)
    print(sys.stderr, 'starting up on %s port %s' % client_address)
    sock.bind(client_address)
    # Listen for incoming connections
    sock.listen(1)
    # Wait for a connection
    print (sys.stderr, 'waiting for a connection')
    connection, server_address = sock.accept()
    print(sys.stderr, 'connection from', server_address)
    return connection

if __name__ == "__main__":
    connection = listen()#waiting for reverse request from server

    while  run :
        
        command = getCommand().encode("utf-8")
        if command:
            sendCommand(command,connection)
            if run:
                response = getResponse(connection).decode("utf-8")
                print("remotroller> "+response)
        
        
        