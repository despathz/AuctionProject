myApp.controller('loginCtrl', ['$rootScope', '$scope', '$state', '$http', '$cookies', function($rootScope, $scope, $state, $http, $cookies) {
    $scope.user = {username: "", password: "", remember: false};
    $scope.tryLogin = function() {
        $scope.error = false;
        $scope.activationError = false;
        if ($scope.user.username.length == 0 || $scope.user.password.length == 0)  {
            $scope.error = true;
        }
        if ($scope.user.password.length < 5) {
            $scope.error = true;
        }
        if (!$scope.error) {
            var res = $http.post('/ws/user/login', $scope.user);
            res.success(function(response) {
                if (response.id != 0) {
                    if (response.activation) {
                        $rootScope.navPref = {username: $scope.user.username, id: response.id, isAdmin: response.superuser, loggedIn: true};
                        if (response.superuser)
                            $state.go('app.adminPage');
                        else
                            $state.go('app.welcome');
                    }
                    else
                        $scope.activationError = true;
                }
                else {
                    $scope.error = true;
                }
            });

        }
    };
}]);

myApp.controller('logoutCtrl', ['$rootScope', '$state', function($rootScope, $state) {
    $rootScope.navPref = {username: "none", loggedIn: false, isAdmin: false, id: 0};
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
		$scope.registerComplete = false;

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
                                if (!response) 
                                    $scope.databaseError = true;
                                else 
                                    $scope.registerComplete = true;
                            });
                        }	
					});
				}
			});
		}
	}
}]);

myApp.controller('profileCtrl', ['$rootScope', '$scope', '$http', '$stateParams', function($rootScope, $scope, $http, $stateParams) {
    $scope.user_id = parseInt($stateParams.id);
	
    if ($scope.user_id == 0) {   //user views his OWN profile
        $scope.user_id = $rootScope.navPref.id;
	}
    $scope.sentID = {id: $scope.user_id};
    var res = $http.post('/ws/user/getProfile', $scope.sentID);
     res.success(function(response) {
        $scope.user = response;
    });

}]);

myApp.controller('editprofileCtrl', ['$rootScope', '$scope', '$http', '$stateParams', function($rootScope, $scope, $http, $stateParams) {
    $scope.user_id = parseInt($stateParams.id);
	$scope.currentUser = "";
	
	$scope.settings = {save: false, edit: true, isAdmin: false, showBanner: false};
    if ($scope.user_id == 0) {   //user views his OWN profile
        $scope.user_id = $rootScope.navPref.id;
		$scope.settings.isAdmin = true;
	}
    $scope.sentID = {id: $scope.user_id};
    var res = $http.post('/ws/user/getProfile', $scope.sentID);
     res.success(function(response) {
        $scope.user = response;
		 $scope.currentUser = $scope.user.username;
    });
    
	$scope.editInfo = function() {
		console.log($scope.user);
		$scope.basicFieldsError = false;
		$scope.successfulUpdate = false;
		$scope.usernameExists = false; $scope.emailExists = false;
		
		if  (($scope.user.email.length == 0) || ($scope.user.name.length == 0) || ($scope.user.surname.length == 0)
			|| ($scope.user.username.length == 0)) {
				$scope.basicFieldsError = true;
        }
		
		if (!$scope.basicFieldsError) {
			var res = $http.post('/ws/user/getIDbyUsername', {username: $scope.currentUser});
        	res.success(function(response) {
				$scope.user.id = response.id;
				var res = $http.post('/ws/user/updateProfileInfo', $scope.user);
				res.success(function(response) {
					if (response == 1) {
						$scope.successfulUpdate = true;
						$scope.settings.edit = true;
					}
					else if (response == 2) {
						$scope.usernameExists = true;
					}
					else if (response == 3) {
						$scope.emailExists = true;
					}
				});
			});
		}
		
		var res = $http.post('/ws/user/getIDbyUsername', {username: $scope.user.username});
        res.success(function(response) {
			console.log("done!");
		});
	};
	
    $scope.activateUser = function() {
        var res = $http.post('/ws/user/activate', $scope.sentID);
        res.success(function(response) {
            if (response)
                $scope.user.activation = true;
        });
    };
    
     $scope.banUser = function() {
        var res = $http.post('/ws/user/ban', $scope.sentID);
        res.success(function(response) {
            if (response)
                $scope.user.activation = false;
        });
    };
}]);