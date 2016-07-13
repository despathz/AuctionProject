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
           title: 'Home'
        }
	})
    
    .state('app.login', {
        url: '/login',
        templateUrl: './views/login.html',
        controller: 'loginCtrl',
        params: { 
            title: 'Log in',
            requireLogin: false
        }
    })
    
    .state('app.logout', {
        url: '/logout',
        controller: 'logoutCtrl',
        requireLogin: true
    })
    
    .state('app.register', {
        url: '/register',
        templateUrl: './views/register.html',
        controller: 'registerCtrl',
        params: { 
            title: 'Register',
            requireLogin: false
        }
    })
    
    .state('app.adminPage', {
        url: '/adminPage',
        templateUrl: './views/adminPage.html',
        controller: 'adminPageCtrl',
        params: {
            title: 'Users maintenance',
            requireLogin: true
        }
    })

});

myApp.run(['$rootScope', '$state', function($rootScope, $state) {
    $rootScope.navPref = {username: "none", loggedIn: false};
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
        $rootScope.title = toParams.title;
        if (!angular.isUndefined(toParams.requireLogin)) {
            if (toParams.requireLogin != $rootScope.navPref.loggedIn)
                $state.go('app.welcome');
        }
    });
}]);