myApp.controller('messageCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {
}]);

myApp.controller('inboxCtrl', ["$rootScope", '$scope', '$http', function($rootScope, $scope, $http) {
    var res = $http.post('/ws/message/inbox', {id: $rootScope.navPref.id});
    res.success(function(response) {
        $scope.inbox = response;
    });
}]);

myApp.controller('sentCtrl', ["$rootScope", '$scope', '$http', function($rootScope, $scope, $http) {
    var res = $http.post('/ws/message/sent', {id: $rootScope.navPref.id});
    res.success(function(response) {
        $scope.sent = response;
    });
}]);

myApp.controller('composeCtrl', ["$rootScope", '$scope', '$http', function($rootScope, $scope, $http) {
    $scope.compose = {text: "", username: "", title: ""};
    
    $scope.eraseText = function() {
        $scope.compose.text = ""; 
    };
    
    $scope.sendMessage = function() {
        $scope.errors = {textError: false, usernameError: false, titleError: false};
        if ($scope.compose.username === "")
            $scope.errors.usernameError = true;
        if ($scope.compose.text === "")
            $scope.errors.textError = true;
        if ($scope.compose.title === "")
            $scope.errors.titleError = true;
        var res = $http.post('/ws/user/getIDbyUsername', {username: $scope.compose.username});
        res.success(function(response) {
            if (response.id == 0 || response.id == $rootScope.navPref.id)
                $scope.errors.usernameError = true;
            if (!$scope.errors.usernameError && !$scope.errors.textError && !$scope.errors.titleError) {
                var res = $http.post('/ws/message/send', {text: $scope.compose.text, title: $scope.compose.title, from: $rootScope.navPref.id, to: response.id});
                res.success(function(response) {
                    if (response)
                        $scope.compose = {text: "", username: "", title: ""};
                });
            }
        });
    }
}]);