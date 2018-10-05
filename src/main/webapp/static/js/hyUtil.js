layui.define(['jquery', 'layer'], function (exports) {
    var $ = layui.$,
        layer = layui.layer;
    $.ajaxSetup({
        complete: function (xhr, ts) {
            // console.log(xhr, ts);
            if (xhr.status == 401) {
                top.window.location = "login.jsp";
            } else if (xhr.status != 200) {
                layer.msg("请求服务器异常");
            }
        }
    });
    $(function () {
        var me = window.hy = {};
        me.debug = false;
        var console = {};
        console.log = function () {
            if (me.debug) {
                window.console.log.apply(this, arguments);
            }
        };
        function guid() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }

        function getUrlParam(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            // if (r != null) return unescape(r[2]);
            if (r != null) return (r[2]);
            return null; //返回参数值
        }

        me.toPointArr = function (txt) {
            var arr = txt.split(";");
            var rst = [];
            $.each(arr, function (idx, one) {
                var xyArr = one.split(",");
                if (xyArr.length == 2) {
                    var x = Number(xyArr[0]);
                    var y = Number(xyArr[1]);
                    rst.push(new BMap.Point(x, y));
                }
            });
            return rst;
        };
        me.getURLParam = getUrlParam;
        me.getURLGuid = function () {
            var guid = getUrlParam("guid");
            return guid;
        };
        me.rebind = function (evtName, func) {
            console.log("rebind evt: ", evtName);
            top.layui.$(top.window).unbind(evtName);
            top.layui.$(top.window).on(evtName, func);
        };
        me.unbind = function (evtName) {
            console.log("unbind evt: ", evtName);
            top.layui.$(top.window).unbind(evtName);
        };
        me.trigger = function (evtName, data) {
            console.log("trigger evt: ", evtName);
            top.layui.$(top.window).trigger(evtName, data);
        };
        me.eventData = function (evtName, data) {
            me.rebind(evtName, function () {
                return data;
            });
        };
        me.getEventData = function (evtName) {
            var event = top.layui.$.Event(evtName);
            top.layui.$(top.window).trigger(event);
            return event.result;
        };
        me.msg = function () {
            top.layui.layer.msg.apply(this, arguments);
        };
        me.setNeedRefresh = function () {
            top.layui.$.data(top.window, "needRefresh", true);
        };
        me.getAndRemoveNeedRefresh = function () {
            var val = top.layui.$.data(top.window, "needRefresh");
            if (val) {
                top.layui.$.removeData(top.window, "needRefresh");
                return true;
            }
        };
        me.closeCurrentWindow = function () {
            if (top.layui) {
                var parentIndex = top.layui.layer.getFrameIndex(window.name);
                top.layui.layer.close(parentIndex);
            }
        };
        me.layerOpen = function () {
            if (top.layui) {
                top.layui.layer.open.apply(this, arguments);
            }
        };
        me.layerOpenUrl = function (title, url, width, height, endCallback) {
            console.log(arguments);
            if (title == null || title == '') {
                title = false;
            }
            if (width == null || width == '') {
                width = 800;
            }
            if (height == null || height == '') {
                height = ($(window).height() - 50);
            }
            me.layerOpen({
                type: 2,
                area: [width + 'px', height + 'px'],
                fix: false, //不固定
                maxmin: true,
                shade: 0.4,
                title: title,
                content: url,
                end: function () {
                    if (endCallback) {
                        endCallback(arguments);
                    }
                }
            })
        };
        me.popup = (function () {
            var selectedEvent = "paqx.popup.selected";
            var defaultDataEvent = "paqx.popup.defaultData";

            var notifySelected = function (data, gid, closeFn) {
                console.log("$$$$$$$$$", arguments);
                if (!gid) {
                    gid = me.getURLGuid();
                }
                if (gid && $.trim(gid).length > 0) {
                    hy.trigger(selectedEvent + gid, [data, closeFn]);
                } else {
                    hy.trigger(selectedEvent, [data, closeFn]);
                }
            };
            var getDefaultData = function (gid) {
                if (!gid) {
                    gid = me.getURLGuid();
                }
                console.log("popup default data for guid: ", gid);
                var evtName = defaultDataEvent;
                if (gid) {
                    evtName += gid;
                }
                return me.getEventData(evtName);
            };
            var openSelect = function (param, dataCallback) {
                var gid = guid();
                var selectEvtName = selectedEvent + gid;
                var defaultDataEvtName = defaultDataEvent + gid;
                var _param = {
                    defaultData: {},
                    width: 400,
                    height: 450,
                    maxmin: true,
                    shade: 0.4,
                    dataCallback: function (success, data, closeFn) {
                        if (success) {
                            console.log("popup select success, data is ", data);
                        } else {
                            console.log('popup select no thing');
                        }
                        if (closeFn) {
                            closeFn();
                        }
                    }
                };
                if (dataCallback && dataCallback != null) {
                    _param.dataCallback = dataCallback;
                }
                if (param) {
                    $.extend(_param, param);
                }
                if (!_param.url) {
                    hy.msg("url不能为空");
                    return;
                } else {
                    if (_param.url.indexOf('?') >= 0) {
                        _param.url += "&guid=" + gid;
                    } else {
                        _param.url += "?guid=" + gid;
                    }
                }
                var isDataBack = false;
                me.rebind(defaultDataEvtName, function () {
                    console.log("receive evt: ", defaultDataEvtName);
                    return _param.defaultData;
                });
                me.rebind(selectEvtName, function (evt, data, closeFn) {
                    isDataBack = true;
                    if (!closeFn) {
                        closeFn = $.noop;
                    }
                    _param.dataCallback(true, data, closeFn);
                });

                me.layerOpen({
                    type: 2,
                    area: [_param.width + 'px', _param.height + 'px'],
                    fix: false, //不固定
                    maxmin: _param.maxmin,
                    shade: _param.shade,
                    title: _param.title,
                    content: _param.url,
                    end: function () {
                        console.log("popup select window close, isDataBack is ", isDataBack);
                        if (!isDataBack) {
                            _param.dataCallback(false, null, $.noop);
                        }
                        me.unbind(selectEvtName);
                        me.unbind(defaultDataEvtName);
                    }
                });
            };
            var openSelectOne = function (setVal, title, url, width, height, maxmin) {
                var p = {title: title, url: url};
                if (width) {
                    p.width = width;
                }
                if (height) {
                    p.height = height;
                }
                if (maxmin) {
                    p.maxmin = maxmin;
                }
                openSelect(p, function (success, data, closeFn) {
                    if (success) {
                        closeFn();
                        if (data && data[0]) {
                            var tmp = data[0];
                            if (setVal) {
                                if ($.isFunction(setVal)) {
                                    setVal(tmp);
                                } else {
                                    $.each(setVal, function (k, v) {
                                        $(v).val(tmp[k]);
                                    });
                                }

                            }
                        }
                    }
                });
            };

            return {
                notifySelected: notifySelected,
                getDefaultData: getDefaultData,
                openSelect: openSelect,
                openSelectOne: openSelectOne
            };
        })();
        me.department = (function () {
            var openSelect = function (type, param, dataCallback) {
                var parentInclude = false;
                if (param.parentInclude) {
                    parentInclude = param.parentInclude;
                }
                var depParam = {
                    width: 400,
                    height: 450,
                    title: "选择单位",
                    url: "main/department/departmentSelect.html?type=" + type + '&parentInclude=' + parentInclude
                };
                $.extend(depParam, param);
                me.popup.openSelect(depParam, dataCallback);
            };
            var openSelectRadio = function (dataCallback, param) {
                return openSelect('radio', param, dataCallback);
            };
            var openSelectCheckbox = function (dataCallback, param) {
                return openSelect('checkbox', param, dataCallback);
            };
            return {
                notifySelected: me.popup.notifySelected,
                openSelectRadio: openSelectRadio,
                openSelectCheckbox: openSelectCheckbox,
                getDefaultData: me.popup.getDefaultData
            };
        })();


    });
    exports('hyUtil', {});
});

