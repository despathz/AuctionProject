myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', 'stopwatch', 'pollingFactory', function($rootScope, $scope, $state, $stateParams, $http, $cookies, stopwatch, pollingFactory) {
    $scope.currentTab = "desc";
    $scope.hasEnded = false;
    $scope.bidAmount = "";
    $scope.bidConfirmMessage = false;
    $scope.XMLcreated = false;
    $scope.hasStarted = false;
	$scope.noBid = false;
    $http.get('/ws/auction/' + $stateParams.id).
    success(function(response) {
        $scope.auction = response;
		
		$http.get('/ws/bid/forAuction/' + $stateParams.id).
		success(function(response) {
			$scope.currentBid = response;
			if ($scope.currentBid.amount == 0) 
				$scope.noBid = true;
			
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
			if (hour >= 24)
				$scope.currentBid.bid_time = ~~(hour/24) + "days ago";
			else if (hour >= 1)
				$scope.currentBid.bid_time = hour + " hours ago";
			else if (min >= 1)
				$scope.currentBid.bid_time = min + " minutes ago";
			else if (sec >= 1)
				$scope.currentBid.bid_time = sec + " seconds ago";
			else
				$scope.currentBid.bid_time = "a moment ago";
		});
		
        $rootScope.title = $scope.auction.name;
		$scope.coord = [parseFloat($scope.auction.latitude), parseFloat($scope.auction.longitude)];
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
				$scope.countDown = "-1";
                pollingFactory.callFnOnInterval(function () {
					console.log("hey");
					if ($scope.countDown !== "")
						 $scope.countDown = stopwatch.calc(ends);
					else
						$scope.hasEnded = true;
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
        else if (!$scope.noBid && !isNaN($scope.bidAmount) && parseFloat($scope.bidAmount) <= $scope.currentBid.amount) {
            $scope.bidMsg = "Please bid more than the current bid amount";
            $scope.bidAmount = "";
        }
		else if ($scope.noBid && !isNaN($scope.bidAmount) && parseFloat($scope.bidAmount) <= $scope.auction.first_bid) {
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
                $scope.countDown = "-1";
                pollingFactory.callFnOnInterval(function () {
					if ($scope.countDown !== "")
						 $scope.countDown = stopwatch.calc(ends);
					else 
						$scope.hasEnded = true;
                }, 1);
            }
        });
    };
    
}]);

myApp.controller('createAuctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
	$scope.auction = {name: "", first_bid: "", description: "", country: "", location: ""};
	$scope.tempDate = {selectEYear: "", selectEMonth: "", selectEDay: "", selectEHour: "", selectEMinute: "", selectESecond: ""};
	$scope.coord = [41, 5.6];
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
    
	$scope.dragMarker = function() {
		$scope.coord = [this.getPosition().lat(), this.getPosition().lng()];
	};

	$scope.dragMap = function() {
		$scope.coord = [this.getCenter().lat(), this.getCenter().lng()];
	};
	
	$scope.submitAuction = function() {
		$scope.basicFieldsError = false; 
		$scope.databaseError = false;
		$scope.NumberError = false;
		$scope.NumberBPError = false;
		$scope.categoryError = false;
		$scope.futureDateError = false;
		
		if ($scope.categoryList.length != 0)
			$scope.categoryError = true;
		
		if (isNaN($scope.auction.first_bid))
			$scope.NumberError = true;
		if  (isNaN($scope.buy_price.amount))
			$scope.NumberBPError = true;
		
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
		
		if (!$scope.basicFieldsError && !$scope.NumberError && !$scope.NumberBPError && !$scope.futureDateError && !$scope.categoryError) {
            
            if ($scope.buy_price.amount.length == 0)
                $scope.buy_price.amount = 0;
			
			if ($scope.coord[0] == 41 && $scope.coord[1] == 5.6) {
				$scope.coord[0] = 0;
				$scope.coord[1] = 0;
			}
			
			$scope.auction.longitude = $scope.coord[1];
			$scope.auction.latitude = $scope.coord[0];
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

myApp.controller('AuctionListCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
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