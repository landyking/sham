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
        <form class="layui-form" id="filterForm">
            <div class="layui-form-item">
                <div class="layui-inline">
                    <label class="layui-form-label">数据源</label>
                    <div class="layui-input-block" style="width:100px;">
                        <select name="dataSource" lay-filter="dataSource" class="hy-select"
                                lay-data="{url:'api/listDataSource',firstEmpty:true}">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">CATALOG</label>
                    <div class="layui-input-block" style="width:100px;">
                        <select name="catalog">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">SCHEMA</label>
                    <div class="layui-input-block" style="width: 120px;">
                        <select name="schema">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">TYPE</label>
                    <div class="layui-input-block" style="width: 120px;">
                        <select name="type">
                        </select>
                    </div>
                </div>
                <div class="layui-inline">
                    <button class="layui-btn" lay-submit lay-filter="searchExec">
                        <i class="layui-icon layui-icon-search"></i>
                    </button>
                    <%--<button class="layui-btn layui-btn-normal" lay-submit lay-filter="searchReset">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </button>--%>
                </div>
            </div>
        </form>
    </div>
    <div class="layui-card-body">
        <table id="dataTable" lay-filter="dataTable"></table>
        <script type="text/html" id="tableToolbar">

        </script>
        <script type="text/html" id="tableRowMenu">
            <a class="layui-btn layui-btn-xs" lay-event="dialog"
               lay-data="{url:'views/listTableColumn.jsp',width:800,height:600,title:'表结构',params:{dataSource:'?',name:'?',catalog:'?',schema:'?'}}">表结构</a>
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="dialog"
               lay-data="{url:'views/codeGen.jsp',width:800,height:600,title:'代码生成',params:{dataSource:'?',name:'?',catalog:'?',schema:'?'}}">代码生成</a>
        </script>
    </div>
</div>
</body>
<script src="static/layui/layui.js"></script>
<script src="static/js/layui.config.js"></script>
<script>
    layui.use(['singleTableList', 'jquery', 'hyForm', 'form'], function () {
        var hyForm = layui.hyForm;
        var form = layui.form;
        var $ = layui.$;
        layui.singleTableList.render({
            layFilter: 'dataTable',
            tableConfig: {
                url: 'api/listTable',
                limit: 1000,
                limits: [1000],
                cols: [[
                    {type: 'numbers', fixed: true},
                    {field: 'catalog', title: 'CATALOG', width: 120},
                    {field: 'schema', title: 'SCHEMA', width: 120},
                    {field: 'type', title: '类型', width: 120},
                    {field: 'name', title: '名称', width: 200},
                    {field: 'remarks', title: '描述'},
                    {fixed: 'right', title: '操作', toolbar: '#tableRowMenu', width: 150}
                ]]
            },
            toolbarListener: {},
            rowMenuListener: {}
        });
        form.on('select(dataSource)', function (data) {
            if (!$.isEmptyObject(data.value)) {
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
            }
        });
    })
</script>
</html>
