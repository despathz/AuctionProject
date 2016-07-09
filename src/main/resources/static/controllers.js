myApp.controller('mainController', ['$scope', function($scope) {
    
}]);

myApp.controller('loginController', ['$scope', '$http', function($scope, $http) {
    $scope.user = {remember: false};
    $scope.tryLogin = function() {
        $scope.usernameError = false;
        $scope.passwordError = false;
        if (angular.isUndefined($scope.user.username)) {
            $scope.usernameError = true;
        }
        if (angular.isUndefined($scope.user.password)) {
            $scope.passwordError = true;
        }
        else if ($scope.user.password.length < 5) {
            $scope.passwordError = true;
        }
        if (!$scope.passwordError && !$scope.usernameError) {
            console.log($scope.user);
            var res = $http.post('/ws/user/login', $scope.user);
            res.success(function(response) {
                console.log(response);
            });
       }
    };
}]);

myApp.controller('registerController', ['$scope', function($scope) {
    
}]);