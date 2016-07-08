var myApp = angular.module('myApp', ['ngRoute', 'ngResource']);

myApp.config(function ($routeProvider) {

	$routeProvider

	.when('/', {
        title: 'welcome',
		templateUrl: './views/welcome.html',
		controller: 'mainController'
	})
    
    .when('/login', {
        title: 'Log in',
        templateUrl: './views/login.html',
        controller: 'loginController'
    })

});

myApp.run(['$rootScope', function($rootScope) {
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
        $rootScope.title = current.$$route.title;
    });
}]);

myApp.controller('mainController', ['$scope', function($scope) {
    
}]);

myApp.controller('loginController', ['$scope', '$http', function($scope, $http) {
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
        if (!$scope.passwordError && !$scope.usernameError) {
        	console.log($scope.user);
        	var res = $http.post('/ws/login', $scope.user);
        	res.success(function(response) {
        		console.log(response);
        	});
       }
    };
}]);