import socket, struct, sys, time, pyautogui, re
from ScreenRecorder import ScreenRecorder
from threading import Thread

client_address = ('169.254.244.174', 10000)
mouseClientAddress = ('localhost', 10001)
mouseSock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
def mouseService(arg):
    # Bind the socket to the port
    mouseSock.bind(mouseClientAddress)

    while True:
        cord, address = mouseSock.recvfrom(4096)
        c = cord.decode("utf-8")
        xy = re.split("-",c)
        print(int(xy[0]),int(xy[1]))
        pyautogui.moveTo(int(xy[0]),int(xy[1]))
        pyautogui.click()

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
    thread = Thread(target = mouseService, args = (10, ))
    thread.daemon = True
    thread.start()

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