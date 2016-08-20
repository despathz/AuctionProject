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
    
    $scope.search = function() {
        $scope.numberError = false;
        
        if (isNaN($scope.data.from) || isNaN($scope.data.to))
            $scope.numberError = true;
        
        if ($scope.data.from === "")
            $scope.data.from = 0;
        
        if ($scope.data.to === "")
            $scope.data.to = 0;
        
        if (!$scope.numberError) {
            $scope.data.from = parseFloat($scope.data.from);
            $scope.data.to = parseFloat($scope.data.to);
            console.log($scope.data);
        }
        
    };
    
    $scope.browse = function() {
        var params = {keywords: null, from: null, to: null, location: null, category: $scope.categoryPathList[$scope.categoryPathList.length-1].id, page: 1};
        $state.go('app.results', params);
    };
    
}]);

myApp.controller('listAuctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
    
    $http.get('ws/search/category/matches/' + $stateParams.category).
    success(function(response) {
        $scope.pages = Math.ceil(response/2);
        $scope.pageNumbers = _.range(1, $scope.pages + 1);
        $scope.getResults();
    });
    
    $scope.getResults = function() {
        $http.get('ws/search/category/' + $stateParams.category + '/' + $stateParams.page).
        success(function(response) {
            $scope.results = response;
        });
    };
    
    $scope.sellerProfile = function(user_id) {
        $state.go('app.profile', {id: user_id});
    };
    
    $scope.auctionPage = function(auction_id) {
        $state.go('app.auction.display', {id: auction_id});
    };
    
    $scope.getPage = function(n) {
        $stateParams.page = n;
        $scope.getResults();
        $state.go($state.current, $stateParams, {notify: false});
    };
    
}]);