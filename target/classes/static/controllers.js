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

myApp.controller('registerCtrl', ['$scope', '$http', function($scope, $http) {
	$scope.user = {remember: false, superuser: false};
	$scope.accept = false;
	$scope.tryRegister = function() {
        console.log($scope.user.accept);
        $scope.errors = {acceptError: false, usernameError: false, registerError: false};
        if (!$scope.accept) {
            $scope.errors.acceptError = true;
        }
        else {
            var res = $http.post('/ws/user/register', $scope.user);
            res.success(function(response) {
                if (!response) {
                    $scope.errors.registerError = true;
                }
            });
        }
	}
}]);