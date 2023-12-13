import os

import requests
import datetime
import json
from datetime import datetime, timedelta
key="7c43aee9e196839929914187be1b2d66"
city="441900"
url=f"https://restapi.amap.com/v3/weather/weatherInfo?key={key}&city={city}&extensions=base&output=JSON"
file="/data/info.json"

def update():
    r = requests.get(url)
    js = r.json()
    js["last_time"] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    f = open(file, "w+")
    json.dump(js, f)
    f.close()


def check_time_difference(filename):

    # 读取 JSON 文件中的时间
    with open(filename, 'rb') as json_file:
        data = json.load(json_file)
        file_time_str = data.get("last_time")
        if not file_time_str:

            return True
        # 将字符串时间转换为时间对象
        file_time = datetime.strptime(file_time_str, "%Y-%m-%d %H:%M:%S")

        # 计算当前时间与文件时间的差异
        time_difference = datetime.now() - file_time

        # 判断差异是否超过一天
        if time_difference > timedelta(minutes=30):
            return True
        else:
            return False

if not os.path.exists(file):
    update()

if check_time_difference(file):
    update()
with open(file,"r+") as f:
    js=json.load(f.buffer)
    temper=js["lives"][0]['temperature_float']
    x=js["lives"][0]
    wea=x["weather"]
    c="东莞"
    print(c,wea,temper)





