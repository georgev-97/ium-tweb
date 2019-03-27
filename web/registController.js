var myApp = angular.module('regist', []).controller("registController", 
    function($scope) {
        $scope.showConfirm = false;
	$scope.checkPassword = function(){
            if($scope.password === $scope.passwordConfirm){
                $scope.equal = "password corrispondenti";
                $scope.colour = {'color':'green'};
            }else{
                $scope.equal = "password non corrispondenti";
                $scope.colour = {'color':'red'};
            }
        };
        $scope.submit = function(){
            if(window.regForm.account.value == "" || window.regForm.password.value == ""){
                window.alert("compilare tutti i campi");
                return;
            }else{
                window.regForm.submit();
            }
        } 
    });

