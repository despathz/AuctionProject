myApp.controller('navCtrl', ['$rootScope', '$scope', '$stateParams', 'notify', 'pollingFactory', function($rootScope, $scope, $stateParams, notify, pollingFactory) {
	pollingFactory.callFnOnInterval(function () {
        notify.callServer();
    }, 5);
}]);

myApp.controller('mainCtrl', ['$rootScope', '$scope', '$stateParams', function($rootScope, $scope, $stateParams) {
    $scope.isAdmin = false;
	$scope.user_id = parseInt($stateParams.id);
	if ($scope.user_id == 1)
		$scope.isAdmin = true;
}]);

myApp.controller('adminPageCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
    $http.get('/ws/user/getAll')
    .success(function(response) {
        $scope.userList = response;
    });
    $scope.viewProfile = function(userid) {
        $state.go('app.editprofile', {id: userid});
    };
}]);

myApp.controller('searchCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
    $scope.currentTab = "search";
    $scope.data = {keywords: "", from: "", to: "", location: ""};
    
    $scope.$watch('currentTab', function() {
        $scope.categoryPathList = [];
        $scope.categoryPathList.push({name: "All", id: 1});
        $http.get('/ws/category/parent/1')
        .success(function(response) {
            $scope.categoryList = response;
        });
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
        $http.get('/ws/category/parent/' + id)
        .success(function(response) {
            $scope.categoryList = response;
        });
    };
    
    $scope.browse = function() {
        var params = {keywords: null, from: null, to: null, location: null, category: $scope.categoryPathList[$scope.categoryPathList.length-1].id, page: 1};
        $state.go('app.results', params);
    };
    
    $scope.search = function() {
        $scope.numberError = false;
        
        if (isNaN($scope.data.from)) {
            $scope.numberError = true;
            $scope.data.from = "";
        }
        
        if (isNaN($scope.data.to)) {
            $scope.numberError = true;
            $scope.data.to = "";
        }
        
        if (!$scope.numberError) {
            if ($scope.data.from === "")
                $scope.data.from = 0;
            if ($scope.data.to === "")
                $scope.data.to = 0;
            $scope.data.from = parseFloat($scope.data.from);
            $scope.data.to = parseFloat($scope.data.to);
            
            if ($scope.data.keywords.length == 0 && $scope.data.location.length == 0 && $scope.data.from == 0 && $scope.data.to == 0) {
                $scope.browse();
            }
            else {
                var params = {};
                params.keywords = $scope.data.keywords;
                params.from = $scope.data.from;
                params.to = $scope.data.to;
                params.location = $scope.data.location;
                params.category = $scope.categoryPathList[$scope.categoryPathList.length-1].id;
                params.page = 1;
                $state.go('app.results', params);
            }
        }
    };
    
}]);

myApp.controller('listAuctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
    
    $scope.getResults = function(option) {
        if (option == 1) {
            $http.post('ws/search/advanced/' + $stateParams.page, {
            keywords: $stateParams.keywords, location: $stateParams.location, from: $stateParams.from, to: $stateParams.to, category: $stateParams.category
            }).success(function(response) {
                $scope.results = response;
            });
        }
        else if (option == 2) {
            $http.get('ws/search/category/' + $stateParams.category + '/' + $stateParams.page).
            success(function(response) {
                $scope.results = response;
            });
        }
    };
    
    $scope.sellerProfile = function(user_id) {
        $state.go('app.profile', {id: user_id});
    };
    
    $scope.auctionPage = function(auction_id) {
        $state.go('app.auction.display', {id: auction_id});
    };
    
    $scope.getPage = function(n) {
        $stateParams.page = n;
        $scope.currentPage = n;
        $scope.getResults($scope.option);
        $state.go($state.current, $stateParams, {notify: false});
    };
    
    if ($stateParams.location == null && $stateParams.keywords == null && $stateParams.from == null && $stateParams.to == null) {
        console.log("browse");
        $http.get('ws/search/category/matches/' + $stateParams.category).
        success(function(response) {
            $scope.matches = response;
            $scope.pages = Math.ceil($scope.matches/2);
            $scope.currentPage = $stateParams.page;
            $scope.pageNumbers = _.range(1, $scope.pages + 1);
            $scope.option = 2;
            $scope.getResults($scope.option);
        });
    }
    else {
        console.log("search");
            $scope.option = 1;
            $scope.getResults($scope.option);
    }
    
}]);