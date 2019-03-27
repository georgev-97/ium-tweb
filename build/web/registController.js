var myApp = angular.module('regist', []).controller("registController", 
    function($scope) {
        $scope.showConfirm = false;
	$scope.checkPassword = function(){
            if($scope.password === $scope.passwordConfirm){
                $scope.equal = "password corrispondenti";
            }else{
                $scope.equal = "password non corrispondenti";
            }
        };
    });

