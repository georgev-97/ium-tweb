var myApp = angular.module('courseProfessor', []).controller("courseProfessorController",
        function ($scope, $http) {
            $http.get("/Ripetizioni/Controller", {params: {command: 'getCourse'}})
                    .then(response => {
                        $scope.courseList = response.data.courseList;
                    });
            $http.get("/Ripetizioni/Controller", {params: {command: 'getProfessor'}})
                    .then(response => {
                        $scope.professorList = response.data.professorList;
                    });

<<<<<<< HEAD
=======
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

>>>>>>> 8857527a59596dd259a9d474c98274f5843cf42f
            $scope.submit = function () {
                $scope.prof = $scope.professor.match(/\(.*\)/)[0]
                        .replace(/\(/, "").replace(/\)/, "");
                $http.get("/Ripetizioni/Controller", {params: {command: 'courseProfessor',
                        course: $scope.course, professor: $scope.prof}})
                        .then(response => {
                            if (response.data.error === "") {
                                window.alert("inserimento avvenuto");
                                window.location.assign("admin.html");
<<<<<<< HEAD
                            }else{
                                window.alert("errore di inserimento");
=======
                            } else {
                                window.alert(response.data.error);
>>>>>>> 8857527a59596dd259a9d474c98274f5843cf42f
                            }
                        });
            };
        });

