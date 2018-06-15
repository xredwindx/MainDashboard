/**
 * Created by ??? on 2018-05-23.
 */
routeApp.controller("mainDashboardCtl", function ($scope, $http, $interval, $window, $sce) {
    // init
    $scope.init = function () {
        var userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        $scope.userID = userInfo.user_id;

        $scope.getMainDashboarList();
        $scope.getErrMsgList();
    }

    // 에러 메세지
    $scope.mdAlertHandling = function (alertMsg, alertType) {
        $scope.mdAlertClass = ["alert"];

        if(alertType == "success") {
            $scope.mdAlertClass.push("alert-success");
        } else {
            $scope.mdAlertClass.push("alert-danger");
        }

        $scope.mdAlert = true;
        $scope.mdAlertMsg = alertMsg;
    }

    // dashboard 목록
    $scope.getMainDashboarList = function () {
        $http({
            method: "POST",
            url: "/api/maindashboard",
            headers: {
                "Content-Type": "application/json"
            },
            data: "{}"
        }).then(function success (res) {
            $scope.mainDashboardList = res.data;
            $scope.mdAlert = false;
        }, function error (error) {
            $scope.mdAlertHandling("에러가 발생하였습니다", "danger");
        });
    }

    // 에러메세지 목록
    $scope.getErrMsgList = function () {
        $http({
            method: "POST",
            url: "/api/maindashboard/errmsg",
            headers: {
                "Content-Type": "application/json"
            },
            data: "{}"
        }).then(function success (res) {
            $scope.errMsgList = res.data;
        }, function error (error) {
        });
    }

    // init
    $scope.init();

    // main dashboard class color
    $scope.getStatusClass = function (item) {
        var ret = "btn btn-circle btn-ll";
        if(item == "") {
            ret += " btn-default";
        } else if (item == "0") {
            ret += " btn-success";
        } else if (item == "1") {
            ret += " btn-warning";
        } else {
            ret += " btn-danger";
        }
        return ret;
    }

    // message class color
    $scope.getErrStatusClass = function (item) {
        var ret = "default";
        if(item == "1") {
            ret = "warning";
        } else if(item == "2") {
            ret = "danger";
        }

        return ret;
    }

    // status text
    $scope.getCustom = function (webName, name) {
        if(webName == "") {
            return name;
        } else {
            return webName;
        }
    }

    // live hidden
    $scope.showLive = function (item) {
        if(item.live_stream_status == "") {
            return false;
        }
        return true;
    }

    // link
    $scope.blankLink = function (item, type) {
        if(type == "gf") {
            var uri = item.gf_uri+"&var-service_name="+item.gf_svc_name+"&var-svr_id="+item.gf_svr_id;
            $window.open(uri, "_blank");
        } else {
            if(item.live_stream_status == "") {
                return;
            }

            $window.open("/streaming/dashboard?custom="+$window.encodeURIComponent($window.btoa($window.encodeURI(item.custom))), "_blank");
        }
    }

    // message
    $scope.getText = function (item) {
        // 마지막 문자 \n 제거
        var sItem = item.slice(0, -1);
        var result = sItem.replace("\n", "<br/>");
        return $sce.trustAsHtml(result);
    }

    // refresh
    $interval(function () { $scope.init(); }, 60*1000);
});
