myApp.controller('messageCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {

}]);

myApp.controller('inboxCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {
    
}]);

myApp.controller('sentCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {
    
}]);

myApp.controller('composeCtrl', ["$rootScope", '$scope', function($rootScope, $scope) {
    $scope.compose = {textbody: "", sentUsername: ""};
    $scope.eraseText = function() {
        $scope.compose.textbody = ""; 
    };
}]);