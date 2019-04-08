var myApp = angular.module('admin', []).controller('adminController', function ($scope, $http) {
    $scope.render = false;
    $http.get("/Ripetizioni/Controller", {params: {command: 'getAutSesData'}})
            .then(response => {
                if (response.data.id === "")
                    window.location.assign("login.html");
                else if (response.data.role !== 0)
                    window.location.assign("login.html");
                else if (response.data.account === "")
                    window.location.assign("login.html");
                else {
                    $scope.user = response.data.account;
                    $scope.render = true;
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
                            setTimeout(function () {
                                window.location.assign("admin.html")
                            }, 2000);
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
                            setTimeout(function () {
                                window.location.assign("admin.html")
                            }, 2000);
                        } else {
                            alert(response.data.error);
                        }
                    }
                });
    };
    $http.get("/Ripetizioni/Controller", {params: {command: 'getCourse'}})
            .then(response => {
                $scope.courseList = response.data.courseList;
            });
    $http.get("/Ripetizioni/Controller", {params: {command: 'getProfessor'}})
            .then(response => {
                $scope.professorList = response.data.professorList;
            });
    $scope.updateCourse = function () {
        $scope.prof = $scope.professor.match(/\(.*\)/)[0]
                .replace(/\(/, "").replace(/\)/, "");
        $http.get("/Ripetizioni/Controller", {params: {command: 'getFreeCourse', professor: $scope.prof}})
                .then(response => {
                    if (response.data.error === "") {
                        $scope.courseList = response.data.courseList;
                    } else if (response.data.error === "no element") {
                        $scope.courseList = response.data.courseList;
                        window.alert("non è possibile associare il professore a nuovi corsi");
                    } else {
                        window.alert(response.data.error);
                    }
                });
    };
    $scope.updateProfessor = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'getCourseProfessor', course: $scope.course}})
                .then(response => {
                    if (response.data.error === "") {
                        $scope.professorList = response.data.professorList;
                    } else {
                        window.alert(response.data.error);
                    }
                });
    };
    $scope.submit = function () {
        $scope.prof = $scope.professor.match(/\(.*\)/)[0]
                .replace(/\(/, "").replace(/\)/, "");
        $http.get("/Ripetizioni/Controller", {params: {command: 'courseProfessor',
                course: $scope.course, professor: $scope.prof}})
                .then(response => {
                    if (response.data.error === "") {
                        var x = document.getElementById("snackbar");
                        // Add the "show" class to DIV
                        x.className = "show";
                        setTimeout(function () {
                            window.location.assign("admin.html")
                        }, 2000);
                    } else {
                        window.alert(response.data.error);
                    }
                });
    };
});
