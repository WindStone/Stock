<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: yuanren.syr
  Date: 2016/3/13
  Time: 14:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>相关性</title>
</head>
<body>
<table border="1">
    <tr>
        <th>股票代码</th>
        <th>相关性</th>
        <th width="512px">序列值</th>
        <th width="512px">序列值2</th>
    </tr>

    <c:forEach var="correlation" items="${correlations}">
        <tr>
            <td>${correlation.stockNameB}(${correlation.stockCodeB})</td>
            <td>${correlation.correlation}</td>
            <td>${correlation.posibility}</td>
            <td><c:forEach var="value" items="${correlation.codeAValues}">${value}&nbsp;</c:forEach></td>
            <td><c:forEach var="value" items="${correlation.codeBValues}">${value}&nbsp;</c:forEach></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
