import socket,sys,os,subprocess,time,struct
from threading import Thread

homeDir = os.getcwd()

def getCommand(str="remotroller> "):
    cm = input(str)
    if cm=="-c sender":
        sys.exit(0)
    return cm

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


def getResponse(connection):
    raw_msglen = recvall(connection, 4)
    msglen = struct.unpack('>I', raw_msglen)[0]
    return recvall(connection, msglen)

def navigateFile():
    print("remotroller> navigate to file folder and type the file name")
    run = True
    while run:
        p = subprocess.Popen("dir",stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True,shell=True)
        dir,err = p.communicate()
        print(dir+err)
        cm = getCommand(str=os.getcwd()+" ")
        if  dir.find(cm) != -1:#ERRORE: find() non garantisce che il nome sia scritto per intero utilizzare una re
            return cm
        if cm[:2] != "cd":
            print("remotroller> only <cd> command are accepted")
        else:
            try:
                os.chdir(cm[2:].replace(" ","",1))
            except FileNotFoundError as e:
                print("remotroller> ERROR: "+str(e))


class SenderClient(Thread):

    def __init__(self,sock):
        Thread.__init__(self)
        self.connection = sock

    def run(self):
        connection = self.connection
        fileName = navigateFile()
        send(fileName.encode("utf-8"),connection)

        re = getResponse(connection).decode("utf-8")
        print(re)
        
        while True:
            re = getResponse(connection).decode("utf-8") 
            if re == "?!?bre@k?!?":
                break
            send(getCommand(str=re).encode("utf-8"),connection)


        with open(fileName,"rb") as f:
            send(f.read(),connection)
    os.chdir(homeDir)        
        

