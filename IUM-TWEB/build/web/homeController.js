var myApp = angular.module('home', []).controller('homeController', function ($scope, $http) {
    $http.get("/Ripetizioni/Controller", {params:{command: 'getSession'}})
            .then(response => {
                if(response.data !== ""){
                    console.log(response);
                    $scope.user = response.data.user;
                }
                else{
                    $scope.user = "azz";
                }
    }).catch(error => console.log(error));
});