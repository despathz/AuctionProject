var myApp = angular.module('myApp', ['ngRoute']);

myApp.config(function ($stateProvider) {

	$stateProvider

	.state('index', {
		url = '/'.
		templateUrl: './index.html',
		controller: 'mainController'
	})

});

myApp.controller('mainController', ['$scope', '$log', function($scope, $http) {
	$http.get("http://localhost:8080/");
}]);