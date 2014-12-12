<% var target, models; %>
<html>
<head>
    <title>Webit!!!!!</title>
    <meta http-equiv="Content-Type" content="text/html; charset=${target}"/>
    <style type="text/css">
        body { font-size: 10pt; color: #333333; }
        thead { font-weight: bold; background-color: #C8FBAF; }
        td { font-size: 10pt; text-align: center; }
        .odd { background-color: #F3DEFB; }
        .even { background-color: #EFFFF8; }
    </style>
</head>
<body>
    <h1>Template Engine Benchmark - Webit!!!!!</h1>
    <table>
        <thead>
            <tr>
                <th>序号</th>
                <th>编码</th>
                <th>名称</th>
                <th>日期</th>
                <th>值</th>
            </tr>
        </thead>
        <tbody>
            <%for(model : models){ %>
            <tr class="${for.iter.isOdd ? "odd" : "even"}">
                <td>${for.iter.index}</td>
                <td>${model.code}</td>
                <td>${model.name}</td>
                <td>${model.date}</td>
                <%if(model.value > 105.5) {%>
                <td style="color: red;">${model.value}%</td>
                <%} else {%>
                <td style="color: blue;">${model.value}%</td>
                <%}%>
            </tr>
            <%}%>
        </tbody>
    </table>
</body>
</html>