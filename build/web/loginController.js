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
    $scope.login = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'login', account: $scope.account, password: $scope.password}})
                .then(response => {
                    console.log(response);
                    if (response.data.error === "") {
                        console.log(response.data.account);
                        if (response.data.role === "admin") {
                            window.location.assign("admin.html");
                        } else {
                            window.location.assign("utente.html");
                        }
                    } else {
                        if (response.data.error === "Wrong password") {
                            passwordElement = document.getElementById("password");
                            passwordElement.setCustomValidity("Password Errata");
                            passwordElement.validity = false;
                            passwordElement.reportValidity();
                        }
                    }
                }).catch(error => console.log(error));
    };
});
