<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: yuanren.syr
  Date: 2016/2/22
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<h1>${title}</h1>
<h3>本周交易记录</h3>
<c:forEach var="operator" items="${tradeRecordMap}">
    <table>
        <tr>
            <th rowspan="6">${operator.key}</th>
        </tr>
        <tr>
            <th>股票</th>
            <th>流向</th>
            <th>日期</th>
            <th>时间</th>
            <th>价格</th>
            <th>思路及总结</th>
        </tr>
        <c:forEach var="tradeRecordView" items="${operator.value}">
            <c:forEach var="detailView" items="${tradeRecordView.tradeDetailRecordViews}" varStatus="status">
                <tr>
                    <c:if test="${status.first}">
                        <td colspan="${fn:length(tradeRecordView)}">
                            <b>
                                    ${tradeRecordView.stockName}(${tradeRecordView.stockCode})
                            </b>
                        </td>
                    </c:if>
                    <td>${detailView.tradeFlow}</td>
                    <td>${detailView.tradeDate}</td>
                    <td>${detailView.tradeTime}</td>
                    <td>${detailView.price}</td>
                    <td>${detailView.conclusion}</td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</c:forEach>
</body>
</html>
