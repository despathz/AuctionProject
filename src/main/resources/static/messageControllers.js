myApp.controller('messageCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {

}]);

myApp.controller('inboxCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {
    
}]);

myApp.controller('sentCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {
    
}]);

myApp.controller('composeCtrl', ["$rootScope", '$scope', '$http', function($rootScope, $scope, $http) {
    $scope.compose = {textbody: "", username: "", title: ""};
    
    $scope.eraseText = function() {
        $scope.compose.textbody = ""; 
    };
    
    $scope.sendMessage = function() {
        $scope.errors = {bodyError: false, usernameError: false, titleError: false};
        if ($scope.compose.username === "")
            $scope.errors.usernameError = true;
        if ($scope.compose.textbody === "")
            $scope.errors.bodyError = true;
         if ($scope.compose.title === "")
            $scope.errors.titleError = true;
        var res = $http.post('/ws/user/register/checkUsername', $scope.compose);
        res.success(function(response) {
            $scope.errors.usernameError = !response;
            if (!$scope.errors.usernameError && !$scope.errors.bodyError && !$scope.errors.titleError) {
                
            }
        });
    }
}]);