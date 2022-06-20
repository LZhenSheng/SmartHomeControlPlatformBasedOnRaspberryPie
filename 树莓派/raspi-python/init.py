import RPi.GPIO as GPIO
import time


class Init(object):
    def __init__(self, port):
        self.port = port
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        GPIO.setup(self.port, GPIO.IN)
        self.on_state = GPIO.HIGH
        self.off_state = not self.on_state

    def set_on(self):
        GPIO.setup(self.port, GPIO.OUT)
        GPIO.output(self.port, self.on_state)

    def set_off(self):
        GPIO.setup(self.port, GPIO.OUT)
        GPIO.output(self.port, self.off_state)

    def is_on(self):
        return GPIO.input(self.port) == self.on_state

    def is_off(self):
        return GPIO.input(self.port) == self.off_state


class Light(Init):
    def __init__(self, port):
        super(Light, self).__init__(port)
        GPIO.setup(self.port, GPIO.OUT)

    def toggle(self):
        if self.is_on():
            self.set_off()
        else:
            self.set_on()

    def blink(self, t=0.5):
        i = 0
        while i < 3:
            self.set_on()
            time.sleep(t)
            self.set_off()
            time.sleep(t)
            i += 1
            #print(i)
