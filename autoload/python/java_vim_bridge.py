# -*- coding: utf-8 -*-
"""Bridge connect vim to java

@author wocanmei
@date 2018-07-15 17:56:25
"""

import time
import socket


MAX_CONN_TIMES = 5
SEP = '__%#mpsync&@__'
EOF = '\0'

s = None


def start(port, theme):
    is_connected = False
    connect_cnt = 0
    while not is_connected and connect_cnt < MAX_CONN_TIMES:
        connect_cnt += 1
        try:
            _connect(port, theme)
        except Exception:
            is_connected = False
            time.sleep(1)
        else:
            is_connected = True

    if not is_connected:
        raise Exception('Fail to connect after ' + str(connect_cnt) + ' tries')


def _connect(port, theme):
    global s
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('127.0.0.1', 23789))
    s.send('start' + SEP + str(port) + SEP + theme + EOF)


def open(path):
    s.send('open' + SEP + path + EOF)


def sync(path, content, bottom):
    s.send('sync' + SEP + path + SEP + content + SEP + str(bottom) + EOF)


def close(path):
    s.send('close' + SEP + path + EOF)


def stop():
    s.send('stop' + EOF)
    s.close()
