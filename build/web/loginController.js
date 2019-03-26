var myApp = angular.module('login', []).controller('loginController', function ($scope, $http) {
    $scope.ajaxCheckUser = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'checkUser', account: $scope.account}})
                .then(response => {
                    nameElement = document.getElementById("name");
            console.log(response.data.response);
                    if (response.data.response === "false") {
                        nameElement.setCustomValidity("L'utente non esiste");
                        nameElement.reportValidity();
                        nameElement.validity = false;
                    } else {
                        nameElement.setCustomValidity("");
                        nameElement.validity = true;
                    }
                }).catch(error => console.log(error));
    };
   /* $scope.login = function () {
        $http.get("/Ripetizioni/Controller", {params: {action: 'login', user: $scope.user, password: $scope.password}})
                .then(response => {
                    if (response.data.error === "") {
                        console.log(response);
                        window.location.assign("home.html");
                    } else {
                        if(response.data.error === "Wrong password"){
                            passwordElement = document.getElementById("password");
                            passwordElement.setCustomValidity("Passwor Errata");
                            passwordElement.validity = false;
                            passwordElement.reportValidity();
                        }
                    }
                }).catch(error => console.log(error));
    };*/
});
