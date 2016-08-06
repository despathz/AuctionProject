myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
    $scope.servicePath = '/ws/auction/' + $stateParams.id;
    var res = $http.get($scope.servicePath);
    res.success(function(response) {
        $scope.auction = response;
        if (response.id == 0) {
            console.log('invalid auction id');
        }
        console.log(response.started);
        console.log(new Date());
    });
    
    $scope.creatorProfile = function() {
        $state.go("app.profile", {id: $scope.auction.user_id});
    };
}]);