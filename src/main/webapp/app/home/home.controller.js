(function () {
    'use strict';
    angular.module('predictimoApp').controller('HomeController', HomeController);
    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$http'];

    function HomeController($scope, Principal, LoginService, $state, $http) {
        var vm = this;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.openPredictions = [];
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });
        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        
        getOpenPredictions();

        function getOpenPredictions() {
            $http({
                method: 'GET'
                , url: '/api/predictions/open'
            }).then(function successCallback(response) {
                vm.openPredictions = response.data;
            }, function errorCallback(response) {

            });
        }

        function register() {
            $state.go('register');
        }
    }
})();