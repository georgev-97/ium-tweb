var myApp = angular.module('user', []).controller('userController', function ($scope, $http) {
    $http.get("/Ripetizioni/Controller", {params: {command: 'getSession'}})
            .then(response => {
                if (response.data !== "") {
                    console.log(response.data);
                    $scope.user = response.data.account;
                } else {
                    $scope.user = "anonimo";
                }
            }).catch(error => console.log(error));

     $http.get("/Ripetizioni/Controller", {params:{command:'getBookings'}})
             .then(response=> {
                 if(response.data !== ""){
                     $scope.userReservations = JSON.parse(response.data.userReservations);
                 }
                 else(alert(response.data.error));
     });
});
