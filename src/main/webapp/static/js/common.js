layui.define(['hyUtil', 'jquery', 'layer'], function (exports) {
    var $ = layui.$,
        layer = layui.layer,
        hint = layui.hint();
    var clickAjax = function (event) {
        var me = $(this);
        var href = me.attr("href");
        if (href) {
            $.get(href, function (rst) {
                if (rst.code != 0) {
                    layer.msg(rst.msg);
                } else {
                    var ajaxDone = me.attr("ajaxDone");
                    if (ajaxDone) {
                        if ('refreshPage' === ajaxDone) {
                            window.location.reload();
                        }
                    }
                }
            });
        }
        event.preventDefault();
    };
    var clickDialog = function (event) {
        var me = $(this);
        var layData = me.attr("lay-data");
        if (layData) {
            try {
                var config = {
                    title: '未知的标题',
                    url: '',
                    width: 500,
                    height: 400,
                    params: {}
                };
                config = $.extend(config, new Function('return ' + layData)());
                var txt = "";
                if (!$.isEmptyObject(config.params)) {
                    $.each(config.params, function (k, v) {
                        txt += '&';
                        txt += k + "=" + v;
                    });
                }
                if (!$.isEmptyObject(txt)) {
                    txt = txt.substr(1);
                }
                if (config.url.indexOf('?') > -1) {
                    txt = "&" + txt;
                } else {
                    txt = "?" + txt;
                }
                hy.layerOpenUrl(config.title, config.url + txt, config.width, config.height);
            } catch (e) {
                hint.error('row menu property lay-data configuration item has a syntax error: ' + data)
            }
        }
        event.preventDefault();
    };
    var render = function () {
        $("a[target=ajax]").off().click(clickAjax);
        $("a[target=dialog]").off().click(clickDialog);
        $("button.hy-close-window").off().click(function () {
            hy.closeCurrentWindow();
            return false;
        });
    };
    render();
    exports('common', {render: render});
});
