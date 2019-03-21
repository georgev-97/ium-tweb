var myApp = angular.module('home', []).controller('homeController', function ($scope, $http) {
    $http.get("/Ripetizioni/Main", {params:{action: 'getSession'}})
            .then(response => {
                if(response.data !== ""){
                    console.log(response);
                    $scope.user = response.data.user;
                }
                else{
                    $scope.user = "PORCOILDIO";
                }
    }).catch(error => console.log(error));
});