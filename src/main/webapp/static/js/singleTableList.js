layui.define(['hyUtil', 'table', 'layer', 'element', 'form', 'hyForm', 'common'], function (exports) {
    var $ = layui.$,
        element = layui.element,
        layer = layui.layer,
        hint = layui.hint(),
        form = layui.form,
        hyForm = layui.hyForm,
        table = layui.table;

    var singleTableList = {};
    singleTableList.render = function (cfg) {
        var myCfg = {
            searchExecLayFilter: 'searchExec',
            searchResetLayFilter: 'searchReset',
            layFilter: 'dataTable',
            tableConfig: {
                elem: '#dataTable',
                height: null,
                page: true,
                toolbar: '#tableToolbar',
                defaultToolbar: ['filter', 'print'],
                done: function () {
                    layui.common.render();
                }
            },
            rowMenuListener: {},
            toolbarListener: {}
        };
        $.extend(true, myCfg, cfg);

        if (myCfg.searchExecLayFilter) {
            // console.log("##############");
            var searchForm = $("button[lay-filter=" + myCfg.searchExecLayFilter + "]").parents('.layui-form');
            if (searchForm) {
                var formVals = hyForm.getFormVals(searchForm);
                if (myCfg.tableConfig.where == null) {
                    myCfg.tableConfig.where = {};
                }
                myCfg.tableConfig.where = $.extend(myCfg.tableConfig.where, formVals);
            }
        }
        var calcTableHeight = function () {
            var fullHeight = $(window).innerHeight();
            var searchHeight = 0;
            if (myCfg.searchExecLayFilter) {
                searchHeight = $("button[lay-filter=" + myCfg.searchExecLayFilter + "]").parents('.layui-card-header').outerHeight();
            }
            var calcHeight = fullHeight - searchHeight - 85;
            return calcHeight;
        };
        if ($.isEmptyObject(myCfg.tableConfig.height)) {
            myCfg.tableConfig.height = calcTableHeight();
        }
        //渲染数据表格
        var stable = table.render(myCfg.tableConfig);
        $(window).resize(function () {
            console.log(arguments);
            // layui.table.reload('dataTable', {height: calcTableHeight()});
        });
        var processDialog = function (btn, arr) {
            var layData = btn.attr("lay-data");
            if (layData) {
                try {
                    var config = {
                        title: '未知的标题',
                        url: '',
                        width: 500,
                        height: 400,
                        params: {}
                    };
                    config = $.extend(config, new Function('return ' + layData)(arr[0]));
                    var txt = "";
                    if (!$.isEmptyObject(config.params)) {
                        $.each(config.params, function (k, v) {
                            txt += '&';
                            if (v == '?') {
                                txt += k + "=" + arr[0][k];
                            } else {
                                txt += k + "=" + v;
                            }
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
                    hy.layerOpenUrl(config.title, config.url + txt, config.width, config.height, function () {
                        if (hy.getAndRemoveNeedRefresh()) {
                            stable.reload();
                        }
                    });
                } catch (e) {
                    hint.error('row menu property lay-data configuration item has a syntax error: ' + data)
                }
            }
        };
        var processAjax = function (btn, arr) {
            var layData = btn.attr("lay-data");
            if (layData) {
                try {
                    var config = {
                        title: '确定执行该操作？',
                        url: '',
                        params: {}
                    };
                    config = $.extend(config, new Function('return ' + layData)(arr[0]));
                    if (config.title) {
                        layer.confirm(config.title, function (idx) {

                            var ps = {};
                            if (!$.isEmptyObject(config.params)) {
                                $.each(config.params, function (k, v) {
                                    if (v == '?') {
                                        ps[k] = arr[0][k];
                                    } else {
                                        ps[k] = v;
                                    }
                                });
                            }
                            $.post(config.url, ps, function (rst) {
                                if (rst.code == 0) {
                                    //nothing
                                } else {
                                    hy.msg("执行操作失败:" + rst.msg);
                                }
                            }).complete(function () {
                                stable.reload();
                            });
                            layer.close(idx);
                        })
                    }
                } catch (e) {
                    hint.error('row menu property lay-data configuration item has a syntax error: ' + layData)
                }
            }
        };
        var commonProcess = function (evt, btn, arr) {
            if ('dialog' == evt) {
                processDialog(btn, arr);
            } else if ('ajax' == evt) {
                processAjax(btn, arr);
            } else {
                return false;
            }
            return true;
        };
        //注册表头工具栏监听器
        if (myCfg.toolbarListener) {
            //监听头工具栏事件
            table.on('toolbar(' + myCfg.layFilter + ')', function (obj) {
                var checkStatus = table.checkStatus(obj.config.id)
                    , data = checkStatus.data; //获取选中的数据
                var btn = $(this);

                if (!commonProcess(obj.event, btn, data)) {
                    var listener = myCfg.toolbarListener[obj.event];
                    if (listener) {
                        listener(checkStatus, data, obj, stable);
                    }
                }
            });
        }
        //注册表每一行操作按钮监听器
        if (myCfg.rowMenuListener) {
            //监听行工具事件
            table.on('tool(' + myCfg.layFilter + ')', function (obj) {
                var data = obj.data //获得当前行数据
                    , layEvent = obj.event; //获得 lay-event 对应的值
                var btn = $(this);
                if (!commonProcess(layEvent, btn, [data])) {
                    var rowMenuListener = myCfg.rowMenuListener[layEvent];
                    if (rowMenuListener) {
                        rowMenuListener(data, obj, stable);
                    }
                }
            });
        }

        //搜索区域渲染
        if (myCfg.searchResetLayFilter) {
            $("[lay-filter=" + myCfg.searchResetLayFilter + "]").click(function () {
                var tmpFm = $(this).parents(".layui-form");
                if (tmpFm[0]) {
                    tmpFm[0].reset();
                    stable.reload({
                        page: {
                            curr: 1 //重新从第 1 页开始
                        },
                        where: hyForm.getFormVals(tmpFm)
                    });
                }
                return false;
            });
        }
        if (myCfg.searchExecLayFilter) {
            form.on('submit(' + myCfg.searchExecLayFilter + ')', function (data) {
                stable.reload({
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    where: data.field
                });
                return false;
            });
        }


        return stable;
    };

    exports('singleTableList', singleTableList);
});