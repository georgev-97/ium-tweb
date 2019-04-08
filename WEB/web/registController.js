var myApp = angular.module('regist', []).controller("registController",
        function ($scope, $http) {
            $scope.nameAlreadyUsed = false;
            $scope.showConfirm = false;
            $scope.checkPassword = function () {
                if ($scope.password === $scope.passwordConfirm) {
                    $scope.equal = "password corrispondenti";
                    $scope.colour = {'color': 'green'};
                } else {
                    $scope.equal = "password non corrispondenti";
                    $scope.colour = {'color': 'red'};
                }
            };
            $scope.submit = function () {
                if (window.regForm.account.value === "" ||
                        window.regForm.password.value === "" ||
                        window.regForm.passwordConfirm.value === "") {

                    window.alert("compilare tutti i campi");
                    return;
                } else if (window.regForm.password.value !==
                        window.regForm.passwordConfirm.value) {

                    window.alert("le password non corrispondono");
                } else if ($scope.nameAlreadyUsed) {
                    window.alert("il nome utente è già in uso");
                } else {
                    $http.get("/Ripetizioni/Controller", {params: {command: 'checkUser', account: $scope.account}})
                            .then(response => {
                                if (response.data.error !== "") {
                                    window.location.assign("login.html");
                                } else {
                                    var x = document.getElementById("snackbar");
                                    // Add the "show" class to DIV
                                    x.className = "show";
                                    setTimeout(function () {
                                        x.className = "hide";
                                    }, 2000);
                                }
                            });
                }
            };
            $scope.checkUser = function () {
                $http.get("/Ripetizioni/Controller", {params: {command: 'checkUser', account: $scope.account}})
                        .then(response => {
                            nameElement = document.getElementById("account");
                            console.log("eccoci");
                            console.log(response.data.response);
                            if (response.data.response === "true") {
                                nameElement.setCustomValidity("il nome utente è già in uso");
                                nameElement.reportValidity();
                                nameElement.validity = false;
                                $scope.nameAlreadyUsed = true;
                            } else {
                                nameElement.setCustomValidity("");
                                nameElement.validity = true;
                                $scope.nameAlreadyUsed = false;
                            }
                        }).catch(error => console.log(error));
            };
        });

