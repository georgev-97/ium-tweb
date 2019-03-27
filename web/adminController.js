var myApp = angular.module('admin', []).controller('adminController', function ($scope, $http) {
    $http.get("/Ripetizioni/Controller", {params:{command: 'getSession'}})
            .then(response => {
                if(response.data !== ""){
                    console.log(response.data);
                    $scope.user = response.data.account;
                }
                else{
                    $scope.user = "anonimo";
                }
    }).catch(error => console.log(error));
    $scope.add = function(){
            $http.get("/Ripetizioni/Controller", {params:{command: 'addCourse', course: $scope.name, description: $scope.description}})
                    .then(function(response){
                        console.log(response.data);
                        if(response.data !==  ""){
                            if(response.data.error === ""){
                                window.location.assign("admin.html");
                                alert("inserimento andato a buon fine");
                            }
                            else alert(response.data.error);
                        }
            });

    }
});
