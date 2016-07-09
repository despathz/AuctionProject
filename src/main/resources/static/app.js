var myApp = angular.module('myApp', ['ngRoute', 'ngResource']);

myApp.config(function ($routeProvider) {

	$routeProvider

	.when('/', {
        title: 'Welcome',
		templateUrl: './views/welcome.html',
		controller: 'mainController'
	})
    
    .when('/login', {
        title: 'Log in',
        templateUrl: './views/login.html',
        controller: 'loginController'
    })

    .when('/register', {
    title: 'Register',
    templateUrl: './views/register.html',
    controller: 'registerController'
    })

});

myApp.run(['$rootScope', '$location', function($rootScope, $location) {
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
        $rootScope.title = current.$$route.title;
        $rootScope.location = $location.path();
    });
}]);