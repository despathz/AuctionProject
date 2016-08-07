myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', 'stopwatch', 'pollingFactory', function($rootScope, $scope, $state, $stateParams, $http, $cookies, stopwatch, pollingFactory) {
    $scope.hasEnded = false;
    $scope.bidAmount = "";
    $scope.servicePath = '/ws/auction/' + $stateParams.id;
    var res = $http.get($scope.servicePath);
    res.success(function(response) {
        $scope.auction = response;
        if (response.id == 0) {
            console.log('invalid auction id');
        }
        var started = new Date(response.started);
        $scope.startDate = started.getFullYear() + "-" + ('0' + (started.getMonth()+1)).slice(-2) + "-" + ('0' + started.getDate()).slice(-2);
        $scope.startTime = ('0' + started.getHours()).slice(-2) + ":" + ('0' + started.getMinutes()).slice(-2) + ":" + ('0' + started.getSeconds()).slice(-2);
        var ends = new Date(response.ends);
        var today = new Date();
        if (today > ends)
            $scope.hasEnded = true;
        else {
            pollingFactory.callFnOnInterval(function () {
                $scope.countDown = stopwatch.calc(ends);
            }, 1);
        }
    });
    
    $scope.servicePath = '/ws/bid/forAuction/' + $stateParams.id;
    var res2 = $http.get($scope.servicePath);
    res2.success(function(response) {
        var i;
        var today = new Date();
        for (i in response) {
            var sec = 0, min = 0, hour = 0;
            sec = ~~((today - response[i].bid_time)/1000);
            if (sec > 60) {
                min = ~~(sec / 60);
                sec = sec - 60 * min;
                if (min > 60) {
                    hour = ~~(min / 60);
                    min = min - 60 * hour;
                }
            }
            if (hour >= 1)
                response[i].bid_time = hour + " hours ago";
            else if (min >= 1)
                response[i].bid_time = min + " minutes ago";
            else if (sec >= 1)
                response[i].bid_time = sec + " seconds ago";
        }
        $scope.bidList = response;
    });
    
    $scope.creatorProfile = function() {
        $state.go("app.profile", {id: $scope.auction.user_id});
    };
    
    $scope.bid = function() {
        $scope.bidMsg = "";
        if (!isNaN($scope.bidAmount)) {
            var toSent = {amount: $scope.bidAmount, bidder: $rootScope.session.id, auction: $scope.auction.id};
            var res = $http.post('/ws/bid/add', toSent);
            res.success(function(response) {
                if (response == false)
                    console.log('error');
            });
        }
        else 
            $scope.bidMsg = "Please type a valid amount of money";
        $scope.bidAmount = "";
    };
    
    $scope.bidderProfile = function(user_id) {
        $state.go("app.profile", {id: user_id});
    };
    
}]);

myApp.controller('createAuctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
	$scope.auction = {name: "", firstBid: "", startDate: "", endDate: "", description: "", buyPrice:""};
}]);