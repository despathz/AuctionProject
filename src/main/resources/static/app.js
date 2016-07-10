var myApp = angular.module('myApp', ['ui.router', 'ngResource', 'ngCookies']);

myApp.config(function ($stateProvider) {

	$stateProvider
    
    .state('app', {
        abstract: true,
        views: {
            'nav': {
                templateUrl: './views/nav.html',
                controller: 'navCtrl'
            },
            '': {
                templateUrl: './views/body.html'
            }
        }
    })

	.state('app.welcome', {
        url: '/' ,
        templateUrl: './views/welcome.html',
        controller: 'mainCtrl',
        params: { 
           title: 'Home',
        }
	})
    
    .state('app.login', {
        url: '/login',
        templateUrl: './views/login.html',
        controller: 'loginCtrl',
        params: { 
           title: 'Log in',
        }
    })
    
    .state('app.logout', {
        url: '/logout',
        controller: 'logoutCtrl'
    })
    
    .state('app.register', {
        url: '/register',
        templateUrl: './views/register.html',
        controller: 'registerCtrl',
        params: { 
           title: 'Register',
        }
    })

});

myApp.run(['$rootScope', function($rootScope) {
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        $rootScope.title = toParams.title;
    });
}]);