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
    
    .state('app.profile', {
        url: '/profile/:id',
        templateUrl: './views/profile.html',
        controller: 'profileCtrl',
        params: {
            title: 'Profile',
            requireLogin: 1,
            id: "0"
        }
    })
    
    .state('app.message', {
        url: '/messages',
        templateUrl: './views/message/message.html',
        controller: 'messageCtrl',
        params: {
            requireLogin: 1
        }
    })
    
    .state('app.message.inbox', {
        url: '/inbox',
        templateUrl: './views/message/inbox.html',
        controller: 'inboxCtrl',
        params: {
            title: 'Messages - Inbox',
            requireLogin: 1
        }
    })
    
    .state('app.message.sent', {
        url: '/sent',
        templateUrl: './views/message/sent.html',
        controller: 'sentCtrl',
        params: {
            title: 'Messages - Sent',
            requireLogin: 1
        }
    })
    
    .state('app.message.compose', {
        url: '/compose/:to',
        templateUrl: './views/message/compose.html',
        controller: 'composeCtrl',
        params: {
            title: 'Messages - Compose',
            requireLogin: 1,
            to: ""
        }
    })
    
    .state('app.message.view', {
        url: '/view/:id',
        templateUrl: './views/message/view.html',
        controller: 'viewCtrl',
        params: {
            title: 'Messages - View',
            requireLogin: 1,
            id: ""
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