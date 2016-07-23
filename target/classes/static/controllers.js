myApp.controller('navCtrl', ['$rootScope', '$scope', '$stateParams', function($rootScope, $scope, $stateParams) {
	
}]);

myApp.controller('mainCtrl', ['$rootScope', '$scope', '$stateParams', function($rootScope, $scope, $stateParams) {
    $scope.isAdmin = false;
	$scope.user_id = parseInt($stateParams.id);
	console.log($scope.user_id);
	console.log($scope.user_id);
	if ($scope.user_id == 1)
		$scope.isAdmin = true;
	console.log($scope.isAdmin);
}]);

myApp.controller('adminPageCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
    var res = $http.get('/ws/user/getAll');
    res.success(function(response) {
        $scope.userList = response;
    });
    $scope.viewProfile = function(userid) {
        $state.go('app.editprofile', {id: userid});
    };
}]);

myApp.controller('profileCtrl', ['$rootScope', '$scope', '$http', '$stateParams', function($rootScope, $scope, $http, $stateParams) {
    $scope.user_id = parseInt($stateParams.id);
	
    if ($scope.user_id == 0) {   //user views his OWN profile
        $scope.user_id = $rootScope.navPref.id;
	}
    $scope.sentID = {id: $scope.user_id};
    var res = $http.post('/ws/user/getProfile', $scope.sentID);
     res.success(function(response) {
        $scope.user = response;
    });

}]);

myApp.controller('editprofileCtrl', ['$rootScope', '$scope', '$http', '$stateParams', function($rootScope, $scope, $http, $stateParams) {
    $scope.user_id = parseInt($stateParams.id);
	$scope.currentUser = "";
	
	$scope.settings = {save: false, edit: true, isAdmin: false, showBanner: false};
    if ($scope.user_id == 0) {   //user views his OWN profile
        $scope.user_id = $rootScope.navPref.id;
		$scope.settings.isAdmin = true;
	}
    $scope.sentID = {id: $scope.user_id};
    var res = $http.post('/ws/user/getProfile', $scope.sentID);
     res.success(function(response) {
        $scope.user = response;
		 $scope.currentUser = $scope.user.username;
    });
    
	$scope.editInfo = function() {
		console.log($scope.user);
		$scope.basicFieldsError = false;
		$scope.successfulUpdate = false;
		$scope.usernameExists = false; $scope.emailExists = false;
		
		if  (($scope.user.address.length == 0) || ($scope.user.telephone.length == 0) || ($scope.user.email.length == 0) 
			|| ($scope.user.country.length == 0) || ($scope.user.name.length == 0) || ($scope.user.surname.length == 0)
			|| ($scope.user.trn.length == 0) || ($scope.user.username.length == 0)) {
				$scope.basicFieldsError = true;
        }
		
		if (!$scope.basicFieldsError) {
			var res = $http.post('/ws/user/getIDbyUsername', {username: $scope.currentUser});
        	res.success(function(response) {
				$scope.user.id = response.id;
				var res = $http.post('/ws/user/updateProfileInfo', $scope.user);
				res.success(function(response) {
					if (response == 1) {
						$scope.successfulUpdate = true;
						$scope.settings.edit = true;
					}
					else if (response == 2) {
						$scope.usernameExists = true;
					}
					else if (response == 3) {
						$scope.emailExists = true;
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