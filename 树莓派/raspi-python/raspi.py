import _thread
import base64
import json
import os
import re
import signal
import random
from linkkit import linkkit
import pymysql as pymysql
import requests as requests
from aip import AipSpeech
from MyEncoder import MyEncoder
import snowboydecoder
from init import Init, Light
import time
import adafruit_dht

ProductKey = "gxtyIZJ9otJ"
DeviceName = "mypi"
DeviceSecret = "642e70a3085721e6d5865b7a32951fc1"


def on_connect(session_flag, rc, userdata):
    print("on_connect:%d,rc:%d,userdata:" % (session_flag, rc))
    pass


def on_disconnect(rc, userdata):
    print("on_disconnect:rc:%d,userdata:" % rc)


def on_subscribe_topic(mid, granted_qos, userdata):  # 订阅topic
    # lk.subscribe_topic(lk.to_full_topic("user/getLight"), 1)
    # lk.subscribe_topic(lk.to_full_topic("user/getFan"), 1)
    print("on_subscribe_topic mid:%d, granted_qos:%s" %
          (mid, str(','.join('%s' % it for it in granted_qos))))
    pass


def on_topic_message(topic, payload, qos, userdata):
    if topic == '/gxtyIZJ9otJ/mypi/user/getFan':
        print(1)
        if str(payload)[9] == '1':
            fan.set_off()
            print(2)
        else:
            fan.set_on()
            print(3)
    else:
        if str(payload)[11] == '1':
            led1.set_on()
        else:
            led1.set_off()
    print("阿里云上传回的数值是:", topic)
    data=str(payload)[2:-1]
    print("阿里云上传回的数值是:",payload[7])
    print("阿里云上传回的数值是:",payload)
    dataDict=json.loads(data)
    print("阿里云上传回的数值是:",type(dataDict))
    print(dataDict["temp"]["value"])

    pass


def on_unsubscribe_topic(mid, userdata):
    print("on_unsubscribe_topic mid:%d" % mid)
    pass


def on_publish_topic(mid, userdata):
    print("on_publish_topic mid:%d" % mid)


# 百度语音合成
def baidu_tts(words):
    result = client.synthesis(words, 'zh', 1, {'vol': 5, 'per': 4})
    if not isinstance(result, dict):
        with open('audio.mp3', 'wb') as f:
            f.write(result)
        os.system("cvlc audio.mp3 --play-and-exit")
    else:
        print(result)


interrupted = False


def signal_handler(signal, frame):
    global interrupted
    interrupted = True


def interrupt_callback():
    global interrupted
    return interrupted


# 人脸搜索返回处理
def faceSearch(img, access_token):
    # wake_up()
    output = getsearchMessage(img, access_token)
    if output['error_msg'] == 'SUCCESS':
        user_list = output['result']['user_list']
        print(user_list)
        score = user_list[0]['score']
        user = user_list[0]['user_id']
        if user == '234':
            print("sdfj")
            words = "认证成功,您是胡雅焜." + "相似度为百分之" + \
                    str(round(score, 2)) + ",快来唤醒小派吧!"
            baidu_tts(words)
            wake_up()
    else:
        print(output['error_msg'])
        baidu_tts("对不起,认证失败!")


# 身份认证
def verify():
    baidu_tts("您好,我是小派,使用小派之前将先对您进行身份认证哦!")
    print("开始认证...")
    os.system('fswebcam -S 10 image.jpg')
    f = open('image.jpg', 'rb')
    img = base64.b64encode(f.read())
    faceSearch(img, access_token)


# 数据库连接参数
con_config = {
    'host': "42.192.116.184",
    'port': 3306,
    'user': "root",
    'password': "123456",
    'database': "db_raspberry"
}


# 人脸识别接口请求
def getsearchMessage(img, access_token):
    request_url = "https://aip.baidubce.com/rest/2.0/face/v3/search"
    params = {"image": img, "image_type": "BASE64", "group_id_list": "123123", "quality_control": "LOW",
              "liveness_control": "NORMAL"}
    request_url = request_url + "?access_token=" + access_token
    response = requests.post(request_url, data=params)
    return response.json()


# 百度云api连接参数
baidu_config = {
    'APP_ID': '18329444',
    'API_KEY': '5zrkKb7HYGWcSFDYEVpf2xaC',
    'SECRET_KEY': 'qqm9cm8XpGbcgUr1q5Lfd8E8y9rMGp3P'
}


def signal_handler(signal, frame):
    global interrupted
    interrupted = True


def wake_up():
    global detector
    model = 'resources/小派.pmdl'
    # capture SIGINT signal, e.g., Ctrl+C
    signal.signal(signal.SIGINT, signal_handler)
    detector = snowboydecoder.HotwordDetector(model, sensitivity=0.5)
    print('Listening... Press Ctrl+C to exit')
    detector.start(detected_callback=callbacks,
                   interrupt_check=interrupt_callback, sleep_time=0.03)
    detector.terminate()


def callbacks():
    global detector
    print("成功唤醒小派!")
    snowboydecoder.play_audio_file()  # dingaccess_token
    detector.terminate()  # close
    Speech(access_token)
    wake_up()


# 在线语音识别  与逻辑处理
def Speech(access_token):
    global detector
    request_url = "http://vop.baidu.com/server_api"
    headers = {'Content-Type': 'application/json'}
    WAVE_FILE = "beginSpeech.wav"
    # begin Speech
    answers = ["我在,请说话.", "来了,来了,我来了!", "您好,老大,我是小派!", "我是机智可爱的小派,有何指示?"]
    baidu_tts(answers[randomNumber(answers)])
    print("小派开始录音了,录音时间3s!")
    os.system(
        'arecord -d 3 -r 16000 -c 1 -t wav -f S16_LE  beginSpeech.wav')  # 采集音频
    # 采集音频
    print("录音结束,语音识别中...")
    f = open(WAVE_FILE, "rb")  # 以二进制读模式打开输入音频
    speech = base64.b64encode(f.read())  # 读音频文件并使用base64编码
    size = os.path.getsize(WAVE_FILE)  # 获取文件大小(字节)
    data_json = json.dumps(
        {"format": "wav", "rate": 16000, "channel": 1, "cuid": "zhl", "token": getaccess_token2(),
         "speech": speech, "len": size}, cls=MyEncoder, indent=4)  # 请求数据格式
    response = requests.post(request_url, data=data_json,
                             headers=headers)  # 发起requests
    print(response.json())
    if response.json().get('err_msg') == 'success.':  # 处理返回数据
        words = response.json().get('result')[0]
        print("您说的是:" + words)
        if re.findall('[歌曲音乐]', words):
            os.system('cvlc music.mp3')
        # elif re.findall('关.*[歌曲音乐]', words):
        # pygame.mixer.music.stop()
        elif re.findall('[温湿度]', words):
            words = insertAndPublishData()
            baidu_tts(words)
        elif re.findall('[天气]', words):
            baidu_tts(str(getWeatherData()))
        elif re.findall('开.*灯', words):
            led1.set_on()
            baidu_tts("老大,灯已打开.")
        elif re.findall('关.*灯', words):
            led1.set_off()
            baidu_tts("老大,灯已关闭.")
        elif re.findall('开.*[风扇]', words):
            fan.set_off()
            baidu_tts("老大,风扇已打开.")
        elif re.findall('关.*[风扇]', words):
            fan.set_on()
            baidu_tts("老大,风扇已关闭.")
        else:
            baidu_tts(tuling(words))
    else:
        baidu_tts("对不起老大,小派没有听清.")


# 获取天气
def getWeatherData():
    # 实况天气
    url_now = "https://free-api.heweather.net/s6/weather/now?location=guilin&key=8bf5d16e26c54784818061c82ac15030"
    # 生活指数
    url_lifestyle = "https://free-api.heweather.net/s6/weather/lifestyle?location=guilinimage&key=8bf5d16e26c54784818061c82ac15030"
    response_now = requests.get(url=url_now)
    response_lifestyle = requests.get(url=url_lifestyle)
    self_now = response_now.json()
    self_lifestyle = response_lifestyle.json()
    data = self_now['HeWeather6'][0]['basic']['location'] + '实时天气:' + self_now['HeWeather6'][0]['now'][
        'cond_txt'] + ',温度:' + \
           self_now['HeWeather6'][0]['now'][' 7tmp'] + ',湿度:' + self_now['HeWeather6'][0]['now']['hum'] + ',风力:' + \
           self_now['HeWeather6'][0]['now']['wind_sc'] + '级,' + \
           self_lifestyle['HeWeather6'][0]['lifestyle'][0]['txt']
    return data


# 图灵机器人智能对话
def tuling(words):
    request_url = "http://openapi.tuling123.com/openapi/api/v2"
    api_key = "e01ee05ea2164b1c8f786734ae3274a8"
    headers = {'content-type': 'application/json'}
    request_data = {"userInfo": {"apiKey": api_key, "userId": "zhanghuilin"},
                    "perception": {"inputText": {"text": words}}}
    response = requests.post(
        request_url, data=json.dumps(request_data), headers=headers)
    response_text = response.json()["results"][0]["values"]["text"]
    return response_text


# 查询tb_dht11表数据
def queryData():
    url_now = "https://free-api.heweather.net/s6/weather/now?location=350881&key=8bf5d16e26c54784818061c82ac15030"
    # 生活指数
    url_lifestyle = "https://free-api.heweather.net/s6/weather/lifestyle?location=350881&key=8bf5d16e26c54784818061c82ac15030"
    response_now = requests.get(url=url_now)
    response_lifestyle = requests.get(url=url_lifestyle)
    self_now = response_now.json()
    self_lifestyle = response_lifestyle.json()
    data = '温度:' + \
           self_now['HeWeather6'][0]['now']['tmp'] + '湿度:' + self_now['HeWeather6'][0]['now']['hum']
    return data


# 生成随机数
def randomNumber(str):
    length = len(str)
    num = random.randint(0, length - 1)
    return num


# 获取百度云token
def getaccess_token():
    host = 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=Ao9VHcGcwdDte8SC3rhqB9n5&client_secret=9GtXYI0NoFhxqgSheO4oywYU070rGFGU'
    header_1 = {'Content-Type': 'application/json; charset=UTF-8'}
    request = requests.post(host, headers=header_1)
    access_token = request.json()['access_token']
    return access_token


# 获取百度云token
def getaccess_token2():
    host = 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=FMEQ9ZT5edkhez7MCMZ2s3Zd&client_secret=j3yt6rE7t4gVr0nocUIPs8siCHNQuvl1'
    header_1 = {'Content-Type': 'application/json; charset=UTF-8'}
    request = requests.post(host, headers=header_1)
    access_token = request.json()['access_token']
    # print(access_token)
    return access_token


# 监控人脸认证
def monitorSearch(img, access_token):
    output = getsearchMessage(img, access_token)
    if output['error_msg'] != 'SUCCESS':
        return 1
    else:
        pass


import oss2


# 图片上传到阿里云oss
def oss_upload(img_url):
    # oss配置
    endpoint = 'oss-cn-beijing.aliyuncs.com'
    auth = oss2.Auth('LTAI5tHQrZUnCN2XmbxpnLot', 'XafW6TPKItxmVVQs4C81KRcWqhDc5x')
    bucket = oss2.Bucket(auth, endpoint, '2342342344234324')
    bucket.put_object_from_file("image.jpg", img_url)
    # bucket.put_object(file_path, file)
    # 返回一个可以访问的url链接
    return f'http://oss.phpkt.com.com/image.jpg'


# 热释电红外传感器监控
def humanMonitoring():
    if human.is_on():  # 监测高电平则表示热释电红外传感器监测到异常
        print("监测到有人靠近...开始人脸检测...")
        os.system('fswebcam -S 10 image.jpg')
        f = open('image.jpg', 'rb')
        img = base64.b64encode(f.read())
        result = monitorSearch(img, access_token)  # 将采集到的图像上传百度云人脸库搜索
        if result == 1:  # 图像搜索不到则表示陌生物体靠近
            print("234234")
            led1.blink()  # 红灯警告
            data = {"human": 1}
            lk.publish_topic(lk.to_full_topic("user/putDHT111"), str(data))
            oss_upload("/home/pi/snowboy/examples/Python3/image.jpg")
    else:
        pass


def thread1():
    while 1:
        humanMonitoring()


def thread2():
    while 1:
        smokeMonitoring()

def on_mjpegstreamer_start():
    os.system("sh /home/pi/finaldesign/mjpg-streamer/start.sh")


# MQ-2烟雾传感器监控
def smokeMonitoring():
    if smoke.is_off():  # 监测高电平则表示MQ-2烟雾传感器检测到有害气体
        print("working")
        led1.blink()  # 红灯警告
        data = {"fire": 1}
        lk.publish_topic(lk.to_full_topic("user/putFire"), str(data))
        print("222")
        # mqttClient.publish(topics['topic4'], json.dumps(
        #     {"smoke": 1}), 0)  # 发布MQTT消息到阿里云物联网平台
    else:
        pass


def thread3():
    insertAndPublishData()


# 记录dht11的数据插入tb_dht11表并发布到阿里云物联网平台
def insertAndPublishData():
    conn = pymysql.connect(**con_config)  # 打开数据库连接
    cur = conn.cursor()  # 使用cursor()方法获取操作游标
    sql_delete = "truncate table tb_dht11;"  # 插入数据库之前先清空表所有内容
    cur.execute(sql_delete)  # 使用execute方法执行SQL语句
    dhtDevice = adafruit_dht.DHT11(5)
    for i in range(1, 10):  # 记录10000条数据
        try:
            # Print the values to the serial port
            temperature_c = dhtDevice.temperature
            temperature_f = temperature_c * (9 / 5) + 32
            humidity = dhtDevice.humidity
            print("Temp: {:.1f} F / {:.1f} C    Humidity: {}% ".format(temperature_f, temperature_c, humidity))
            if temperature_c is not None and humidity is not None:
                realtime = time.strftime('%Y-%m-%d %H:%M:%S')
                # 插入数据库
                insert_data = [
                    (str(temperature_c), str(humidity), realtime)
                ]
                sql_insert = "insert into tb_dht11(temperature,humidity,realtime) values(%s,%s,%s)"
                cur.executemany(sql_insert, insert_data)
                conn.commit()  # 提交到数据库执行
                # time.sleep(1)
                fan.set_on()
        except RuntimeError as error:
            # Errors happen fairly often, DHT's are hard to read, just keep going
            print(error.args[0])
        time.sleep(1.0)
    conn.close()  # 关闭数据库连接


# 收到订阅消息
def on_message(mqttClient, userdata, msg):
    msg = str(msg.payload, encoding='utf8')
    print(msg)
    setjson = json.loads(msg)
    print(setjson)
    ledChecked0 = setjson['ledList'][0]['ledChecked0']
    ledChecked1 = setjson['ledList'][1]['ledChecked1']
    fanChecked0 = setjson['fanList'][0]['fanChecked0']
    led1.set_on() if ledChecked1 == 1 else led1.set_off()
    led2.set_on() if ledChecked0 == 1 else led2.set_off()
    fan.set_off() if fanChecked0 == 1 else fan.set_on()


# 阿里云物联网平台mqtt连接参数
aliyun_config = {
    'productKey': 'gxtyIZJ9otJ',
    'deviceName': 'mypi',
    'deviceSecret': '642e70a3085721e6d5865b7a32951fc1',
    'port': 1883,
    'host': 'gxtyIZJ9otJ.iot-as-mqtt.cn-shanghai.aliyuncs.com'
}

if __name__ == '__main__':
    led1 = Light(17)  # led1 BCM 17 红灯
    led2 = Light(22)  # led2 BCM 22 黄灯
    human = Init(18)  # 热释电红外传感器 BCM 18 VCC b1 GND b14
    smoke = Init(20)  # 烟雾传感器 BCM 20
    fan = Init(26)  # 风扇 BCM libpython326
    dht = Init(5)  # dht11 BCM 5
    access_token = getaccess_token()
    client = AipSpeech(
        baidu_config['APP_ID'], baidu_config['API_KEY'], baidu_config['SECRET_KEY'])

    lk = linkkit.LinkKit(
        host_name="cn-shanghai",
        product_key=ProductKey,
        device_name=DeviceName,
        device_secret=DeviceSecret)
    lk.on_connect = on_connect
    lk.on_disconnect = on_disconnect
    lk.on_subscribe_topic = on_subscribe_topic
    lk.on_topic_message = on_topic_message
    lk.on_publish_topic = on_publish_topic
    lk.on_unsubscribe_topic = on_unsubscribe_topic
    lk.connect_async()
    time.sleep(2)
    print(lk.to_full_topic("user/getLight"))
    lk.subscribe_topic(lk.to_full_topic("user/getLight"), 1)
    lk.subscribe_topic(lk.to_full_topic("user/getFan"), 1)
    _thread.start_new_thread(thread1,())
    _thread.start_new_thread(thread2,())
    _thread.start_new_thread(thread3,())
    verify()
    wake_up()
    time.sleep(1000)
