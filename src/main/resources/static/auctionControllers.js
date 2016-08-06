myApp.controller('auctionCtrl', ['$rootScope', '$scope', '$state', '$stateParams', '$http', '$cookies', function($rootScope, $scope, $state, $stateParams, $http, $cookies) {
    $scope.hasEnded = false;
    $scope.servicePath = '/ws/auction/' + $stateParams.id;
    var res = $http.get($scope.servicePath);
    res.success(function(response) {
        $scope.auction = response;
        if (response.id == 0) {
            console.log('invalid auction id');
        }
        var started = new Date(response.started);
        $scope.startDate = started.getFullYear() + "-" + ('0' + (started.getMonth()+1)).slice(-2) + "-" + ('0' + started.getDate()).slice(-2);
        $scope.startTime = ('0' + started.getHours()).slice(-2) + ":" + ('0' + started.getMinutes()).slice(-2) + ":" + ('0' + started.getSeconds()).slice(-2);
        var ends = new Date(response.ends);
        var today = new Date();
        if (today > ends)
            $scope.hasEnded = true;
    });
    
    $scope.creatorProfile = function() {
        $state.go("app.profile", {id: $scope.auction.user_id});
    };
}]);