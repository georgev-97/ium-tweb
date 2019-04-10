import socket, struct, sys, time
from ScreenRecorder import ScreenRecorder

client_address = ('localhost', 10000)

#try to revers connect to client, return true if connection is enstablished, false if not
def tryConnection():
    global sock
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(client_address)
        return True
    except:
        return False

#try every delay second to connect
def waitForConnection(delay):
    connect = False
    while not connect:
        connect = tryConnection()
        time.sleep(delay)

if __name__ == "__main__":
    screen = ScreenRecorder()
    waitForConnection(1)
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