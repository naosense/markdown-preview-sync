# -*- coding: utf-8 -*-
"""Bridge connect vim to java

@author wocanmei
@date 2018-07-15 17:56:25
"""

import time
import socket
import sys


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
    s.sendall(wrap('start' + SEP + str(port) + SEP + theme + EOF))


def open(path):
    s.sendall(wrap('open' + SEP + path + EOF))


def sync(path, content, bottom):
    s.sendall(wrap('sync' + SEP + path + SEP + content + SEP + str(bottom) + EOF))


def close(path):
    s.sendall(wrap('close' + SEP + path + EOF))


def stop():
    s.sendall(wrap('stop' + EOF))
    s.close()


def version():
    return sys.version_info[0]


def wrap(data):
    if version() == 2:
        return data
    elif version() == 3:
        return data.encode()
