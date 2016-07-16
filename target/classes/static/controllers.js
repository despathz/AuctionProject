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

myApp.controller('profileCtrl', ['$scope', '$http', '$stateParams', function($scope, $http, $stateParams) {
    $scope.user = {id: $stateParams.id};
}]);