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
</head>
<body>
<h1>Welcome to sham system !</h1>
<script type="text/javascript" src="static/layui/layui.js"></script>
<script type="text/javascript" src="static/js/layui.config.js"></script>
<script type="text/javascript">
    layui.use(['jquery', 'tips'], function () {

    })
</script>
</body>
</html>
