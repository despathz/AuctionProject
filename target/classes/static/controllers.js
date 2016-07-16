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