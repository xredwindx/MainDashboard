/**
 * Created by ??? on 2017-12-22.
 */
var routeApp = app.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state("streaming", {
            url: "/",
            templateUrl: "/maindashboard/main"
        });
});