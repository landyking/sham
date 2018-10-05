layui.define(['hyUtil', 'form', 'layer', 'element', 'laydate'], function (exports) {
    var $ = layui.$,
        form = layui.form,
        element = layui.element,
        layer = layui.layer,
        hint = layui.hint();

    form.verify({
        pass: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
    });
    var hyForm = {};
    hyForm.remoteSelectUrl = "";
    hyForm.loadRemoteSelectResult = null;
    hyForm.renderCalendar = function () {
        $(".hy-calendar").each(function () {
            var one = this;
            var me = $(one);
            var ld = me.attr("lay-data");
            try {
                var config = $.extend(new Function('return ' + ld)(), {elem: one});
                layui.laydate.render(
                    config
                );
            } catch (e) {
                hint.error('hy_calendar property lay-data configuration item has a syntax error: ' + ld)
            }
        });
    };
    hyForm.loadRemoteSelect = function () {
        var arr = [];
        $("select.hy-select").each(function () {
            var me = $(this);
            var ld = me.attr("lay-data");
            if (ld) {
                try {
                    var config = $.extend({
                        labelField: 'label',
                        valueField: 'value',
                        firstEmpty: false,
                        firstEmptyLabel: '---'
                    }, new Function('return ' + ld)());
                    var promise = $.get(hyForm.remoteSelectUrl + config.url, function (rst) {
                        if (rst.code === 0) {
                            me.empty();
                            if (config.firstEmpty) {
                                me.append('<option value="">' + config.firstEmptyLabel + '</option>');
                            }
                            $.each(rst.data, function (idx, one) {
                                var label = one[config.labelField];
                                var value = one[config.valueField];
                                me.append('<option value="' + value + '">' + label + '</option>');
                            });
                        } else {
                            hy.msg("加载下拉数据失败:" + rst.msg);
                        }
                    });
                    arr.push(promise);
                } catch (e) {
                    hint.error('row menu property lay-data configuration item has a syntax error: ' + ld)
                }
            }
        });
        var apply = $.when.apply(null, arr);
        hyForm.loadRemoteSelectResult = apply;
        return apply;
    };
    hyForm.loadFormData = function (config) {
        return $.get(config.loadUrl, function (rst) {
            if (rst.code == 0) {
                form.val(config.layFilter, rst.data);
                if (config.afterLoad) {
                    config.afterLoad(config.layFilter, rst.data);
                }
            } else {
                hy.msg("加载表单数据失败:" + rst.msg);
            }
        });
    };
    hyForm.render = function (cfg) {
        var config = {
            layFilter: 'dataForm',
            loadUrl: '',
            saveUrl: '',
            beforeSave: null,
            afterLoad: null
        };
        config = $.extend(config, cfg);
        hyForm.loadRemoteSelectResult.done(function () {
            if ($.isEmptyObject(config.loadUrl)) {
                if (config.afterLoad) {
                    config.afterLoad(config.layFilter, null);
                }
            } else {
                hyForm.loadFormData(config);
            }
        });
        form.on('submit(save)', function (data) {
            var bf = null;
            if (config.beforeSave) {
                bf = config.beforeSave(data, config.layFilter);
            }
            if (bf != false) {
                $.post(config.saveUrl, data.field, function (rst) {
                    if (rst.code == 0) {
                        hy.setNeedRefresh();
                        hy.closeCurrentWindow();
                    } else {
                        hy.msg("保存数据失败:" + rst.msg);
                    }
                });
            }
            return false;
        });
    };
    hyForm.getFormVals = function (formElem) {
        var fieldElem = formElem.find('input,select,textarea')
        var nameIndex = {}; //数组 name 索引
        var field = {};
        layui.each(fieldElem, function (_, item) {
            item.name = (item.name || '').replace(/^\s*|\s*&/, '');

            if (!item.name) return;
            //用于支持数组 name
            if (/^.*\[\]$/.test(item.name)) {
                var key = item.name.match(/^(.*)\[\]$/g)[0];
                nameIndex[key] = nameIndex[key] | 0;
                item.name = item.name.replace(/^(.*)\[\]$/, '$1[' + (nameIndex[key]++) + ']');
            }

            if (/^checkbox|radio$/.test(item.type) && !item.checked) return;
            field[item.name] = item.value;
        });
        return field;
    };

    hyForm.loadRemoteSelect().done(function () {
        form.render('select');
    });
    hyForm.renderCalendar();

    exports('hyForm', hyForm);
});