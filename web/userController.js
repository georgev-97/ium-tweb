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

    $http.get("/Ripetizioni/Controller", {params: {command: 'getCourse'}})
            .then(response => {
                $scope.courseList = response.data.courseList;
            });
    $http.get("/Ripetizioni/Controller", {params: {command: 'getProfessor'}})
            .then(response => {
                $scope.professorList = response.data.professorList;
            });
    $scope.updateProfessor = function () {

        $http.get("/Ripetizioni/Controller", {params: {command: 'getCourseProfessor'
                , course: $scope.course}})

                .then(response => {
                    if (response.data.error === "") {
                        $scope.professorList = response.data.professorList;
                    } else {
                        window.alert(response.data.error);
                    }
                });
    };
    $scope.getReservation = function () {
        if ($scope.professor !== null) {
            $scope.prof = $scope.professor.match(/\(.*\)/)[0]
                    .replace(/\(/, "").replace(/\)/, "");
            $http.get("/Ripetizioni/Controller", {params: {command: 'getReservation'
                    , course: $scope.course, professor: $scope.prof}})

                    .then(response => {
                        if (response.data.error === "") {
                            var reservationMatrix = response.data.reservationMatrix;
                            $scope.slot = {h9: [], h11: [], h14: [], h16: []};
                            for (var i = 0; i < reservationMatrix.length; i++) {
                                switch (reservationMatrix[i][0]) {
                                    case 'lunedì':
                                        $scope.slot[reservationMatrix[i][1]][0] = reservationMatrix[i][3];
                                        break;
                                    case 'martedì':
                                        $scope.slot[reservationMatrix[i][1]][1] = reservationMatrix[i][3];
                                        break;
                                    case 'mercoledì':
                                        $scope.slot[reservationMatrix[i][1]][2] = reservationMatrix[i][3];
                                        break;
                                    case 'giovedì':
                                        $scope.slot[reservationMatrix[i][1]][3] = reservationMatrix[i][3];
                                        break;
                                    case 'venerdì':
                                        $scope.slot[reservationMatrix[i][1]][4] = reservationMatrix[i][3];
                                        break;
                                }
                            }
                        } else {
                            window.alert(response.data.error);
                        }
                    });
        }
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