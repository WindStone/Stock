<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: yuanren.syr
  Date: 2016/2/22
  Time: 1:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body style="position:relative; margin:0 auto; width:1024px">
<div>
    <h1>Home</h1>
    <h3>总结笔记</h3>
    <ul>
        <c:forEach var="wikiIndexView" items="${wikiIndexViews}">
            <li><a href="${wikiIndexView.link}">${wikiIndexView.plainText}</a></li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
