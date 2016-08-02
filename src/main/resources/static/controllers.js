myApp.controller('navCtrl', ['$rootScope', '$scope', '$stateParams', 'notify', 'pollingFactory', function($rootScope, $scope, $stateParams, notify, pollingFactory) {
	pollingFactory.callFnOnInterval(function () {
        notify.callServer();
    }, 5);
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