myApp.controller('loginCtrl', ['$rootScope', '$scope', '$state', '$http', '$cookies', function($rootScope, $scope, $state, $http, $cookies) {
    $scope.user = {remember: false};
    $scope.tryLogin = function() {
        $scope.usernameError = false;
        $scope.passwordError = false;
        $scope.activationError = false;
        if (angular.isUndefined($scope.user.username)) {
            $scope.usernameError = true;
        }
        if (angular.isUndefined($scope.user.password)) {
            $scope.passwordError = true;
        }
        else if ($scope.user.password.length < 5) {
            $scope.passwordError = true;
        }
        if (!$scope.usernameError && !$scope.passwordError) {
            var res = $http.post('/ws/user/login', $scope.user);
            res.success(function(response) {
                console.log(response);
                if (response.username === $scope.user.username) {
                    if (response.activation) {
                        $rootScope.navPref = {username: $scope.user.username, id: response.id ,loggedIn: true};
                        if (response.superuser)
                            $state.go('app.adminPage');
                        else
                            $state.go('app.welcome');
                    }
                    else
                        $scope.activationError = true;
                }
                else {
                    $scope.usernameError = true;
                    $scope.passwordError = true;
                }
            });

        }
    };
}]);

myApp.controller('logoutCtrl', ['$rootScope', '$state', function($rootScope, $state) {
    $rootScope.navPref = {username: "none", loggedIn: false};
    $state.go('app.welcome');
}]);

myApp.controller('registerCtrl', ['$scope', '$http', '$rootScope', '$state', function($scope, $http, $rootScope, $state) {
    $scope.user = {remember: false, superuser: false, activation: false, username: "", password: "", email: "", name: "", surname: ""};
    $scope.prop = {accept: false, verifyPassword: ""};
	$scope.tryRegister = function() {
		$scope.basicFieldsError = false; //these fields must be filled
		$scope.missmatchPassError = false;
		$scope.acceptTermsError = false;
		$scope.userExists = false;
        $scope.emailExists = false;
        $scope.passwordError = false;
		$scope.databaseError = false;

		if (($scope.user.username.length == 0) || ($scope.user.password.length == 0)
			|| ($scope.prop.verifyPassword.length == 0) || ($scope.user.email.length == 0)
			|| ($scope.user.name.length == 0) || ($scope.user.surname.length == 0)) {
				$scope.basicFieldsError = true;
        }
        
        if (!$scope.basicFieldsError && $scope.user.password.length < 5) {
            $scope.passwordError = true;
        }
		else if ($scope.prop.verifyPassword !== $scope.user.password) {
			$scope.missmatchPassError = true;
		}
		if (!$scope.prop.accept) {
			$scope.acceptTermsError = true;
		}

		if (!$scope.acceptTermsError && !$scope.basicFieldsError && !$scope.missmatchPassError && !$scope.passwordError) {
			var res = $http.post('/ws/user/register/checkUsername', $scope.user);
			res.success(function(response) {
				if (response) {
                    $scope.userExists = true;
                }
				else {
					var res = $http.post('/ws/user/register/checkEmail', $scope.user);
					res.success(function(response) {
                        if (response) {
                            $scope.emailExists = true;
                        }
                        else {
                            var res = $http.post('/ws/user/register', $scope.user);
                            res.success(function(response) {
                                if (!response) {
                                    $scope.databaseError = true;
                                }
                                else {
                                    $rootScope.navPref = {username: $scope.user.username, loggedIn: true};
                                    $state.go('app.welcome');
                                }
                            });
                        }	
					});
				}
			});
		}
	}
}]);