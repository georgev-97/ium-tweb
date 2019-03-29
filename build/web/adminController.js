var myApp = angular.module('admin', []).controller('adminController', function ($scope, $http) {
    $http.get("/Ripetizioni/Controller", {params: {command: 'getSession'}})
            .then(response => {
                if (response.data !== "") {
                    console.log(response.data);
                    $scope.user = response.data.account;
                } else {
                    $scope.user = "anonimo";
                }
            }).catch(error => console.log(error));
    $scope.addCourse = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'addCourse', course: $scope.name, description: $scope.description}})
                .then(response => {
                    console.log(response.data);
                    if (response.data !== "") {
                        if (response.data.error === "") {
                            var x = document.getElementById("snackbar");
                            // Add the "show" class to DIV
                            x.className = "show";
                            setTimeout(function(){window.location.assign("admin.html") }, 2000);
                        } else {
                            alert(response.data.error);
                        }
                    }
                });
    };
    $scope.addProfessor = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'addProfessor', name: $scope.name, username: $scope.username, email: $scope.email}})
                .then(response => {
                    console.log(response.data);
                    if (response.data !== "") {
                        if (response.data.error === "") {
                            var x = document.getElementById("snackbar");
                            x.className = "show";
                            setTimeout(function(){window.location.assign("admin.html") }, 2000);
                        } else {
                            alert(response.data.error);
                        }
                    }
                });

    };
});
