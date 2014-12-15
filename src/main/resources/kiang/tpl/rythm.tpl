@import kiang.teb.TebModel
@args String target
@args java.util.List<TebModel> models
<html>
<head>
    <title>Rythm!!!!!</title>
    <meta http-equiv="Content-Type" content="text/html; charset=@raw(){@target}"/>
    <style type="text/css">
        body { font-size: 10pt; color: #333333; }
        thead { font-weight: bold; background-color: #C8FBAF; }
        td { font-size: 10pt; text-align: center; }
        .odd { background-color: #F3DEFB; }
        .even { background-color: #EFFFF8; }
    </style>
</head>
<body>
    <h1>Template Engine Benchmark - Rythm!!!!!</h1>
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
        @raw() {
            @for(model: models) {
                @{
                    String clazz = model_index % 2 ==0 ? "odd" : "even";
                }
            <tr class="@clazz">
                <td>@model_index</td>
                <td>@model.getCode()</td>
                <td>@model.getName()</td>
                <td>@model.getDate()</td>
                @if(model.getValue() > 105.5) {
                <td style="color: red;">@model.getValue()%</td>
                } else {
                <td style="color: blue;">@model.getValue()%</td>
                }
            </tr>
            }
        }
        </tbody>
    </table>
</body>
</html>