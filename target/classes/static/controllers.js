myApp.controller('navCtrl', ['$rootScope', function($rootScope) {

}]);

myApp.controller('mainCtrl', ['$scope', function($scope) {
    
}]);

myApp.controller('adminPageCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
    var res = $http.get('/ws/user/getAll');
    res.success(function(response) {
        $scope.userList = response;
    });
    $scope.viewProfile = function(userid) {
        $state.go('app.profile', {id: userid});
    };
}]);

myApp.controller('profileCtrl', ['$rootScope', '$scope', '$http', '$stateParams', function($rootScope, $scope, $http, $stateParams) {
    $scope.user_id = parseInt($stateParams.id);
	
    if ($scope.user_id == 0)    //user views his OWN profile
        $scope.user_id = $rootScope.navPref.id;
    $scope.settings = {save: false, edit: true};
    $scope.sentID = {id: $scope.user_id};
    var res = $http.post('/ws/user/getProfile', $scope.sentID);
     res.success(function(response) {
        $scope.user = response;
    });
    
	$scope.editInfo = function() {
		console.log("MAOUUU");
		console.log($scope.user);
		$scope.basicFieldsError = false;
		$scope.successfulUpdate = false;
		if (($scope.user.address.length == 0)|| ($scope.user.telephone.length == 0)
			|| ($scope.user.country.length == 0) || ($scope.user.name.length == 0) || ($scope.user.surname.length == 0)) {
				$scope.basicFieldsError = true;
        }
		
		if (!$scope.basicFieldsError) {
			var res = $http.post('/ws/user/getIDbyUsername', {username: $scope.user.username});
        	res.success(function(response) {
				console.log("done!");
				$scope.user.id = response.id;
				console.log($scope.user.id);
				var res = $http.post('/ws/user/updateProfileInfo', $scope.user);
				res.success(function(response) {
					if (response) {
						$scope.successfulUpdate = true;
						$scope.settings.edit = true;
					}
				});
			});
		}
		
		var res = $http.post('/ws/user/getIDbyUsername', {username: $scope.user.username});
        res.success(function(response) {
			console.log("done!");
		});
	};
	
    $scope.activateUser = function() {
        var res = $http.post('/ws/user/activate', $scope.sentID);
        res.success(function(response) {
            if (response)
                $scope.user.activation = true;
        });
    };
    
     $scope.banUser = function() {
        var res = $http.post('/ws/user/ban', $scope.sentID);
        res.success(function(response) {
            if (response)
                $scope.user.activation = false;
        });
    };
}]);