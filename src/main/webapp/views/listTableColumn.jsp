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
        <form class="layui-form">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">模板</label>
                    <div class="layui-input-block" style="width:100px;">
                        <select name="template" lay-filter="dataSource" class="hy-select"
                                lay-data="{url:'api/listDataSource',firstEmpty:true}">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <button class="layui-btn layui-btn-normal" lay-submit lay-filter="searchExec">
                        <i class="layui-icon layui-icon-star"></i>生成代码
                    </button>
                </div>
            </div>
        </form>
    </div>
    <div class="layui-card-body">
        <table id="dataTable" lay-filter="dataTable"></table>
    </div>
</div>
</body>
<script src="static/layui/layui.js"></script>
<script src="static/js/layui.config.js"></script>
<script>
    layui.use(['singleTableList', 'jquery', 'hyForm', 'form', 'hyUtil'], function () {
        var layer = layui.layer;
        var hyForm = layui.hyForm;
        var form = layui.form;
        var $ = layui.$;
        layui.singleTableList.render({
            layFilter: 'dataTable',
            tableConfig: {
                url: 'api/listTableColumn',
                where: {
                    catalog: hy.getURLParam("catalog"),
                    schema: hy.getURLParam("schema"),
                    dataSource: hy.getURLParam("dataSource"),
                    table: hy.getURLParam("name")
                },
                toolbar: false,
                limit: 1000,
                limits: [1000],
                cols: [[
                    {type: 'numbers', fixed: true},
                    {field: 'remarks', title: '描述',minWidth:100},
                    {field: 'name', title: '列名', width: 120},
                    {field: 'dataType', title: 'SQL类型', width: 90},
                    {field: 'typeName', title: '数据库类型', width: 120},
                    {field: 'columnSize', title: '大小', width: 90},
                    {field: 'decimalDigits', title: '精度', width: 90},
                    {field: 'isNullable', title: '可空', width: 70},
                    {field: 'columnDef', title: '默认', width: 100},
                    {field: 'isAutoIncrement', title: '自增', width: 70}
                ]]
            },
            toolbarListener: {},
            rowMenuListener: {}
        });
    })
</script>
</html>
