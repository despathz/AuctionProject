myApp.controller('navCtrl', ['$rootScope', function($rootScope) {
    $rootScope.navPref = {username: "none", loggedIn: false};
}]);

myApp.controller('mainCtrl', ['$scope', function($scope) {
    
}]);

myApp.controller('loginCtrl', ['$rootScope', '$scope', '$state', '$http', '$cookies', function($rootScope, $scope, $state, $http, $cookies) {
    $scope.user = {remember: false};
    $scope.tryLogin = function() {
        $scope.usernameError = false;
        $scope.passwordError = false;
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
            console.log($scope.user);
            var res = $http.post('/ws/user/login', $scope.user);
            res.success(function(response) {
                console.log(response);
                if (response.username === $scope.user.username && response.password === $scope.user.password) {
                    $rootScope.navPref = {username: $scope.user.username, loggedIn: true};
                    $state.go('app.welcome');
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
	$scope.user = {remember: false, superuser: false};
	$scope.prop = {accept: false};
	$scope.tryRegister = function() {
		$scope.basicFieldsError = false; //these fields must be filled
		$scope.missmatchPassError = false;
		$scope.acceptTermsError = false;
		$scope.userExists = false;

		if ((angular.isUndefined($scope.user.username)) || (angular.isUndefined($scope.user.password)) 
			|| (angular.isUndefined($scope.prop.verifyPassword)) || (angular.isUndefined($scope.user.email)) 
			|| (angular.isUndefined($scope.user.name)) || (angular.isUndefined($scope.user.surname))) {
				$scope.basicFieldsError = true;
			}
		if ($scope.prop.verifyPassword !== $scope.user.password) {
			$scope.missmatchPassError = true;
		}
		if (!$scope.prop.accept) {
			$scope.acceptTermsError = true;
		}
			
		if (!$scope.acceptTermsError && !$scope.basicFieldsError && !$scope.missmatchPassError) {
			console.log($scope.prop); //for debugging
			var data = {
                "user": $scope.user,
                "userExists": !$scope.userExists
            };

			console.log(data.userExists);
			var res = $http.post('/ws/user/register', data);
			res.success(function(response) {
				if (response.username === $scope.user.username) {
                    $scope.userExists = true;
					console.log($scope.userExists);
                }
				else {
					$scope.userExists = false;
					$rootScope.navPref = {username: $scope.user.username, loggedIn: true};
					$state.go('app.welcome');
				}	
			});
		}
	};
}]);