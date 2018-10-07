<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <base href="<%=request.getContextPath()+"/"%>"/>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="static/layui/css/layui.css">
    <link rel="stylesheet" href="static/css/common.css">
</head>
<body>
<div class="layui-card">
    <div class="layui-card-header sham-card-header">
        <div class="layui-form">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">模板</label>
                    <div class="layui-input-block" style="width:160px;">
                        <select name="template" lay-filter="dataSource" class="hy-select"
                                lay-data="{url:'api/listTemplate'}">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">移除前缀</label>
                    <div class="layui-input-block" style="width:80px;">
                        <input type="number" name="prefixLength" min="0" autocomplete="off" class="layui-input"
                               value="0">
                    </div>
                </div>
                <div class="layui-inline">
                    <button class="layui-btn layui-btn-normal" lay-submit lay-filter="codeGenBtn">
                        <i class="layui-icon layui-icon-star"></i>生成
                    </button>
                    <button class="layui-btn layui-btn-normal copyCode">
                        <i class="layui-icon layui-icon-code-circle"></i>复制
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-card-body">
        <blockquote class="layui-elem-quote">
            <span id="tableName"></span>
            <span id="fileName"></span>
        </blockquote>
        <pre class="layui-code" id="showCode">
        </pre>
    </div>
</div>
</body>
<script src="static/layui/layui.js"></script>
<script src="static/js/layui.config.js"></script>
<script src="static/js/clipboard.min.js"></script>
<script>
    layui.use(['singleTableList', 'jquery', 'hyForm', 'form', 'hyUtil', 'tips'], function () {
        var layer = layui.layer;
        var hyForm = layui.hyForm;
        var form = layui.form;
        var $ = layui.$;
        $("#tableName").html("表名: " + hy.getURLParam("name"));
        form.on("submit(codeGenBtn)", function (data) {
            var ps = $.extend({
                catalog: hy.getURLParam("catalog"),
                schema: hy.getURLParam("schema"),
                dataSource: hy.getURLParam("dataSource"),
                table: hy.getURLParam("name")
            }, data.field);
            $.post("api/codeGen", ps, function (rst) {
                if (rst.code == 0) {
                    var code = $("#showCode");
                    code.empty();
                    code.text(rst.data);
                    $("#fileName").html(" 文件名: " + rst.fileName);
                } else {
                    layer.msg("生成失败" + rst.msg);
                }
            });
            return false;
        });
        new ClipboardJS('.copyCode', {
            text: function (trigger) {
                return $("#showCode").text();
            }
        }).on('success', function () {
            layui.tips.success('复制成功');
        });
    })
</script>
</html>
