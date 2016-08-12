myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', 'stopwatch', 'pollingFactory', function($rootScope, $scope, $state, $stateParams, $http, $cookies, stopwatch, pollingFactory) {
    $scope.hasEnded = false;
    $scope.bidAmount = "";
    $scope.bidConfirmMessage = false;
    var res = $http.get('/ws/auction/' + $stateParams.id);
    res.success(function(response) {
        $scope.auction = response;
        $rootScope.title = $scope.auction.name;
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
    
	var res2 = $http.get('/ws/image/get/' + $stateParams.id);
    res2.success(function(response) {
        $scope.imgA = response[0];
        $scope.imgB = response[1];
<<<<<<< HEAD
=======
        console.log($scope.imgA + " " + $scope.imgB);
>>>>>>> 94bd0bbf80d2faea97143037a151e4a592382786
    });
	
    $scope.servicePath = '/ws/bid/forAuction/' + $stateParams.id;
    var res3 = $http.get($scope.servicePath);
    res3.success(function(response) {
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
        if (!isNaN($scope.bidAmount))
            $scope.bidConfirmMessage = true;
        else {
            $scope.bidMsg = "Please type a valid amount of money";
            $scope.bidAmount = "";
        }
    };
    
    $scope.confirmBid = function() {
        var toSent = {amount: $scope.bidAmount, bidder: $rootScope.session.id, auction: $scope.auction.id};
        var res = $http.post('/ws/bid/add', toSent);
        res.success(function(response) {
            if (response == true) {
                $scope.bidAmount = "";
                $scope.bidConfirmMessage = false;
                $state.go($state.current, {id: $stateParams.id}, {reload: true});
            }
        });
    };
    
    $scope.cancelBid = function() {
        $scope.bidAmount = "";
        $scope.bidConfirmMessage = false;
    };
    
    $scope.bidderProfile = function(user_id) {
        $state.go("app.profile", {id: user_id});
    };
    
}]);

myApp.controller('createAuctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', '$sce', function($rootScope, $scope, $state, $stateParams, $http, $cookies, $sce) {
	$scope.auction = {name: "", first_bid: "", description: "", buy_price:"", user_id: $rootScope.session.id, started:"" , ends:""};
	$scope.tempDate = {selectYear: "", selectMonth: "", selectDay: "", selectHour: "", selectMinute: "", selectSecond: ""};
	
	$scope.months = [{month: "Jan", number: 1}, {month: "Feb", number: 2}, {month: "Mar", number: 3}, {month: "Apr", number: 4}, {month: "May", number: 5}, {month: "Jun", number: 6}, {month: "Jul", number: 7}, {month: "Aug", number: 8}, {month: "Sep", number: 9}, {month: "Oct", number: 10}, {month: "Nov", number: 11}, {month: "Dec", number: 12}];
    
    $scope.images = {imgA : "", imgB: ""};
    
    $scope.categoryPath = "";
    $scope.categoryPathList = [];
    $scope.categoryPathList.push({name: "General", id: 1});
    var res = $http.get('/ws/category/parent/1');
    res.success(function(response) {
        $scope.categoryList = response;
    });
    
    $scope.findSubCat = function(name, id) {
        var pos = -1, i = 0;
        while (i < $scope.categoryPathList.length) {
            if ($scope.categoryPathList[i].id == id){
                pos = i;
                break;
            }
            i++;
        }
        if (pos == -1)
            $scope.categoryPathList.push({name, id});
        else {
            while ($scope.categoryPathList.length > pos + 1)
                $scope.categoryPathList.pop();
        }
        var res = $http.get('/ws/category/parent/' + id);
        res.success(function(response) {
            $scope.categoryList = response;
        });
    };
    
    $scope.loadImgA = function() {
        var f = document.getElementById("imgA");
        f.files[0],
        r = new FileReader();
        r.onloadend = function(e){
            $scope.images.imgA = e.target.result;
        }
        r.readAsDataURL(f.files[0]);
    };
    
    $scope.loadImgB = function() {
        var f = document.getElementById("imgB");
        f.files[0],
        r = new FileReader();
        r.onloadend = function(e){
            $scope.images.imgB = e.target.result;
        }
        r.readAsDataURL(f.files[0]);
    };
    
	$scope.submitAuction = function() {
		$scope.basicFieldsError = false; 
		$scope.databaseError = false;
		$scope.NumberError = false;
		
		if (isNaN(parseFloat($scope.auction.first_bid)) || isNaN(parseFloat($scope.auction.buy_price)))
			$scope.NumberError = true;
		
		$scope.auction.ends = new Date(parseInt($scope.tempDate.selectEYear), parseInt($scope.tempDate.selectEMonth.number) - 1, parseInt($scope.tempDate.selectEDay), parseInt($scope.tempDate.selectEHour), parseInt($scope.tempDate.selectEMinute), parseInt($scope.tempDate.selectESecond), 0).getTime();
		
		if (($scope.auction.name.length == 0) || ($scope.auction.description.length == 0) || (isNaN($scope.auction.ends))) {
			$scope.basicFieldsError = true;
        }
		
		$scope.auction.buy_price = parseFloat($scope.auction.buy_price);
		$scope.auction.first_bid = parseFloat($scope.auction.first_bid);
		
		if (!$scope.basicFieldsError && !$scope.NumberError) {
			var res = $http.post('/ws/auction/createAuction', $scope.auction);
			res.success(function(response) {
				if (response == -1) 
					$scope.databaseError = true;
				else {
					$state.go('app.auction', {id: response});
                    var res = $http.post('/ws/image/upload', {auction: response, imgA: $scope.images.imgA, imgB: $scope.images.imgB});
                    res.success(function(response) {
                        console.log("Images uploaded!");
                    });
				}
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

myApp.directive('customOnChange', function() {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var onChangeHandler = scope.$eval(attrs.customOnChange);
      element.bind('change', onChangeHandler);
    }
  };
});