<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <base href="<%=request.getContextPath()+"/"%>"/>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Home</title>
    <link rel="stylesheet" href="static/layui/css/layui.css">
    <link rel="stylesheet" href="static/css/common.css">
</head>
<body>
<div class="sham-card-container">
    <div class="layui-card">
        <div class="layui-card-header sham-card-header">
            <form class="layui-form" id="filterForm">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">数据源</label>
                        <div class="layui-input-block">
                            <select name="illegalType" class="hy-select"
                                    lay-data="{url:'admin/dict/listSelectItem?dictCode=trafficIllegal',firstEmpty:true,firstEmptyLabel:'全部'}">
                                <option value="">全部</option>
                                <option value="1">机动车违反禁止标线</option>
                                <option value="2">机动车逆向行驶</option>
                                <option value="3">机动车不在机动车道内行驶</option>
                                <option value="4">机动车违反道路交通信号通行</option>
                                <option value="5">机动车违法停车</option>
                                <option value="6">机动车车窗抛物</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">CATALOG</label>
                        <div class="layui-input-block">
                            <select name="illegalType" class="hy-select"
                                    lay-data="{url:'admin/dict/listSelectItem?dictCode=trafficIllegal',firstEmpty:true,firstEmptyLabel:'全部'}">
                                <option value="">全部</option>
                                <option value="1">机动车违反禁止标线</option>
                                <option value="2">机动车逆向行驶</option>
                                <option value="3">机动车不在机动车道内行驶</option>
                                <option value="4">机动车违反道路交通信号通行</option>
                                <option value="5">机动车违法停车</option>
                                <option value="6">机动车车窗抛物</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">SCHEMA</label>
                        <div class="layui-input-block">
                            <select name="illegalType" class="hy-select"
                                    lay-data="{url:'admin/dict/listSelectItem?dictCode=trafficIllegal',firstEmpty:true,firstEmptyLabel:'全部'}">
                                <option value="">全部</option>
                                <option value="1">机动车违反禁止标线</option>
                                <option value="2">机动车逆向行驶</option>
                                <option value="3">机动车不在机动车道内行驶</option>
                                <option value="4">机动车违反道路交通信号通行</option>
                                <option value="5">机动车违法停车</option>
                                <option value="6">机动车车窗抛物</option>
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
                    <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="export">导出选中违法信息</button>
                </div>
            </script>
            <script type="text/html" id="tableRowMenu">
                <a class="layui-btn layui-btn-xs" lay-event="dialog"
                   lay-data="{url:'_admin/trafficIllegal/audit.jsp',width:650,height:700,title:'交通违法举报信息审核',params:{id:'?'}}">审核</a>
            </script>
        </div>
    </div>
</div>
<script type="text/javascript" src="static/layui/layui.js"></script>
<script type="text/javascript" src="static/js/layui.config.js"></script>
<script type="text/javascript">
    layui.use(['jquery', 'tips', 'singleTableList'], function () {
        var layer = layui.layer;
        var hyForm = layui.hyForm;
        var $ = layui.$;
        layui.singleTableList.render({
            layFilter: 'dataTable',
            tableConfig: {
                url: 'admin/award/awardList',
                cols: [[
                    {type: 'numbers', fixed: true},
                    {checkbox: true, fixed: true},
                    {field: 'id', title: 'id', hide: true},
                    {field: 'typeDesc', title: '举报分类', width: 120},
                    {field: 'subType', title: '举报子分类'},
                    {field: 'money', title: '奖励金额', width: 90},
                    {field: 'grantFlag', title: '是否发放', width: 90},
                    {field: 'grantTime', title: '奖金发放时间', hide: true},
                    {field: 'auditTime', title: '审核通过时间', width: 160},
                    {field: 'informerName', title: '举报人姓名', width: 100},
                    {field: 'informerPhone', title: '举报人电话', width: 130},
                    {field: 'openid', title: '举报人微信id', hide: true},
                    {field: 'nickName', title: '举报人微信昵称', width: 130},
                    {field: 'createTime', title: '举报时间', width: 160},
                    {field: 'mmoney', title: '举报月已奖励金额', width: 150},
                    {field: 'tmoney', title: '已奖励总金额', width: 120},
                    {fixed: 'right', title: '操作', toolbar: '#tableRowMenu', width: 210}
                ]]
            },
            toolbarListener: {
                'batchGrant': function (checkStatus, data, obj, stable) {
                    if ($.isEmptyObject(data)) {
                        layer.msg("未选中任何内容");
                        return;
                    }
                    layer.confirm("确认将选中项全部标记为已发放？", function (idx) {
                        var ids = "";
                        $.each(data, function (idx, one) {
                            ids += one.id + ",";
                        });
                        $.post("admin/award/setAwardGrant", {
                            id: ids
                        }).complete(function () {
                            layer.close(idx);
                            stable.reload();
                        });
                    })
                },
                'export': function (checkStatus, data, obj) {
                    layer.confirm("确定导出当前筛选的全部数据？", function (idx) {
                        var vals = hyForm.getFormVals($("#filterForm"));
                        var query = "";
                        $.each(vals, function (k, v) {
                            query += k + "=" + v + "&";
                        });
                        window.open("admin/award/exportAwardList?" + query);
                        layer.close(idx);
                    });
                }
            },
            rowMenuListener: {
                'changeMoney': function (row, obj, stable) {
                    layer.prompt({
                        formType: 0,
                        title: '请输入修改后的金额'
                    }, function (value, index, elem) {
                        $.post("admin/award/modifyAwardMoney", {
                            id: row.id,
                            money: value
                        }, function (rst) {
                            if (rst.code == 0) {
                                layer.close(index);
                                stable.reload();
                            } else {
                                layer.msg("操作失败:" + rst.msg);
                            }
                        });
                    });
                }
            }
        });
    })
</script>
</body>
</html>
