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
    <div class="layui-card-header layuiadmin-card-header-auto">
        <form class="layui-form" id="filterForm">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">数据源</label>
                    <div class="layui-input-block">
                        <select name="dataSource" lay-filter="dataSource" class="hy-select"
                                lay-data="{url:'api/listDataSource',firstEmpty:true}">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">CATALOG</label>
                    <div class="layui-input-block">
                        <select name="catalog">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">SCHEMA</label>
                    <div class="layui-input-block">
                        <select name="schema">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">TYPE</label>
                    <div class="layui-input-block">
                        <select name="type">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <a class="layui-btn" lay-submit="" lay-filter="searchExec">
                        <i class="layui-icon layui-icon-search"></i>
                    </a>
                    <a class="layui-btn layui-btn-normal" lay-filter="searchReset">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>
                </div>
            </div>
        </form>
    </div>
    <div class="layui-card-body">
        <table id="dataTable" lay-filter="dataTable"></table>
        <script type="text/html" id="tableToolbar">
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="export">export</button>
            </div>
        </script>
        <script type="text/html" id="tableRowMenu">
            <a class="layui-btn layui-btn-xs" lay-event="dialog"
               lay-data="{url:'_admin/trafficIllegal/audit.jsp',width:650,height:700,title:'test',params:{id:'?'}}">test</a>
        </script>
    </div>
</div>
</body>
<script type="text/javascript" src="static/layui/layui.js"></script>
<script type="text/javascript" src="static/js/layui.config.js"></script>
<script type="text/javascript">
    layui.use(['jquery', 'tips', 'singleTableList', 'common', 'form'], function () {
        var layer = layui.layer;
        var hyForm = layui.hyForm;
        var form = layui.form;
        var $ = layui.$;
        layui.singleTableList.render({
            layFilter: 'dataTable',
            tableConfig: {
                url: 'api/listTable',
                cols: [[
                    {type: 'numbers', fixed: true},
                    {field: 'catalog', title: 'CATALOG', width: 120},
                    {field: 'schema', title: 'SCHEMA', width: 120},
                    {field: 'type', title: '类型', width: 100},
                    {field: 'name', title: '名称', width: 200},
                    {field: 'remarks', title: '描述'},
                    {fixed: 'right', title: '操作', toolbar: '#tableRowMenu', width: 210}
                ]]
            },
            toolbarListener: {},
            rowMenuListener: {}
        });
        form.on('select(dataSource)', function (data) {
            var p1 = hyForm.renderRemoteSelect($("select[name=catalog]"), {
                url: 'api/listCatalog?dataSource=' + data.value,
                firstEmpty: false
            });
            var p2 = hyForm.renderRemoteSelect($("select[name=schema]"), {
                url: 'api/listSchema?dataSource=' + data.value,
                firstEmpty: false
            });
            var p3 = hyForm.renderRemoteSelect($("select[name=type]"), {
                url: 'api/listTableType?dataSource=' + data.value,
                firstEmpty: true
            });
            $.when(p1, p2, p3).done(function () {
                form.render('select');
            });
        });
    })
</script>
</html>
