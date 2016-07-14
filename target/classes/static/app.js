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
            requireLogin: 2
        }
	})
    
    .state('app.login', {
        url: '/login',
        templateUrl: './views/login.html',
        controller: 'loginCtrl',
        params: { 
            title: 'Log in',
            requireLogin: 0
        }
    })
    
    .state('app.logout', {
        url: '/logout',
        controller: 'logoutCtrl',
        requireLogin: 1
    })
    
    .state('app.register', {
        url: '/register',
        templateUrl: './views/register.html',
        controller: 'registerCtrl',
        params: { 
            title: 'Register',
            requireLogin: 0
        }
    })
    
    .state('app.adminPage', {
        url: '/adminPage',
        templateUrl: './views/adminPage.html',
        controller: 'adminPageCtrl',
        params: {
            title: 'Users maintenance',
            requireLogin: 1
        }
    })

});

myApp.run(['$rootScope', '$state', function($rootScope, $state) {
    $rootScope.navPref = {username: "none", loggedIn: false};
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        $rootScope.title = toParams.title;
        console.log(toState.name);
        if (toParams.requireLogin == 0 && $rootScope.navPref.loggedIn)
            $state.go('app.welcome');
        else if (toParams.requireLogin == 1 && !$rootScope.navPref.loggedIn)
            $state.go('app.welcome');
    });
}]);