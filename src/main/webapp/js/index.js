$(function () {
    hljs.initHighlightingOnLoad();

    var init = function (data) {
        $('title').html(data.path);
        $('#path').val(data.path);
        $('.markdown-body').html('');
        markdown_refresh(data.units);
        highlight_code();
        scroll_if_possible();
    };

    var sync = function (data) {
        markdown_refresh(data.units);
        highlight_code();
        scroll_if_possible();
    };

    var markdown_refresh = function (units) {
        $.each(units, function (i, u) {

            if (u.operate === 'REPLACE') {
                if (u.id === 'toc_container') {
                    $('.markdown-body').css('padding-left', '200px');
                    var toc = $('#' + u.id);
                    toc.html(u.content);
                    toc.show();
                } else {
                    $('#' + u.id).replaceWith(u.content);
                }
            } else if (u.operate === 'APPEND') {
                $('.markdown-body').append(u.content);
            } else if (u.operate === 'REMOVE') {
                if (u.id === 'toc_container') {
                    $('.markdown-body').css('padding-left', '45px');
                    var cot = $('#' + u.id);
                    cot.html('');
                    cot.hide();
                } else {
                    $('#' + u.id).remove();
                }
            }

            if (u.isMathJax === 1) {
                MathJax.Hub.Queue(['Typeset', MathJax.Hub, u.id]);
            }
        })
    };

    var close = function () {
        window.opener = null;
        window.open('', '_self');
        window.close();
    };

    var highlight_code = function () {
        $('pre code').each(function (i, block) {
            hljs.highlightBlock(block);
        });
    };

    var scroll_if_possible = function () {
        var marker = document.getElementById('_markdown_preview_sync_bottom_marker');
        if (marker !== null) {
            marker.scrollIntoView(false);
        }
    };

    var url = 'ws://127.0.0.1:' + window.location.port + '/ws';
    if (!WebSocket) {
        console.warn('WebSocket is not support');
    } else {
        console.log('Try to connect ' + url);
        var ws = new WebSocket(url);
        ws.onerror = function (e) {
            console.error('Error : ' + e.message);
        };

        ws.onopen = function () {
            console.log('Connected');
        };

        ws.onclose = function () {
            console.log('Disconnected');
            close();
        };

        ws.onmessage = function (d) {
            console.log('Response : ' + d.data.length);
            var data = JSON.parse(d.data);
            var path = $('#path');
            if (path.val() === '') {
                init(data);
            } else {
                if (path.val() === data.path) {
                    if (data.command === 'close') {
                        close()
                    } else if (data.command === 'sync') {
                        sync(data);
                    }
                }
            }
        };
    }


    $(window).bind('beforeunload', function () {
        ws.send('close,' + $('#path').val());
    });


    self.mark_line = function (line) {
        if (ws !== null) {
            ws.send(line);
        } else {
            console.warn('WebSocket is close');
        }
    };
});
