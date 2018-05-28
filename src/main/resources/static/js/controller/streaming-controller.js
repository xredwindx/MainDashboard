/**
 * Created by ??? on 2018-01-09.
 * streaming monitor
 */
app.controller("streamingCtrl", function ($scope, $http, $interval, $window, $uibModal) {
    // init
    $scope.init = function () {
        $scope.customEncript = "";
        var custom = $scope.customEncript;
        console.log(custom);
        $scope.custom = decodeURI($window.atob((custom)));
        $scope.getStreamingList();
    }

    // 장애 처리 메세지
    $scope.streamingAlertHandling = function (alertMsg, alertType) {
        $scope.streamingAlertClass = ["alert"];

        if(alertType == "success") {
            $scope.streamingAlertClass.push("alert-success");
        } else {
            $scope.streamingAlertClass.push("alert-danger");
        }

        $scope.streamingAlert = true;
        $scope.streamingAlertMsg = alertMsg;
    }

    // live streaming 목록
    $scope.getStreamingList = function () {
        $http({
            method: "POST",
            url: "/api/streaming",
            headers: {
                "Content-Type": "application/json"
            },
            data: { "telcoGubun" : "KT", "custom" : $scope.custom }
        }).then(function success (res) {
            $scope.streamingList = res.data;
            $scope.streamingAlert = false;
        }, function error (error) {
            $scope.streamingAlertHandling("에러가 발생하였습니다", "danger");
        });
    }

    // init
    $scope.init();

    $scope.getBgColor = function (item) {
        var ret = "success";
        if(item == "Warning") {
            ret = "warning";
        } else if (item == "Error" || item == "Critical") {
            ret = "danger";
        }
        return ret;
    }

    $scope.history = function () {
        $window.open("/streaming/history","_blank");
    }

    // modal play
    $scope.play = function (item) {
        var playModalIns = $uibModal.open({
            templateUrl: "/streaming/modalPlay",
            controller: "playModalCtrl",
            resolve : {
                item : function () {
                    return item;
                }
            }
        });

        playModalIns.result.then(function () {});
    }

    // refresh
    $interval(function () { $scope.init(); }, 60*1000);
});

/**
 *  play
 */
app.controller("playModalCtrl", function ($scope, $rootScope, $sce, $uibModalInstance, item) {
    // init
    $scope.init = function () {
        $scope.hostName = item.host_name;
        $scope.vol = item.volume;
        $scope.streamName = item.stream_name;
        $scope.streamUrl = "http://"+$scope.hostName+":1935/"+$scope.vol+"/"+$scope.streamName+"/playlist.m3u8";
    }
    $scope.init();

    $scope.ok = function () {
        $uibModalInstance.close();
    }

});