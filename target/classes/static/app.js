var myApp = angular.module('myApp', ['ui.router', 'ngResource', 'ngCookies', 'ngMap']);

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
        url: '/login/:activation',
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
	
	.state('app.editprofile', {
        url: '/editprofile/:id',
        templateUrl: './views/editprofile.html',
        controller: 'editprofileCtrl',
        params: {
            title: 'Edit Profile',
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
    
    .state('app.auction', {
        url: '/auction/display/:id',
        templateUrl: './views/auction/auction.html',
        controller: 'auctionCtrl',
        params: {
            title: 'Auction',
            requireLogin: 2,
            id: ""
        }
    })
    
    .state('app.createAuction', {
        url: '/auction/create',
        templateUrl: './views/auction/createAuction.html',
        controller: 'createAuctionCtrl',
        params: {
            title: 'Create auction',
            requireLogin: 1
        }
    })
    
    .state('app.editAuction', {
        url: '/auction/edit/:id',
        templateUrl: './views/auction/createAuction.html',
        controller: 'editAuctionCtrl',
        params: {
            title: 'Edit auction',
            requireLogin: 1,
            id: ""
        }
    })
    
    .state('app.listingAuction', {
        url: '/search',
        templateUrl: './views/auction/listing.html',
        controller: 'AuctionListCtrl',
        params: {
            title: 'Results',
            requireLogin: 2
        }
    })

});

myApp.run(['$rootScope', '$state', '$cookies', '$interval', function($rootScope, $state, $cookies, $interval) {
	$rootScope.session = {username: 'none', loggedIn: false, isAdmin: false, id: 0};
	if (angular.isUndefined($cookies.get('loggedIn'))) {
		$cookies.putObject('username', 'none');
		$cookies.putObject('loggedIn', false);
		$cookies.putObject('isAdmin', false);
		$cookies.putObject('id', 0);
	}
	else {
		$rootScope.session.username = $cookies.getObject('username');
		$rootScope.session.loggedIn = $cookies.getObject('loggedIn');
		$rootScope.session.isAdmin = $cookies.getObject('isAdmin');
		$rootScope.session.id = $cookies.getObject('id');
	}
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        $rootScope.title = toParams.title;
        if (toParams.requireLogin == 0 && $rootScope.session.loggedIn)
            $state.go('app.welcome');
        else if (toParams.requireLogin == 1 && !$rootScope.session.loggedIn)
            $state.go('app.welcome');
        if (angular.isDefined($rootScope.promise)) {
            $interval.cancel($rootScope.promise);
            $rootScope.promise = undefined;
        }
    });
}]);

myApp.factory("notify", ['$rootScope', '$http', function ($rootScope, $http) {

    function callServer() {
        if ($rootScope.session.id != 0) {
            var res = $http.get('/ws/message/notify/' + $rootScope.session.id);   
            res.success(function(response) {
                $rootScope.newMsg = response;
            });
        }
        return (new Date());
    }

    return {
        callServer: callServer
    };
}]);

myApp.factory("pollingFactory", ['$timeout', function ($timeout) {

    function callFnOnInterval(fn, timeInterval) {

        var promise = $timeout(fn, 1000 * timeInterval);

        return promise.then(function(){
            callFnOnInterval(fn, timeInterval);
        });
    };

    return {
        callFnOnInterval: callFnOnInterval
    };
}]);