/**
 * Created by ??? on 2018-05-23.
 */
routeApp.controller("mainDashboardCtl", function ($scope, $http, $interval, $window, $uibModal) {
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

    $scope.getBgColor = function (item) {
        var ret = "danger";
        if(item == "Warning") {
            ret = "active";
        } else if (item == "Error") {
            ret = "warning";
        }
        return ret;
    }

    $scope.history = function () {
        $window.open("/streaming/history","_blank");
    }

    // refresh
    $interval(function () { $scope.init(); }, 60*1000);
});
