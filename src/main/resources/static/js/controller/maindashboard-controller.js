/**
 * Created by ??? on 2018-05-23.
 */
routeApp.controller("mainDashboardCtl", function ($scope, $http, $interval, $window) {
    // init
    $scope.init = function () {
        var userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        $scope.userID = userInfo.user_id;
        $scope.getMainDashboarList();
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

    // 목록
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

    // init
    $scope.init();

    // class color
    $scope.getStatusClass = function (item) {
        var ret = "btn btn-circle btn-xl";
        if(item == "") {
            ret += " btn-default";
        } else if (item == "1") {
            ret += " btn-success";
        } else {
            ret += " btn-danger";
        }
        return ret;
    }

    // status text
    $scope.getStatusText = function (item) {
        var ret = "N/A";
        if (item == "1") {
            ret = "정상";
        } else if (item == "0") {
            ret = "에러";
        }
        return ret;
    }

    // link
    $scope.blankLink = function (item, type) {
        if(type == "gf") {
            $window.open(item.gf_url, "_blank");
        } else {
            if(item.live_stream_status == "") {
                return;
            }

            $window.open("/streaming/dashboard?custom="+$window.btoa($window.encodeURI(item.custom)), "_blank");
        }
    }

    // refresh
    $interval(function () { $scope.init(); }, 60*1000);
});
