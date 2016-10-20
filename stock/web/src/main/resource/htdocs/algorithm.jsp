<%--
  Created by IntelliJ IDEA.
  User: yuanren.syr
  Date: 2016/1/31
  Time: 22:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script>
        window.jQuery || document.write('<script src="uisvr/js/jquery-2.1.1.min.js"><\/script>')
    </script>
    <script src="uisvr/js/jquery-ui-1.11.4/jquery-ui.min.js" type="text/javascript"></script>
    <style>
        .verticalLine {
            position: absolute;
            width: 2px;
            height: 380px;
            z-index: 100;
            background: #000;
            float: left;
        }

        .dateSelecterContainer {
            position: relative;
            float: left;
            width: 370px;
            height: 480px;
            border-top: 1px #a8a8a8 solid;
        }

        .disappear {
            display: none;
        }

        .dateSelecter {
            padding: 15px 35px;
            cursor: default;

            text-transform: uppercase;
            font-family: Tahoma;
            font-size: 12px;

            background: #141517;
            border-bottom: 1px #a8a8a8 solid;

            -webkit-border-radius: 0px;
            -moz-border-radius: 0px;
            border-radius: 0px;

            -webkit-box-shadow: 0px 1px 1px rgba(255, 255, 255, .1), inset 0px 1px 1px rgb(0, 0, 0);
            -moz-box-shadow: 0px 1px 1px rgba(255, 255, 255, .1), inset 0px 1px 1px rgb(0, 0, 0);
            box-shadow: 0px 1px 1px rgba(255, 255, 255, .1), inset 0px 1px 1px rgb(0, 0, 0);
        }

        .button {
            font-family: Tahoma 微软雅黑;
            font-size: 12px;
            color: #a8a8a8;
            background: #141517;
            text-align: center;
            height: 32px;
            line-height: 32px;
            vertical-align: middle;
            float: left;
            display: inline;
            margin: 0;
            padding: 0;
            transition: color 0.5s;
            -webkit-transition: color 0.5s;
            cursor: hand;
        }

        .button:hover {
            color: #ffffff;
        }

        .buttonLeft {
            border-right: 1px #a8a8a8 solid;
            width: 184px;
        }

        .buttonRight {
            width: 185px;
        }

        .ui-datepicker,
        .ui-datepicker table,
        .ui-datepicker tr,
        .ui-datepicker td,
        .ui-datepicker th {
            margin: 0;
            padding: 0;
            border: none;
            border-spacing: 0;
        }

        .ui-datepicker-header {
            position: relative;
            padding-bottom: 10px;
            border-bottom: 1px solid #d6d6d6;
        }

        .ui-datepicker-title {
            text-align: center;
        }

        .ui-datepicker-month {
            position: relative;
            padding-right: 15px;
            color: #565656;
        }

        .ui-datepicker-year {
            padding-left: 8px;
            color: #a8a8a8;
        }

        .ui-datepicker-month:before {
            display: block;
            position: absolute;
            top: 5px;
            right: 0;
            width: 5px;
            height: 5px;
            content: '';

            background: #a5cd4e;
            background: -moz-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #a5cd4e), color-stop(100%, #6b8f1a));
            background: -webkit-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: -o-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: -ms-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);

            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
        }

        .ui-datepicker-prev,
        .ui-datepicker-next {
            position: absolute;
            top: -2px;
            padding: 5px;
            cursor: pointer;
        }

        .ui-datepicker-prev {
            left: 5px;
            padding-left: 0;
        }

        .ui-datepicker-next {
            right: 5px;
            padding-right: 0;
        }

        .ui-datepicker-prev span,
        .ui-datepicker-next span {
            display: block;
            width: 5px;
            height: 10px;
            text-indent: -9999px;

            background-image: url(/image/arrows.png);
        }

        .ui-datepicker-prev span {
            background-position: 0px 0px;
        }

        .ui-datepicker-next span {
            background-position: -5px 0px;
        }

        .ui-datepicker-prev-hover span {
            background-position: 0px -10px;
        }

        .ui-datepicker-next-hover span {
            background-position: -5px -10px;
        }

        .ui-datepicker-calendar th {
            padding-top: 15px;
            padding-bottom: 10px;

            text-align: center;
            font-weight: normal;
            color: #a8a8a8;
            font-size: 12px;
        }

        .ui-datepicker-calendar td {
            padding: 0 7px;

            text-align: center;
            line-height: 26px;
            font-size: 12px;
        }

        .ui-datepicker-calendar .ui-state-default {
            display: block;
            width: 26px;
            outline: none;

            text-decoration: none;
            color: #a8a8a8;

            border: 1px solid transparent;
        }

        .ui-datepicker-calendar .ui-state-active {
            color: #6a9113;
            border: 1px solid #6a9113;
        }

        .ui-datepicker-other-month .ui-state-default {
            color: #565656;
        }

        .canvasDiv {
            position: relative;
            border: 1px black solid;
            width: 1024px;
            height: 500px;
            float: left;
        }
    </style>
</head>
<body style="position:relative; margin:0 auto; width:1400px">
<div>
    <div>
        <div class="canvasDiv" width="1024px">
            <canvas id="bollCanvas" width="1024" height="500"/>
        </div>
        <div id="bollCanvasDataContainer" class="dateSelecterContainer disappear">
            <div id="bollStartDateSelecter" class="dateSelecter dateSelecterStart"></div>
            <div id="bollEndDateSelecter" class="dateSelecter dateSelecterEnd"></div>
            <div>
                <div id="bollSubmit" class="button buttonLeft">Submit</div>
                <div id="bollReset" class="button buttonRight">Reset</div>
            </div>
        </div>
        <div id="bollCanvasLine" class="verticalLine"/>
    </div>
    <div>
        <div class="canvasDiv" width="1024px">
            <canvas id="bollDiffCanvas" width="1024" height="500"/>
        </div>
        <div id="bollDiffCanvasDataContainer" class="dateSelecterContainer disappear">
            <div id="bollDiffStartDateSelecter" class="dateSelecter dateSelecterStart"></div>
            <div id="bollDiffEndDateSelecter" class="dateSelecter dateSelecterEnd"></div>
            <div>
                <div id="bollDiffSubmit" class="button buttonLeft">Submit</div>
                <div id="bollDiffReset" class="button buttonRight">Reset</div>
            </div>
        </div>
        <div id="bollDiffCanvasLine" class="verticalLine"/>
    </div>
    <div>
        <div class="canvasDiv">
            <canvas id="macdCanvas" width="1024" height="500"/>
        </div>
        <div id="macdCanvasDataContainer" class="dateSelecterContainer disappear">
            <div id="macdStartDateSelecter" class="dateSelecter dateSelecterStart"></div>
            <div id="macdEndDateSelecter" class="dateSelecter dateSelecterEnd"></div>
            <div>
                <div id="macdSubmit" class="button buttonLeft">Submit</div>
                <div id="macdReset" class="button buttonRight">Reset</div>
            </div>
        </div>
        <div id="macdCanvasLine" class="verticalLine"/>
    </div>
    <div>
        <div class="canvasDiv">
            <canvas id="dmaCanvas" width="1024" height="500"/>
        </div>
        <div id="dmaCanvasDataContainer" class="dateSelecterContainer disappear">
            <div id="dmaStartDateSelecter" class="dateSelecter dateSelecterStart"></div>
            <div id="dmaEndDateSelecter" class="dateSelecter dateSelecterEnd"></div>
            <div>
                <div id="dmaSubmit" class="button buttonLeft">Submit</div>
                <div id="dmaReset" class="button buttonRight">Reset</div>
            </div>
        </div>
        <div id="dmaCanvasLine" class="verticalLine"/>
    </div>
</div>
<script>
    var boll = new canvas({
        algorithmName: "BOLL超过中轴百分比",
        canvas: document.getElementById("bollCanvas"),
        canvasLine: document.getElementById("bollCanvasLine"),
        dateContainer: $("#bollCanvasDataContainer"),
        startDateSelecter: $("#bollStartDateSelecter"),
        endDateSelecter: $("#bollEndDateSelecter"),
        submitButton: document.getElementById("bollSubmit"),
        resetButton: document.getElementById("bollReset"),
        startInitialDate: "${bollStartDate}",
        endInitialDate: "${endDate}",
        defaultStartDate: "${bollDefaultDate}"
    });

    var bollDiff = new canvas({
        algorithmName: "BOLL距上下轴3%股票数量差值百分比",
        canvas: document.getElementById("bollDiffCanvas"),
        canvasLine: document.getElementById("bollDiffCanvasLine"),
        dateContainer: $("#bollDiffCanvasDataContainer"),
        startDateSelecter: $("#bollDiffStartDateSelecter"),
        endDateSelecter: $("#bollDiffEndDateSelecter"),
        submitButton: document.getElementById("bollDiffSubmit"),
        resetButton: document.getElementById("bollDiffReset"),
        startInitialDate: "${bollDiffStartDate}",
        endInitialDate: "${endDate}",
        defaultStartDate: "${bollDiffDefaultDate}"
    });

    var macd = new canvas({
        algorithmName: "MACD中轴上方百分比",
        canvas: document.getElementById("macdCanvas"),
        canvasLine: document.getElementById("macdCanvasLine"),
        dateContainer: $("#macdCanvasDataContainer"),
        startDateSelecter: $("#macdStartDateSelecter"),
        endDateSelecter: $("#macdEndDateSelecter"),
        submitButton: document.getElementById("macdSubmit"),
        resetButton: document.getElementById("macdReset"),
        startInitialDate: "${macdStartDate}",
        endInitialDate: "${endDate}",
        defaultStartDate: "${macdDefaultDate}",
        avg: ${macdAvg}
    });

    var dma = new canvas({
        algorithmName: "DMA向上百分比",
        canvas: document.getElementById("dmaCanvas"),
        canvasLine: document.getElementById("dmaCanvasLine"),
        dateContainer: $("#dmaCanvasDataContainer"),
        startDateSelecter: $("#dmaStartDateSelecter"),
        endDateSelecter: $("#dmaEndDateSelecter"),
        submitButton: document.getElementById("dmaSubmit"),
        resetButton: document.getElementById("dmaReset"),
        startInitialDate: "${dmaStartDate}",
        endInitialDate: "${endDate}",
        defaultStartDate: "${dmaDefaultDate}"
    });

    function canvas(options) {
        var algorithmName = options.algorithmName;
        var canvas = options.canvas;
        var canvasLine = options.canvasLine;
        var dateContainer = options.dateContainer;
        var startDateSelecter = options.startDateSelecter;
        var endDateSelecter = options.endDateSelecter;
        var submitButton = options.submitButton;
        var resetButton = options.resetButton;
        var avg = options.avg;
        var startInitialDate = options.startInitialDate;
        var endInitialDate = options.endInitialDate;
        var startDate = startInitialDate;
        var endDate = endInitialDate;
        var isDrawShExp = true;
        var isDrawSzExp = true;
        var defaultStartDate = options.defaultStartDate;

        var leftPadding = 100, rightPadding = 100, topPadding = 45, bottomPadding = 75;
        var innerLeftPadding = 0, innerRightPadding = 0, innerTopPadding = 0, innerBottomPadding = 0;
        var labelX1 = canvas.width / 2 - 350, labelX2 = canvas.width / 2 - 100, labelX3 = canvas.width / 2 + 150;

        var resultArray;

        init();

        function init() {
            startDateSelecter.datepicker({
                inline: true,
                firstDay: 1,
                showOtherMonths: true,
                dateFormat: "yy/mm/dd",
                dayNamesMin: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                onSelect: function (dateText, inst) {
                    if (dateText != null && inst != null) {
                        startDate = dateText;
                    }
                }
            });
            endDateSelecter.datepicker({
                inline: true,
                firstDay: 1,
                showOtherMonths: true,
                dateFormat: "yy/mm/dd",
                dayNamesMin: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                onSelect: function (dateText, inst) {
                    if (dateText != null && inst != null) {
                        endDate = dateText;
                    }
                }
            });
            $.ajax({
                type: 'post',
                url: 'getAlgorithmResult.json',
                dataType: 'json',
                data: {algorithm: encodeURI(algorithmName), startDate: startDate, endDate: endDate},
                success: function (data) {
                    if (data.success) {
                        resultArray = JSON.parse(data.value);
                        drawGraph(resultArray, algorithmName, avg);

                        var e = new Object();
                        e.clientX = canvas.getBoundingClientRect.left + leftPadding;
                        e.clientY = canvas.getBoundingClientRect.top + topPadding;
                        e.toElement = canvas;
                        onMouseMove(e)
                        canvas.addEventListener("mousemove", onMouseMove);
                        startInitialDate = resultArray[0].date;
                        endInitialDate = resultArray[resultArray.length - 1].date;
                        startDateSelecter.datepicker("option", "minDate", startInitialDate);
                        startDateSelecter.datepicker("option", "maxDate", endInitialDate);
                        endDateSelecter.datepicker("option", "minDate", startInitialDate);
                        endDateSelecter.datepicker("option", "maxDate", endInitialDate);
                        startDate = defaultStartDate;
                        startDateSelecter.datepicker("setDate", startDate);
                        endDateSelecter.datepicker("setDate", endInitialDate);
                        endDate = endInitialDate;
                        redraw();
                    }
                }
            });
            canvas.addEventListener('click', function (event) {
                var bounding = canvas.getBoundingClientRect();
                var leftRedunt = document.body.getBoundingClientRect().left;
                var coordinateY = canvas.height - bottomPadding + 45;
                if (event.clientY >= coordinateY + bounding.top && event.clientY <= coordinateY + bounding.top + 20
                        && event.clientX > bounding.left + labelX1 && event.clientX < bounding.left + labelX2) {
                    isDrawShExp = !isDrawShExp;
                    redraw();
                } else if (event.clientY >= coordinateY + bounding.top && event.clientY <= coordinateY + bounding.top + 20
                        && event.clientX > bounding.left + labelX2 && event.clientX < bounding.left + labelX3) {
                    isDrawSzExp = !isDrawSzExp;
                    redraw();
                } else {
                    dateContainer.toggleClass("disappear");
                }
            });
            canvasLine.addEventListener('click', function (event) {
                dateContainer.toggleClass("disappear");
            });
            submitButton.addEventListener('click', function (event) {
                redraw();
            });
            resetButton.addEventListener('click', function (event) {
                reset();
            });
        }

        function onMouseMove(event) {
            var bounding = canvas.getBoundingClientRect();
            var leftRedunt = document.body.getBoundingClientRect().left;
            if (event.clientX < bounding.left + leftPadding || event.clientX > bounding.right - rightPadding || event.clientY < bounding.top + leftPadding || event.clientY > bounding.bottom - bottomPadding) {
                return;
            }
            var result = getRecentData(event.clientX - bounding.left);
            canvasLine.style.left = bounding.left + result.x - leftRedunt;
            canvasLine.style.top = bounding.top + topPadding + document.body.scrollTop;
            drawValue(result);
        }

        function reset() {
            startDate = startInitialDate;
            endDate = endInitialDate;

            startDateSelecter.datepicker("setDate", startInitialDate);
            endDateSelecter.datepicker("setDate", endInitialDate);
        }

        function redraw() {
            $.ajax({
                type: 'post',
                url: 'getAlgorithmResult.json',
                dataType: 'json',
                data: {algorithm: encodeURI(algorithmName), startDate: startDate, endDate: endDate},
                success: function (data) {
                    if (data.success) {
                        var tmpResultArray = JSON.parse(data.value);
                        if (tmpResultArray.length < 10) {
                            alert("有效数据少于10个，请扩大日期范围");
                            return;
                        }
                        resultArray = tmpResultArray;
                        drawGraph(resultArray, algorithmName, avg);

                        var e = new Object();
                        e.clientX = canvas.getBoundingClientRect.left + leftPadding;
                        e.clientY = canvas.getBoundingClientRect.top + topPadding;
                        e.toElement = canvas;
                        onMouseMove(e);
                    }
                }
            });
        }

        function drawGraph(drawArray, title, avg) {
            var height = canvas.getAttribute('height');
            var width = canvas.getAttribute('width');
            var ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, width, height);
            var verticalPart = 8, horizontalPart = 10;

            ctx.lineWidth = 1;
            ctx.strokeStyle = '#444444';
            ctx.strokeRect(leftPadding, topPadding, width - leftPadding - rightPadding, height - topPadding - bottomPadding);
            var eachHeight = (height - topPadding - bottomPadding - innerTopPadding - innerBottomPadding) / verticalPart;
            for (var i = 1; i < verticalPart; i++) {
                var coordinateY = topPadding + innerTopPadding + i * eachHeight;
                drawLine(leftPadding, coordinateY, width - rightPadding, coordinateY, 5);
            }
            var eachWidth = (width - leftPadding - rightPadding - innerLeftPadding - innerRightPadding) / horizontalPart;
            for (var i = 1; i < horizontalPart; i++) {
                var coordinateX = leftPadding + innerLeftPadding + i * eachWidth;
                drawLine(coordinateX, topPadding, coordinateX, height - bottomPadding, 5);
            }

            var len = drawArray.length;
            var intervalX = (width - leftPadding - rightPadding - innerLeftPadding - innerRightPadding) / (len - 1);
            var coordinateX = leftPadding + innerLeftPadding;
            for (var i = 0; i < drawArray.length; i++) {
                drawArray[i].x = coordinateX + i * intervalX;
            }

            ctx.beginPath();
            ctx.strokeStyle = '#FF0000';
            var negative = false;
            for (var i = 0; i < drawArray.length; i++) {
                if (drawArray[i].value < 0) {
                    negative = true;
                }
            }
            for (var i = 0; i < drawArray.length; i++) {
                var gapY = height - topPadding - bottomPadding - innerTopPadding - innerBottomPadding;
                if (negative) {
                    var coordinateY = height - bottomPadding - innerBottomPadding - gapY * (drawArray[i].value * 1.0 + 1.0) / 2.0;
                } else {
                    var coordinateY = height - bottomPadding - innerBottomPadding - gapY * drawArray[i].value;
                }
                if (i == 0) {
                    ctx.moveTo(drawArray[i].x, coordinateY);
                } else {
                    ctx.lineTo(drawArray[i].x, coordinateY);
                    ctx.stroke();
                }
            }

            // 上证指数

            ctx.beginPath();
            ctx.strokeStyle = '#0000FF';
            var highestShExp = 0, lowestShExp = 0;
            for (var i = 0; i < drawArray.length; i++) {
                if (i == 0) {
                    highestShExp = drawArray[i].shExp;
                    lowestShExp = drawArray[i].shExp;
                } else {
                    highestShExp = Math.max(highestShExp, drawArray[i].shExp);
                    lowestShExp = Math.min(lowestShExp, drawArray[i].shExp);
                }
            }
            for (var i = 0; i < drawArray.length; i++) {
                var gapShExp = highestShExp - lowestShExp;
                var gapY = height - topPadding - bottomPadding - innerTopPadding - innerBottomPadding;
                var coordinateY = topPadding + innerTopPadding + (highestShExp - drawArray[i].shExp) / gapShExp * gapY;
                if (isDrawShExp) {
                    if (i == 0) {
                        ctx.moveTo(drawArray[i].x, coordinateY);
                    } else {
                        ctx.lineTo(drawArray[i].x, coordinateY);
                        ctx.stroke();
                    }
                }
            }

            // 深圳综指
            ctx.beginPath();
            ctx.strokeStyle = '#499598';
            var highestSzExp = 0, lowestSzExp = 0;
            for (var i = 0; i < drawArray.length; i++) {
                if (i == 0) {
                    highestSzExp = drawArray[i].szExp;
                    lowestSzExp = drawArray[i].szExp;
                } else {
                    highestSzExp = Math.max(highestSzExp, drawArray[i].szExp);
                    lowestSzExp = Math.min(lowestSzExp, drawArray[i].szExp);
                }
            }
            for (var i = 0; i < drawArray.length; i++) {
                var gapSzExp = highestSzExp - lowestSzExp;
                var gapY = height - topPadding - bottomPadding - innerTopPadding - innerBottomPadding;
                var coordinateY = topPadding + innerTopPadding + (highestSzExp - drawArray[i].szExp) / gapSzExp * gapY;

                if (isDrawSzExp) {
                    if (i == 0) {
                        ctx.moveTo(drawArray[i].x, coordinateY);
                    } else {
                        ctx.lineTo(drawArray[i].x, coordinateY);
                        ctx.stroke();
                    }
                }
            }

            ctx.beginPath();
            ctx.strokeStyle = '#B915D6';
            for (var i = 0; i < drawArray.length; i++) {
                var gapShExp = highestShExp - lowestShExp;
                var gapY = height - topPadding - bottomPadding - innerTopPadding - innerBottomPadding;
                if (negative) {
                    var coordinateY = height - bottomPadding - innerBottomPadding - gapY * (drawArray[i].avg * 1.0 + 1.0) / 2.0;
                } else {
                    var coordinateY = height - bottomPadding - innerBottomPadding - gapY * drawArray[i].avg;
                }
                if (i == 0) {
                    ctx.moveTo(drawArray[i].x, coordinateY);
                } else {
                    ctx.lineTo(drawArray[i].x, coordinateY);
                    ctx.stroke();
                }
            }

            ctx.beginPath();
            ctx.fillStyle = "#000000";
            ctx.font = "12px Arial";
            for (var i = 0; i <= horizontalPart; i++) {
                var x = leftPadding + innerLeftPadding + i * eachWidth;
                var recentData = getRecentData(x, drawArray);
                ctx.textAlign = "center";
                ctx.fillText(recentData.date, x, height - bottomPadding + 25);
            }

            for (var i = 0; i <= verticalPart; i++) {
                var y = topPadding + innerTopPadding + i * eachHeight;
                if (isDrawShExp) {
                    var labelExp = (highestShExp - lowestShExp) * (verticalPart - i) / verticalPart + lowestShExp;
                } else {
                    var labelExp = (highestSzExp - lowestSzExp) * (verticalPart - i) / verticalPart + lowestSzExp;
                }
                if (negative) {
                    var labelPercent = (verticalPart - i) / verticalPart * 2.0 - 1.0;
                    var labelPercentStr = labelPercent.toFixed(2);
                } else {
                    var labelPercent = (verticalPart - i) / verticalPart;
                    var labelPercentStr = labelPercent.toFixed(3);
                }
                ctx.textAlign = "right";
                ctx.fillText(labelExp.toFixed(2), leftPadding - 10, y + 6);
                ctx.textAlign = "left";
                ctx.fillText(labelPercentStr, width - rightPadding + 10, y + 6);
            }

            ctx.beginPath();
            ctx.font = "14px Arial Bold";
            ctx.textAlign = "center";
            ctx.fillText(title, width >> 1, topPadding >> 1);
            ctx.stroke();

            if (avg != null) {
                ctx.beginPath();
                ctx.lineWidth = 2;
                ctx.strokeStyle = "#00FF00";
                if (negative) {
                    var coordinateY = height - bottomPadding - innerBottomPadding - gapY * (avg * 1.0 + 1.0) / 2.0;
                } else {
                    var coordinateY = height - bottomPadding - innerBottomPadding - gapY * avg;
                }
                ctx.moveTo(leftPadding + innerLeftPadding, coordinateY);
                ctx.lineTo(width - rightPadding - innerRightPadding, coordinateY)
                ctx.stroke();
            }
        }

        function drawLine(x1, y1, x2, y2, interval) {
            var ctx = canvas.getContext('2d');
            ctx.beginPath();
            ctx.lineWidth = 1;
            ctx.strokeStyle = '#CCCCCC';
            var slope = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            var xInterval = interval / slope * (x2 - x1);
            var yInterval = interval / slope * (y2 - y1);
            for (var xtmp = x1, ytmp = y1; xtmp <= x2 && ytmp <= y2; xtmp += 2 * xInterval, ytmp += 2 * yInterval) {
                ctx.moveTo(xtmp, ytmp);
                ctx.lineTo(Math.min(x2, xtmp + xInterval), Math.min(y2, ytmp + yInterval));
                ctx.stroke();
            }
        }

        function drawValue(result) {
            var ctx = canvas.getContext('2d');
            var height = canvas.getAttribute('height');
            var width = canvas.getAttribute('width');

            var coordinateY = height - bottomPadding + 45;

            ctx.clearRect(0, coordinateY, width, height - coordinateY);
            var labelXOffset = 30;
            ctx.beginPath();
            ctx.lineWidth = 1;
            ctx.textAlign = "left";
            ctx.strokeStyle = '#000000';
            if (isDrawShExp) {
                ctx.fillStyle = '#0000FF';
                ctx.fillRect(labelX1, coordinateY, 20, 20);
                ctx.strokeText("上证指数: " + result.shExp + "(" + result.date + ")", labelX1 + labelXOffset, coordinateY + 16);
            } else {
                ctx.fillStyle = '#888888';
                ctx.fillRect(labelX1, coordinateY, 20, 20);
                ctx.strokeText("上证指数(隐藏)", labelX1 + labelXOffset, coordinateY + 16);
            }
            if (isDrawSzExp) {
                ctx.fillStyle = "#499598";
                ctx.fillRect(labelX2, coordinateY, 20, 20);
                ctx.strokeText("深圳综指: " + result.szExp + "(" + result.date + ")", labelX2 + labelXOffset, coordinateY + 16);
            } else {
                ctx.fillStyle = "#888888";
                ctx.fillRect(labelX2, coordinateY, 20, 20);
                ctx.strokeText("深圳综指(隐藏)", labelX2 + labelXOffset, coordinateY + 16);
            }
            ctx.fillStyle = '#FF0000';
            ctx.fillRect(labelX3, coordinateY, 20, 20);
            ctx.strokeText("百分比: " + Math.round(result.value * 100) + "%(" + result.date + ")", labelX3 + labelXOffset, coordinateY + 16);
            ctx.stroke();
        }

        function getRecentData(x) {
            var start = 0, end = resultArray.length - 1;
            while (start < end) {
                if (resultArray[end].x < x) {
                    return resultArray[end];
                }
                if (resultArray[start].x > x) {
                    return resultArray[start];
                }
                var mid = (start + end) >> 1;
                if ((resultArray[mid].x + resultArray[mid + 1].x) / 2 < x) {
                    start = mid + 1;
                } else {
                    end = mid;
                }
            }
            return resultArray[end];
        }

    }

</script>

</body>
</html>
