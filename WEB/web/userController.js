var myApp = angular.module('user', []).controller('userController', function ($scope, $http) {
    $scope.render = false;
    $scope.slotToSubmit = "";
    $http.get("/Ripetizioni/Controller", {params: {command: 'getAutSesData'}})
            .then(response => {
                if (response.data.id === "")
                    window.location.assign("login.html");
                else if (response.data.role !== 1)
                    window.location.assign("login.html");
                else if (response.data.account === "")
                    window.location.assign("login.html");
                else {
                    $scope.user = response.data.account;
                    $scope.render = true;
                    //             //
                    //INIT DATA SET//
                    //             //
                    $scope.slot = {'9 - 11': ['f', 'f', 'f', 'f', 'f']
                        , '11 - 13': ['f', 'f', 'f', 'f', 'f']
                        , '14 - 16': ['f', 'f', 'f', 'f', 'f']
                        , '16 - 18': ['f', 'f', 'f', 'f', 'f']};
                    $scope.slotId = {'9 - 11': [], '11 - 13': [], '14 - 16': [], '16 - 18': []};

                    $http.get("/Ripetizioni/Controller", {params: {command: 'getCourse'}})
                            .then(response => {
                                $scope.courseList = response.data.courseList;
                            });
                    $http.get("/Ripetizioni/Controller", {params: {command: 'getProfessor'}})
                            .then(response => {
                                $scope.professorList = response.data.professorList;
                            });
                    $scope.getUserReservation();
                }
            }).catch(error => console.log(error));

    $scope.logout = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'logout'}});
    }; 
     
    $scope.updateProfessor = function () {

        $http.get("/Ripetizioni/Controller", {params: {command: 'getCourseProfessor'
                , course: $scope.course}})

                .then(response => {
                    $scope.slotToSubmit = "";
                    $scope.slot = {'9 - 11': ['f', 'f', 'f', 'f', 'f']
                        , '11 - 13': ['f', 'f', 'f', 'f', 'f']
                        , '14 - 16': ['f', 'f', 'f', 'f', 'f']
                        , '16 - 18': ['f', 'f', 'f', 'f', 'f']};
                    $scope.professor = null;
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
                            console.log(reservationMatrix);
                            for (var i = 0; i < reservationMatrix.length; i++) {
                                switch (reservationMatrix[i][0]) {
                                    case 'lunedì':
                                        $scope.slot[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][0] = reservationMatrix[i][3];
                                        $scope.slotId[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][0] = reservationMatrix[i][4];
                                        break;
                                    case 'martedì':
                                        $scope.slot[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][1] = reservationMatrix[i][3];
                                        $scope.slotId[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][1] = reservationMatrix[i][4];
                                        break;
                                    case 'mercoledì':
                                        $scope.slot[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][2] = reservationMatrix[i][3];
                                        $scope.slotId[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][2] = reservationMatrix[i][4];
                                        break;
                                    case 'giovedì':
                                        $scope.slot[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][3] = reservationMatrix[i][3];
                                        $scope.slotId[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][3] = reservationMatrix[i][4];
                                        break;
                                    case 'venerdì':
                                        $scope.slot[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][4] = reservationMatrix[i][3];
                                        $scope.slotId[reservationMatrix[i][1] + " - " + reservationMatrix[i][2]][4] = reservationMatrix[i][4];
                                        break;
                                }
                            }
                        } else {
                            window.alert(response.data.error);
                        }
                    });
        } else {
            $scope.slot = {'9 - 11': ['f', 'f', 'f', 'f', 'f']
                , '11 - 13': ['f', 'f', 'f', 'f', 'f']
                , '14 - 16': ['f', 'f', 'f', 'f', 'f']
                , '16 - 18': ['f', 'f', 'f', 'f', 'f']};
        }
    };
    $scope.setValue = function (key, index) {
        $scope.slotToSubmit = $scope.slotId[key][index];
    };
    $scope.reserve = function () {
        // if no one resrvation is select don't perform
        if ($scope.slotToSubmit !== "") {
            $http.get("/Ripetizioni/Controller", {params: {command: 'reserve',
                    slotId: $scope.slotToSubmit}})
                    .then(response => {
                        $scope.slotToSubmit = "";//reset the reservation selection
                        if (response.data.error === "") {
                            $scope.getReservation();

                            var x = document.getElementById("snackbar");
                            // Add the "show" class to DIV
                            x.className = "show";
                            setTimeout(function () {
                                x.className = "hide";
                            }, 2000);
                        } else {
                            window.alert(response.data.error);
                        }
                    });
        }
    };

    $scope.deleteReservation = function (rid, ruid) {
        console.log("che" + rid + "---" + ruid);
        $http.get("/Ripetizioni/Controller", {params: {command: 'deleteReservation',
                reservationId: rid, reservationUserId: ruid}})
                .then(response => {
                    $scope.getUserReservation();
                    if (response.data.error !== "") {
                        window.alert(response.data.error);
                    }
                });
    };

    $scope.getUserReservation = function () {
        $http.get("/Ripetizioni/Controller", {params: {command: 'getUserReservation'}})
                .then(response => {
                    if (response.data.error === "") {
                        $scope.userReservation = response.data.reservationList;
                        console.log($scope.userReservation);
                    } else {
                        window.alert(response.data.error);
                    }
                });
    };

});
