import time
import mss
import numpy
import screeninfo
import re
import io

class ScreenRecorder:

    def __init__(self):
        # Part of the screen to capture
        res = re.findall("\d+",str(screeninfo.get_monitors()[0]))
        self.monitor = {"top": 0, "left": 0, "width": int(res[0]), "height": int(res[1])}

    def getCompressedFrame(self):
        with mss.mss() as sct:
            # Get raw pixels from the screen, save it to a Numpy array
            img = numpy.array(sct.grab(self.monitor))
            #compressing image Array
            compImg = io.BytesIO()
            numpy.savez_compressed(compImg,img=img)
            compImg.seek(0)
            return compImg.read()
