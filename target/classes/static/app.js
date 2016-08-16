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
        url: '/auction/:id',
        templateUrl: './views/auction/auction.html',
        controller: 'auctionCtrl',
        params: {
            title: 'Auction',
            requireLogin: 2,
            id: ""
        }
    })
    
    .state('app.createAuction', {
        url: '/newAuction',
        templateUrl: './views/auction/createAuction.html',
        controller: 'createAuctionCtrl',
        params: {
            title: 'Create Auction',
            requireLogin: 1
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

myApp.run(['$rootScope', '$state', '$cookies', function($rootScope, $state, $cookies) {
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
    });
}]);

myApp.factory("stopwatch", [function () {
    function calc(ends) {
        var today = new Date();
        var sec = 0, min = 0, hour = 0, day = 0;
		var str = "Ends in ";
        sec = ~~((ends - today)/1000);
        if (sec > 60) {
            min = ~~(sec / 60);
            sec = sec - 60 * min;
            if (min > 60) {
                hour = ~~(min / 60);
                min = min - 60 * hour;
            }
            if (hour > 24) {
                day = ~~(hour / 24);
                hour = hour - 24 * day;
            }
        }
		if (day > 0) {
			str = str + day + " days, ";
			str = str + hour + " hours, ";
			str = str + min + " minutes and ";
			str = str + sec + " seconds";
		}
		else {
			if (hour > 0) {
				str = str + hour + " hours, ";
				str = str + min + " minutes and ";
				str = str + sec + " seconds";
			}
			else {
				if (min > 0) {
					str = str + min + " minutes and ";
					str = str + sec + " seconds";
				}
				else {
					if (sec > 0)
						str = str + sec + " seconds";
					else
						str = "";
				}
			}
		}
        return str;
    }
    
    return {
        calc: calc
    };
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