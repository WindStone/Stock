<%--
  Created by IntelliJ IDEA.
  User: yuanren.syr
  Date: 2016/1/11
  Time: 1:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>结果下载页面</title>
    <link rel="stylesheet" href="/uisvr/css/table.css"/>
    <!--<link rel="stylesheet" href="/uisvr/css/process.css"/>-->
    <style>
        a:link {
            text-decoration: none;
        }

        a:visited {
            text-decoration: none;
        }

        .progress {
            height: 20px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 2px;
            border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .progress--active .progress__bar {
            opacity: 1;
        }

        .progress__text {
            font-size: 12px;
            font-weight: normal;
            text-shadow: 0 1px 1px rgba(0, 0, 0, 0.6);
            padding: 0 0;
            position: relative;
            width: 100%;
            align: center;
            top: -20px;
        }

        .progress__text em {
            font-style: normal;
        }

        .progress__bar {
            color: white;
            line-height: 19px;
            display: block;
            position: relative;
            top: -1px;
            left: -1px;
            width: 0%;
            height: 100%;
            opacity: 0;
            border: 1px solid;
            border-radius: 2px 0 0 2px;
            background-size: 100px 30px, 130px 30px, 130px 30px;
            background-position: -20% center, right center, left center;
            background-repeat: no-repeat, no-repeat, no-repeat;
            -webkit-transition: opacity 0.2s ease, width 0.8s ease-out, background-color 1s ease, border-color 0.3s ease, box-shadow 1s ease;
            transition: opacity 0.2s ease, width 0.8s ease-out, background-color 1s ease, border-color 0.3s ease, box-shadow 1s ease;
            -webkit-animation: pulse 2s ease-out infinite;
            animation: pulse 2s ease-out infinite;
            background-color: rgba(201, 4, 20, 0.95);
            background-image: -webkit-linear-gradient(0deg, rgba(226, 4, 22, 0) 10%, rgba(250, 6, 26, 0.8) 30%, #fb1f31 70%, rgba(250, 6, 26, 0.8) 80%, rgba(226, 4, 22, 0) 90%), -webkit-linear-gradient(left, rgba(251, 31, 49, 0) 0%, #fb1f31 100%), -webkit-linear-gradient(right, rgba(251, 31, 49, 0) 0%, #fb1f31 100%);
            background-image: linear-gradient(90deg, rgba(226, 4, 22, 0) 10%, rgba(250, 6, 26, 0.8) 30%, #fb1f31 70%, rgba(250, 6, 26, 0.8) 80%, rgba(226, 4, 22, 0) 90%), linear-gradient(to right, rgba(251, 31, 49, 0) 0%, #fb1f31 100%), linear-gradient(to left, rgba(251, 31, 49, 0) 0%, #fb1f31 100%);
            border-color: #fb3848;
            box-shadow: 0 0 0.6em #fa061a inset, 0 0 0.4em #e20416 inset, 0 0 0.5em rgba(201, 4, 20, 0.5), 0 0 0.1em rgba(254, 206, 210, 0.5);
        }

        .progress__bar--orange {
            background-color: rgba(201, 47, 0, 0.95);
            background-image: -webkit-linear-gradient(0deg, rgba(227, 53, 0, 0) 10%, rgba(252, 59, 0, 0.8) 30%, #ff4d17 70%, rgba(252, 59, 0, 0.8) 80%, rgba(227, 53, 0, 0) 90%), -webkit-linear-gradient(left, rgba(255, 77, 23, 0) 0%, #ff4d17 100%), -webkit-linear-gradient(right, rgba(255, 77, 23, 0) 0%, #ff4d17 100%);
            background-image: linear-gradient(90deg, rgba(227, 53, 0, 0) 10%, rgba(252, 59, 0, 0.8) 30%, #ff4d17 70%, rgba(252, 59, 0, 0.8) 80%, rgba(227, 53, 0, 0) 90%), linear-gradient(to right, rgba(255, 77, 23, 0) 0%, #ff4d17 100%), linear-gradient(to left, rgba(255, 77, 23, 0) 0%, #ff4d17 100%);
            border-color: #ff6030;
            box-shadow: 0 0 0.6em #fc3b00 inset, 0 0 0.4em #e33500 inset, 0 0 0.5em rgba(201, 47, 0, 0.5), 0 0 0.1em rgba(255, 214, 201, 0.5);
        }

        .progress__bar--yellow {
            background-color: rgba(232, 158, 0, 0.95);
            background-image: -webkit-linear-gradient(0deg, rgba(255, 174, 2, 0) 10%, rgba(255, 183, 28, 0.8) 30%, #ffbf36 70%, rgba(255, 183, 28, 0.8) 80%, rgba(255, 174, 2, 0) 90%), -webkit-linear-gradient(left, rgba(255, 191, 54, 0) 0%, #ffbf36 100%), -webkit-linear-gradient(right, rgba(255, 191, 54, 0) 0%, #ffbf36 100%);
            background-image: linear-gradient(90deg, rgba(255, 174, 2, 0) 10%, rgba(255, 183, 28, 0.8) 30%, #ffbf36 70%, rgba(255, 183, 28, 0.8) 80%, rgba(255, 174, 2, 0) 90%), linear-gradient(to right, rgba(255, 191, 54, 0) 0%, #ffbf36 100%), linear-gradient(to left, rgba(255, 191, 54, 0) 0%, #ffbf36 100%);
            border-color: #ffc74f;
            box-shadow: 0 0 0.6em #ffb71c inset, 0 0 0.4em #ffae02 inset, 0 0 0.5em rgba(232, 158, 0, 0.5), 0 0 0.1em rgba(255, 248, 232, 0.5);
        }

        .progress__bar--green {
            background-color: rgba(0, 178, 23, 0.95);
            background-image: -webkit-linear-gradient(0deg, rgba(0, 203, 26, 0) 10%, rgba(0, 229, 30, 0.8) 30%, #00fe21 70%, rgba(0, 229, 30, 0.8) 80%, rgba(0, 203, 26, 0) 90%), -webkit-linear-gradient(left, rgba(0, 254, 33, 0) 0%, #00fe21 100%), -webkit-linear-gradient(right, rgba(0, 254, 33, 0) 0%, #00fe21 100%);
            background-image: linear-gradient(90deg, rgba(0, 203, 26, 0) 10%, rgba(0, 229, 30, 0.8) 30%, #00fe21 70%, rgba(0, 229, 30, 0.8) 80%, rgba(0, 203, 26, 0) 90%), linear-gradient(to right, rgba(0, 254, 33, 0) 0%, #00fe21 100%), linear-gradient(to left, rgba(0, 254, 33, 0) 0%, #00fe21 100%);
            border-color: #19ff37;
            box-shadow: 0 0 0.6em #00e51e inset, 0 0 0.4em #00cb1a inset, 0 0 0.5em rgba(0, 178, 23, 0.5), 0 0 0.1em rgba(178, 255, 188, 0.5);
        }

        .progress__bar--blue {
            background-color: rgba(18, 135, 204, 0.95);
            background-image: -webkit-linear-gradient(0deg, rgba(20, 151, 227, 0) 10%, rgba(37, 162, 236, 0.8) 30%, #3dacee 70%, rgba(37, 162, 236, 0.8) 80%, rgba(20, 151, 227, 0) 90%), -webkit-linear-gradient(left, rgba(61, 172, 238, 0) 0%, #3dacee 100%), -webkit-linear-gradient(right, rgba(61, 172, 238, 0) 0%, #3dacee 100%);
            background-image: linear-gradient(90deg, rgba(20, 151, 227, 0) 10%, rgba(37, 162, 236, 0.8) 30%, #3dacee 70%, rgba(37, 162, 236, 0.8) 80%, rgba(20, 151, 227, 0) 90%), linear-gradient(to right, rgba(61, 172, 238, 0) 0%, #3dacee 100%), linear-gradient(to left, rgba(61, 172, 238, 0) 0%, #3dacee 100%);
            border-color: #54b6f0;
            box-shadow: 0 0 0.6em #25a2ec inset, 0 0 0.4em #1497e3 inset, 0 0 0.5em rgba(18, 135, 204, 0.5), 0 0 0.1em rgba(225, 242, 252, 0.5);
        }

        .progress__bar:before, .progress__bar:after {
            content: "";
            position: absolute;
            right: -1px;
            top: -10px;
            width: 1px;
            height: 40px;
        }

        .progress__bar:before {
            width: 7px;
            right: -4px;
            background: -webkit-radial-gradient(center, ellipse, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0) 75%);
            background: radial-gradient(ellipse at center, rgba(255, 255, 255, 0.4) 0%, rgba(255, 255, 255, 0) 75%);
        }

        .progress__bar:after {
            background: -webkit-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.3) 25%, rgba(255, 255, 255, 0.3) 75%, rgba(255, 255, 255, 0) 100%);
            background: linear-gradient(to bottom, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.3) 25%, rgba(255, 255, 255, 0.3) 75%, rgba(255, 255, 255, 0) 100%);
        }

        .progress--complete .progress__bar {
            -webkit-animation: none;
            animation: none;
            border-radius: 2px;
        }

        .progress--complete .progress__bar:after, .progress--complete .progress__bar:before {
            opacity: 0;
        }

        @-webkit-keyframes pulse {
            0% {
                background-position: -50% center, right center, left center;
            }
            100% {
                background-position: 150% center, right center, left center;
            }
        }

        @keyframes pulse {
            0% {
                background-position: -50% center, right center, left center;
            }
            100% {
                background-position: 150% center, right center, left center;
            }
        }
    </style>

</head>
<body style="position:relative; margin:10px auto; width:1280px">
<table class="bordered" style="width:100%">
    <tr>
        <th style="width:8%">日期</th>
        <c:forEach var="title" items="${titles}">
            <th style="width:23%">${title}</th>
        </c:forEach>
    </tr>
    <c:forEach var="resultBundle" items="${resultBundles}">
        <tr>
            <td>${resultBundle.date}</td>
            <c:forEach var="fileBundle" items="${resultBundle.fileBundleViews}" varStatus="s">
                <td>
                    <div style="witdh: 100%;">
                        <div style="width: 40%; float: left; padding-left: 10%;">
                            <c:if test="${fileBundle != null}">
                                <a href="<c:url value="/file.html?filename=${fileBundle.fileUrl}"/>"
                                   class="blue button">Download</a>
                            </c:if>
                            <c:if test="${fileBundle == null}">
                                --------
                            </c:if>
                        </div>
                        <div style="width: 40%; float: left; padding-right: 10%;">
                            <c:if test="${fileBundle.fileUrl != null}">
                                <a class="blue button"
                                   onclick="trigger(<c:url value="'${fileBundle.fileUrl}'"/>)">Trigger</a>
                            </c:if>
                            <c:if test="${fileBundle.fileUrl == null}">
                                <a class="blue button"
                                   onclick="trigger(<c:url
                                           value="'${encodeTitles[s.index]}_${resultBundle.date}.xls'"/>)">Trigger</a>
                            </c:if>
                        </div>
                    </div>
                </td>
            </c:forEach>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="${fn:length(titles) + 1}" style="height: 50px">
            <div id="progress" class="progress">
                <b class="progress__bar progress__bar--blue" style="width:100%">
                </b>
                <span id="process-text" class="progress__text">
                    Complete
                </span>
            </div>
        </td>
    </tr>
</table>

<script>
    window.jQuery || document.write('<script src="uisvr/js/jquery-2.1.1.min.js"><\/script>')
</script>
<script>
    var $progress = $('.progress'), $bar = $('.progress__bar'), $text = $('.progress__text'), percent = 0, update, resetColors, speed = 1000, orange = 30, yellow = 55, green = 85, timer;
    resetColors = function () {
        $bar.removeClass('progress__bar--green').removeClass('progress__bar--yellow').removeClass('progress__bar--orange').removeClass('progress__bar--blue');
        $progress.removeClass('progress--complete');
    };

    trigger = function (str) {
        $.get("trigger.json", {"fileName": str});
        update();
    }

    update = function () {
        timer = setTimeout(function () {
            /**
             percent += Math.random() * 1.8;
             percent = parseFloat(percent.toFixed(1));
             $text.find('em').text(percent + '%');
             if (percent >= 100) {
                percent = 100;
                $progress.addClass('progress--complete');
                $bar.addClass('progress__bar--blue');
                $text.find('em').text('Complete');
            } else {
                if (percent >= green) {
                    $bar.addClass('progress__bar--green');
                } else if (percent >= yellow) {
                    $bar.addClass('progress__bar--yellow');
                } else if (percent >= orange) {
                    $bar.addClass('progress__bar--orange');
                }
                speed = Math.floor(Math.random() * 900);
                update();
            }
             $bar.css({width: percent + '%'}); **/
            $.ajax({
                type: 'post',
                url: 'process.json',
                dataType: 'json',
                success: function (data) {
                    percent = data.value * 100;
                    percent = parseFloat(percent.toFixed(1));
                    if (percent >= 100) {
                        percent = 100;
                        $progress.addClass('progress--complete');
                        $bar.removeClass('progress__bar--green');
                        $bar.removeClass('progress__bar--yellow');
                        $bar.removeClass('progress__bar--orange');
                        $bar.addClass('progress__bar--blue');
                        $("#process-text").text('Complete');
                    } else {
                        if (percent <= orange) {
                            $bar.removeClass('progress__bar--green');
                            $bar.removeClass('progress__bar--yellow');
                            $bar.removeClass('progress__bar--orange');
                            $bar.removeClass('progress__bar--blue');
                        } else if (percent >= orange && percent < yellow) {
                            $bar.removeClass('progress__bar--green');
                            $bar.removeClass('progress__bar--yellow');
                            $bar.addClass('progress__bar--orange');
                            $bar.removeClass('progress__bar--blue');
                        } else if (percent >= yellow && percent < green) {
                            $bar.removeClass('progress__bar--green');
                            $bar.addClass('progress__bar--yellow');
                            $bar.removeClass('progress__bar--orange');
                            $bar.removeClass('progress__bar--blue');
                        } else if (percent >= green && percent < 100) {
                            $bar.addClass('progress__bar--green');
                            $bar.removeClass('progress__bar--yellow');
                            $bar.removeClass('progress__bar--orange');
                            $bar.removeClass('progress__bar--blue');
                        }
                        $("#process-text").text('Process: ' + percent + "%");
                    }
                    $bar.css({width: percent + '%'});
                    update();
                }
            })
        }, speed);
    };
    setTimeout(function () {
        $progress.addClass('progress--active');
        update();
    }, 1000);
</script>

</body>
</html>
