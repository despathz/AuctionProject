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
	$scope.auction = {name: "", first_bid: "", description: "", buy_price:"", user_id: $rootScope.session.id, started:"" , ends:""};
	$scope.tempDate = {selectYear: "", selectMonth: "", selectDay: "", selectHour: "", selectMinute: "", selectSecond: "",
					 selectEYear: "", selectEMonth: "", selectEDay: "", selectEHour: "", selectEMinute: "", selectESecond: ""};
	
	$scope.months = [{month: "Jan", number: 1}, {month: "Feb", number: 2}, {month: "Mar", number: 3}, {month: "Apr", number: 4}, {month: "May", number: 5}, {month: "Jun", number: 6}, {month: "Jul", number: 7}, {month: "Aug", number: 8}, {month: "Sep", number: 9}, {month: "Oct", number: 10}, {month: "Nov", number: 11}, {month: "Dec", number: 12}];
	
	$scope.submitAuction = function() {
		$scope.basicFieldsError = false; 
		$scope.databaseError = false;
		$scope.registerComplete = false;
		$scope.NumberError = false;
		
		if (($scope.auction.name.length == 0) || ($scope.auction.first_bid.length == 0)
			|| ($scope.auction.description.length == 0) || ($scope.auction.buy_price.length == 0)) {
				$scope.basicFieldsError = true;
        }
		
//		if (!isNaN($scope.auction.))parseInt(
		$scope.auction.started = new Date(parseInt($scope.tempDate.selectYear), parseInt($scope.tempDate.selectMonth.number) - 1, parseInt($scope.tempDate.selectDay), parseInt($scope.tempDate.selectHour), parseInt($scope.tempDate.selectMinute), parseInt($scope.tempDate.selectSecond), 0).getTime();
		$scope.auction.ends = new Date(parseInt($scope.tempDate.selectEYear), parseInt($scope.tempDate.selectEMonth.number) - 1, parseInt($scope.tempDate.selectEDay), parseInt($scope.tempDate.selectEHour), parseInt($scope.tempDate.selectEMinute), parseInt($scope.tempDate.selectESecond), 0).getTime();
		
		$scope.auction.buy_price = parseFloat($scope.auction.buy_price);
		$scope.auction.first_bid = parseFloat($scope.auction.first_bid);
		console.log($scope.auction);
		
		if (!$scope.basicFieldsError) {
			console.log("SO");
			var res = $http.post('/ws/auction/createAuction', $scope.auction);
			console.log("SO?");
			res.success(function(response) {
				if (!response) 
					$scope.databaseError = true;
				else 
					$scope.registerComplete = true;
				console.log($scope.registerComplete);
			});
		}
		
	};
	
}]);

myApp.filter('range', function() {
	return function(input, min, max) {
	min = parseInt(min); //Make string input int
	max = parseInt(max);
	for (var i=min; i<max; i++)
		input.push(i);
	return input;
	};
});

