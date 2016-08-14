myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', 'stopwatch', 'pollingFactory', function($rootScope, $scope, $state, $stateParams, $http, $cookies, stopwatch, pollingFactory) {
    $scope.hasEnded = false;
    $scope.bidAmount = "";
    $scope.bidConfirmMessage = false;
    $scope.XMLcreated = false;
    $scope.hasStarted = false;
    $http.get('/ws/auction/' + $stateParams.id).
    success(function(response) {
        $scope.auction = response;
        $rootScope.title = $scope.auction.name;
        if (response.id == 0) {
            console.log('invalid auction id');
        }
        if (response.started != null) {
            var started = new Date(response.started);
            $scope.startDate = started.getFullYear() + "-" + ('0' + (started.getMonth()+1)).slice(-2) + "-" + ('0' + started.getDate()).slice(-2);
            $scope.startTime = ('0' + started.getHours()).slice(-2) + ":" + ('0' + started.getMinutes()).slice(-2) + ":" + ('0' + started.getSeconds()).slice(-2);
            $scope.hasStarted = true;
        }
        if ($scope.hasStarted == true) {
            var ends = new Date(response.ends);
            var today = new Date();
            if (today > ends)
                $scope.hasEnded = true;
            else {
                pollingFactory.callFnOnInterval(function () {
                    $scope.countDown = stopwatch.calc(ends);
                }, 1);
            }
        }
    });
    
	$http.get('/ws/image/get/' + $stateParams.id).
    success(function(response) {
        $scope.imgA = response[0];
        $scope.imgB = response[1];
        if ($scope.imgA === "" && $scope.imgB !== "") {
            $scope.imgA = $scope.imgB;
            $scope.imgB = "";
        }
    });
	
    $http.get('/ws/bid/forAuction/' + $stateParams.id).
    success(function(response) {
        $scope.currentBid = response;
        var i;
        var today = new Date();
        var sec = 0, min = 0, hour = 0;
        sec = ~~((today - $scope.currentBid.bid_time)/1000);
        if (sec > 60) {
            min = ~~(sec / 60);
            sec = sec - 60 * min;
            if (min > 60) {
                hour = ~~(min / 60);
                min = min - 60 * hour;
            }
        }
        if (hour >= 1)
            $scope.currentBid.bid_time = hour + " hours ago";
        else if (min >= 1)
            $scope.currentBid.bid_time = min + " minutes ago";
        else if (sec >= 1)
            $scope.currentBid.bid_time = sec + " seconds ago";
    });
    
    $scope.getXML = function() {
        var res = $http.get('/ws/xml/produce/' + $stateParams.id);
        res.success(function(response) {
            $scope.xmlFile = response + ".xml";
            $scope.XMLcreated = true;
        });
    };
    
    $scope.creatorProfile = function() {
        $state.go("app.profile", {id: $scope.auction.user_id});
    };
    
    $scope.bid = function() {
        $scope.bidMsg = "";
        if (isNaN($scope.bidAmount) || $scope.bidAmount.length == 0) {
            $scope.bidMsg = "Please type a valid amount of money";
            $scope.bidAmount = "";
        }
        else if (!isNaN($scope.bidAmount) && parseFloat($scope.bidAmount) <= $scope.currentBid.amount) {
            $scope.bidMsg = "Please bid more than the current bid amount";
            $scope.bidAmount = "";
        }
        else
            $scope.bidConfirmMessage = true;
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
    
    $scope.begin = function() {
        $http.get('/ws/auction/begin/' + $stateParams.id).
        success(function(response) {
            var started = new Date(response);
            $scope.startDate = started.getFullYear() + "-" + ('0' + (started.getMonth()+1)).slice(-2) + "-" + ('0' + started.getDate()).slice(-2);
            $scope.startTime = ('0' + started.getHours()).slice(-2) + ":" + ('0' + started.getMinutes()).slice(-2) + ":" + ('0' + started.getSeconds()).slice(-2);
            $scope.hasStarted = true;
            var ends = new Date($scope.auction.ends);
            var today = new Date();
            if (today > ends)
                $scope.hasEnded = true;
            else {
                pollingFactory.callFnOnInterval(function () {
                    $scope.countDown = stopwatch.calc(ends);
                }, 1);
            }
        });
    };
    
}]);

myApp.controller('createAuctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
	$scope.auction = {name: "", first_bid: "", description: "", country: "", location: ""};
	$scope.tempDate = {selectEYear: "", selectEMonth: "", selectEDay: "", selectEHour: "", selectEMinute: "", selectESecond: ""};
	$scope.tempLL = {longitude: "", latitude: ""};
	$scope.buy_price = {amount: ""};
    
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
		$scope.NumberBPError = false;
		$scope.LLError = false;
		$scope.categoryError = false;
		$scope.futureDateError = false;
		
		if ($scope.categoryList.length != 0)
			$scope.categoryError = true;
		
		if (isNaN($scope.auction.first_bid))
			$scope.NumberError = true;
		if  (isNaN($scope.buy_price.amount))
			$scope.NumberBPError = true;
		
		
		if ($scope.tempLL.latitude.length != 0 && isNaN($scope.tempLL.latitude))
			$scope.LLError = true;
		if ($scope.tempLL.longitude.length != 0 && isNaN($scope.tempLL.longitude))
			$scope.LLError = true;
		
		for (var field in $scope.auction) {
			if ($scope.auction[field].length == 0) {
				$scope.basicFieldsError = true;
				break;
			}
		}
		
		for (var field in $scope.tempDate) {
			if ($scope.tempDate[field].length == 0) {
				$scope.basicFieldsError = true;
				break;
			}
		}
		
		$scope.checkDate = new Date(parseInt($scope.tempDate.selectEYear), parseInt($scope.tempDate.selectEMonth.number) - 1, parseInt($scope.tempDate.selectEDay), parseInt($scope.tempDate.selectEHour), parseInt($scope.tempDate.selectEMinute), parseInt($scope.tempDate.selectESecond), 0).getTime();
			
		if ($scope.checkDate <= (new Date().getTime()))
			$scope.futureDateError = true;
		
		if (!$scope.basicFieldsError && !$scope.NumberError && !$scope.NumberBPError && !$scope.LLError && !$scope.futureDateError && !$scope.categoryError) {
			if ($scope.tempLL.latitude.length == 0)
				$scope.tempLL.latitude = 0;
			if ($scope.tempLL.longitude.length == 0)
				$scope.tempLL.longitude = 0;
            
            if ($scope.buy_price.amount.length == 0)
                $scope.buy_price.amount = 0;
			
			$scope.auction.longitude = $scope.tempLL.longitude;
			$scope.auction.latitude = $scope.tempLL.latitude;
			$scope.auction.user_id = $rootScope.session.id;
            $scope.auction.buy_price = $scope.buy_price.amount;
			
			$scope.auction.ends = $scope.checkDate;
			
			$scope.auction.categoryList = $scope.categoryPathList;
			
			$http.post('/ws/auction/createAuction', $scope.auction).
			success(function(response) {
				if (response == -1) 
					$scope.databaseError = true;
				else {
                    $scope.auction.id = response;
                    $http.post('/ws/image/upload', {id: $scope.auction.id, imgA: $scope.images.imgA, imgB: $scope.images.imgB}).
                    success(function(response) {
                        $state.go('app.auction', {id: $scope.auction.id});
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