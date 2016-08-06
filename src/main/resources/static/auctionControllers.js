myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
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
    });
    
    $scope.servicePath = '/ws/bid/forAuction/' + $stateParams.id;
    var res2 = $http.get($scope.servicePath);
    res2.success(function(response) {
        var i;
        for (i in response) {
            response[i].bid_time = new Date(response[i].bid_time);
            var bidDate = response[i].bid_time.getFullYear() + "-" + ('0' + (response[i].bid_time.getMonth()+1)).slice(-2) + "-" + ('0' + response[i].bid_time.getDate()).slice(-2);
            var bidTime = ('0' + response[i].bid_time.getHours()).slice(-2) + ":" + ('0' + response[i].bid_time.getMinutes()).slice(-2) + ":" + ('0' + response[i].bid_time.getSeconds()).slice(-2);
            response[i].bid_time = bidDate + " " + bidTime;
        }
        $scope.bidList = response;
    });
    
    $scope.creatorProfile = function() {
        $state.go("app.profile", {id: $scope.auction.user_id});
    };
    
    $scope.bid = function() {
        var toSent = {amount: $scope.bidAmount, bidder: $rootScope.session.id, auction: $scope.auction.id};
        var res = $http.post('/ws/bid/add', toSent);
        res.success(function(response) {
            if (response == false)
                console.log('error');
            else
                $scope.bidAmount = "";
        });
    };
    
}]);