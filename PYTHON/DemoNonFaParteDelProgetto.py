import time
import re
import cv2
import mss
import numpy
import screeninfo

with mss.mss() as sct:
    # Part of the screen to capture
    res = re.findall('\d+',str(screeninfo.get_monitors()[0]))
 
    monitor = {"top": 0, "left": 0, "width": int(res[0]), "height": int(res[1])}

    while "Screen capturing":
        last_time = time.time()

        # Get raw pixels from the screen, save it to a Numpy array
        img = numpy.array(sct.grab(monitor))
        cv2.imshow("OpenCV/Numpy normal", img)

        # Display the picture in grayscale
        # cv2.imshow('OpenCV/Numpy grayscale',
        #            cv2.cvtColor(res, cv2.COLOR_BGRA2GRAY))

        print("fps: {}".format(1 / (time.time() - last_time)))

        # Press "q" to quit
        if cv2.waitKey(30) & 0xFF == ord("q"):
            cv2.destroyAllWindows()
            break