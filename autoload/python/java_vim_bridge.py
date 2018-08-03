# -*- coding: utf-8 -*-
"""Bridge connect vim to java

@author wocanmei
@date 2018-07-15 17:56:25
"""

import base64

try:
    from urllib.parse import urlencode
    from urllib.request import urlopen, Request
except ImportError:
    from urllib import urlencode
    from urllib2 import urlopen, Request


url_pre = 'http://127.0.0.1:port'


def set_port(port):
    global url_pre
    url_pre = url_pre.replace('port', port)


def sync(path, content, bottom):
    params = {'path': path, 'content': base64.b64encode(content), 'bottom': bottom}
    request = Request(url_pre + '/sync', data = urlencode(params))
    urlopen(request)


def close(path):
    params = {'path': path}
    request = Request(url_pre + '/close?' + urlencode(params))
    urlopen(request)


def stop():
    request = Request(url_pre + '/stop')
    urlopen(request)
