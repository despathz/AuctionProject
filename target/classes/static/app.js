var myApp = angular.module('myApp', ['ngRoute']);

myApp.config(function ($routeProvider) {

	$routeProvider

	.when('/', {
		templateUrl: './views/welcome.html',
		controller: 'mainController'
	})
    
    .when('/login', {
        templateUrl: './views/login.html',
        controller: 'loginController'
    })

});

myApp.controller('mainController', ['$scope', function($scope) {
    
}]);

myApp.controller('loginController', ['$scope', '$http', function($scope, $http) {
	$http.get('ws/login').success(function(response) {
		$scope.user = response;
	});
	
}]);